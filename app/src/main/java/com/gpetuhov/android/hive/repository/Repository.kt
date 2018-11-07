package com.gpetuhov.android.hive.repository

import androidx.lifecycle.MutableLiveData
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.firestore.*
import com.gpetuhov.android.hive.domain.model.Message
import com.gpetuhov.android.hive.domain.model.User
import com.gpetuhov.android.hive.util.Constants
import timber.log.Timber
import java.util.*
import com.gpetuhov.android.hive.domain.repository.Repo
import com.gpetuhov.android.hive.managers.LocationManager
import org.imperiumlabs.geofirestore.GeoFirestore
import org.imperiumlabs.geofirestore.GeoQuery
import org.imperiumlabs.geofirestore.GeoQueryDataEventListener
import java.lang.Exception

// Read and write data to remote storage (Firestore)
class Repository : Repo {

    companion object {
        private const val TAG = "Repo"
        private const val USERS_COLLECTION = "users"
        private const val CHATROOMS_COLLECTION = "chatrooms"
        private const val MESSAGES_COLLECTION = "messages"
        private const val NAME_KEY = "name"
        private const val USERNAME_KEY = "username"
        private const val EMAIL_KEY = "email"
        private const val SERVICE_KEY = "service"
        private const val IS_VISIBLE_KEY = "is_visible"
        private const val IS_ONLINE_KEY = "is_online"
        private const val LOCATION_KEY = "l"
        private const val SENDER_UID_KEY = "sender_uid"
        private const val TIMESTAMP_KEY = "timestamp"
        private const val MESSAGE_TEXT_KEY = "message_text"
    }

    // Firestore is the single source of truth for the currentUser property.
    // currentUser is updated every time we write data to the corresponding
    // Firestore document (with the uid of the current user).
    // Current user is wrapped inside LiveData
    // so that the UI can easily observe changes and update itself.
    // So the sequence of updates is:
    // 1. Write data to Firestore
    // 2. currentUser is updated
    // 3. UI that observes currentUser (through ViewModel) is updated
    private val currentUser = MutableLiveData<User>()

    // For searchResult the single source of truth is also Firestore.
    // Sequence of updates:
    // 1. Data in Firestore is updated
    // 2. searchResult is updated
    // 3. UI the observes searchResult is updated
    private val searchResult = MutableLiveData<MutableMap<String, User>>()
    private val tempSearchResult = mutableMapOf<String, User>()

    private val searchUserDetails = MutableLiveData<User>()

    private val messages = MutableLiveData<MutableList<Message>>()

    private var isAuthorized = false
    private var currentUserUid: String = ""
    private var queryText = ""
    private val firestore = FirebaseFirestore.getInstance()
    private var geoFirestore: GeoFirestore      // GeoFirestore is used to query users by location
    private var geoQuery: GeoQuery? = null
    private var currentChatRoomUid = ""
    private var currentUserListenerRegistration: ListenerRegistration? = null
    private var searchUserDetailsListenerRegistration: ListenerRegistration? = null
    private var messagesListenerRegistration: ListenerRegistration? = null

    init {
        // Offline data caching is enabled by default in Android.
        // But we enable it explicitly to be sure.
        val settings = FirebaseFirestoreSettings.Builder()
            .setPersistenceEnabled(true)
            .build()

        firestore.firestoreSettings = settings

        geoFirestore = GeoFirestore(firestore.collection(USERS_COLLECTION))

        resetCurrentUser()
        clearResult()
        resetSearchUserDetails()
    }

    // === Repo ===

    override fun onSignIn(user: User) {
        if (!isAuthorized) {
            isAuthorized = true

            // Current user's uid initially comes from Firebase Auth,
            // so we must save it to start getting updates
            // for the corresponding documents in Firestore.
            currentUserUid = user.uid
            startGettingCurrentUserUpdates()

            // Current user's name and email initially come from Firebase Auth,
            // so after successful sign in we must write them to Firestore.
            saveUserNameAndEmail(user)
        }
    }

    override fun onSignOut() {
        if (isAuthorized) {
            isAuthorized = false
            stopGettingCurrentUserUpdates()
            stopGettingSearchResultUpdates()
            resetCurrentUser()
        }
    }

    override fun currentUser() = currentUser

    override fun currentUserUid() = currentUser.value?.uid ?: ""

    override fun currentUserUsername() = currentUser.value?.username ?: ""

    override fun currentUserService() = currentUser.value?.service ?: ""

    override fun saveUserUsername(newUsername: String, onError: () -> Unit) {
        val data = HashMap<String, Any>()
        data[USERNAME_KEY] = newUsername

        saveUserDataRemote(data, { /* Do nothing */ }, onError)
    }

    override fun saveUserService(newService: String, onError: () -> Unit) {
        val data = HashMap<String, Any>()
        data[SERVICE_KEY] = newService

        saveUserDataRemote(data, { /* Do nothing */ }, onError)
    }

    override fun deleteUserService(onError: () -> Unit) {
        val data = HashMap<String, Any>()
        data[SERVICE_KEY] = ""
        data[IS_VISIBLE_KEY] = false

        saveUserDataRemote(data, { /* Do nothing */ }, onError)
    }

    override fun saveUserVisibility(newIsVisible: Boolean, onError: () -> Unit) {
        val data = HashMap<String, Any>()
        data[IS_VISIBLE_KEY] = newIsVisible

        saveUserDataRemote(data, { /* Do nothing */ }, onError)
    }

    override fun saveUserLocation(newLocation: LatLng) =
        geoFirestore.setLocation(currentUserUid, GeoPoint(newLocation.latitude, newLocation.longitude))

    override fun saveUserOnlineStatus(newIsOnline: Boolean, onComplete: () -> Unit) {
        val data = HashMap<String, Any>()
        data[IS_ONLINE_KEY] = newIsOnline

        saveUserDataRemote(data, onComplete, onComplete)
    }

    override fun deleteUserDataRemote(onSuccess: () -> Unit, onError: () -> Unit) {
        if (isAuthorized) {
            firestore.collection(USERS_COLLECTION).document(currentUserUid)
                .delete()
                .addOnSuccessListener {
                    Timber.tag(TAG).d("User data successfully deleted")
                    onSuccess()

                }
                .addOnFailureListener {
                    Timber.tag(TAG).d("Error deleting user data")
                    onError()
                }

        } else {
            onError()
        }
    }

    override fun searchResult() = searchResult

    override fun search(queryLatitude: Double, queryLongitude: Double, queryRadius: Double, queryText: String, onComplete: () -> Unit) {
        this.queryText = queryText

        if (isAuthorized
            && queryLatitude != Constants.Map.DEFAULT_LATITUDE
            && queryLongitude != Constants.Map.DEFAULT_LONGITUDE
            && queryRadius != Constants.Map.DEFAULT_RADIUS) {

            clearTempResult()
            stopGettingSearchResultUpdates()

            Timber.tag(TAG).d("Start search: lat = $queryLatitude, lon = $queryLongitude, radius = $queryRadius")

            val queryLocation = GeoPoint(queryLatitude, queryLongitude)

            geoQuery = geoFirestore.queryAtLocation(queryLocation, queryRadius)

            geoQuery?.addGeoQueryDataEventListener(object : GeoQueryDataEventListener {
                override fun onGeoQueryReady() {
                    Timber.tag(TAG).d("onGeoQueryReady")
                    updateSearchResult()
                    onComplete()
                }

                override fun onDocumentExited(doc: DocumentSnapshot?) {
                    Timber.tag(TAG).d("onDocumentExited")
                    Timber.tag(TAG).d(doc.toString())
                    removeUserFromSearchResults(doc?.id)
                }

                override fun onDocumentChanged(doc: DocumentSnapshot?, geoPoint: GeoPoint?) {
                    Timber.tag(TAG).d("onDocumentChanged")
                    Timber.tag(TAG).d(doc.toString())
                    updateUserInSearchResult(doc, geoPoint)
                }

                override fun onDocumentEntered(doc: DocumentSnapshot?, geoPoint: GeoPoint?) {
                    Timber.tag(TAG).d("onDocumentEntered")
                    Timber.tag(TAG).d(doc.toString())
                    updateUserInSearchResult(doc, geoPoint)
                }

                override fun onDocumentMoved(doc: DocumentSnapshot?, geoPoint: GeoPoint?) {
                    Timber.tag(TAG).d("onDocumentMoved")
                    Timber.tag(TAG).d(doc.toString())
                    updateUserInSearchResult(doc, geoPoint)
                }

                override fun onGeoQueryError(exception: Exception?) {
                    Timber.tag(TAG).d(exception)
                    updateSearchResult()
                    onComplete()
                }
            })

        } else {
            onComplete()
        }
    }

    override fun stopGettingSearchResultUpdates() = geoQuery?.removeAllListeners() ?: Unit

    override fun searchUserDetails() = searchUserDetails

    override fun initSearchUserDetails(uid: String) {
        // Get first update of user details from the search results, which are already available
        val user = searchResult.value?.get(uid)
        if (user != null) searchUserDetails.value = user
    }

    override fun startGettingSearchUserDetailsUpdates(uid: String) {
        searchUserDetailsListenerRegistration = startGettingUserUpdates(uid) { user -> searchUserDetails.value = user }
    }

    override fun stopGettingSearchUserDetailsUpdates() = searchUserDetailsListenerRegistration?.remove() ?: Unit

    override fun messages(): MutableLiveData<MutableList<Message>> = messages

    override fun startGettingMessagesUpdates(userUid1: String, userUid2: String) {
        if (isAuthorized) {
            // Chatroom collection consists of chatroom documents with chatroom uids.
            // Chatroom uid is calculated as userUid1_userUid2
            // Each chatroom document contains subcollection, which contains chatroom messages.
            // Hierarchy:
            // Chatrooms_collection -> Chatroom_document -> Messages_collection -> Message_document

            // This is needed for the chat room to have the same name,
            // despite of the uid of the user, who started the conversation.
            currentChatRoomUid = if (userUid1 < userUid2) "${userUid1}_$userUid2" else "${userUid2}_$userUid1"

            messagesListenerRegistration = firestore
                .collection(CHATROOMS_COLLECTION).document(currentChatRoomUid)
                .collection(MESSAGES_COLLECTION)
                .orderBy(TIMESTAMP_KEY, Query.Direction.DESCENDING)
                .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                    if (firebaseFirestoreException == null) {
                        Timber.tag(TAG).d("Listen success")

                        val messagesList = mutableListOf<Message>()

                        if (querySnapshot != null) {
                            for (doc in querySnapshot.documents) {
                                messagesList.add(getMessageFromDocumentSnapshot(doc))
                            }

                        } else {
                            Timber.tag(TAG).d("Listen failed")
                        }

                        messages.value = messagesList

                    } else {
                        Timber.tag(TAG).d(firebaseFirestoreException)
                    }
                }
        }
    }

    override fun stopGettingMessagesUpdates() {
        messagesListenerRegistration?.remove()
        currentChatRoomUid = ""
    }

    override fun sendMessage(messageText: String, onError: () -> Unit) {
        if (isAuthorized && currentChatRoomUid != "") {
            val data = HashMap<String, Any>()
            data[SENDER_UID_KEY] = currentUserUid
            data[MESSAGE_TEXT_KEY] = messageText
            data[TIMESTAMP_KEY] = FieldValue.serverTimestamp()  // Get timestamp on the server, not on the device

            firestore
                .collection(CHATROOMS_COLLECTION).document(currentChatRoomUid)
                .collection(MESSAGES_COLLECTION)
                .add(data)
                .addOnSuccessListener {
                    Timber.tag(TAG).d("Message successfully sent")
                }
                .addOnFailureListener { error ->
                    Timber.tag(TAG).d("Error sending message")
                    onError()
                }

        } else {
            onError()
        }
    }

    // === Private methods ===

    private fun resetCurrentUser() {
        currentUser.value = createAnonymousUser()
        currentUserUid = ""
    }

    private fun clearResult() {
        clearTempResult()
        updateSearchResult()
    }

    private fun clearTempResult() = tempSearchResult.clear()

    private fun resetSearchUserDetails() {
        searchUserDetails.value = createAnonymousUser()
    }

    private fun updateUserInSearchResult(doc: DocumentSnapshot?, geoPoint: GeoPoint?) {
        if (doc != null && doc.id != currentUser.value?.uid) {
            val user = getUserFromDocumentSnapshot(doc, geoPoint)

            if (checkConditions(user)) {
                tempSearchResult[user.uid] = user
                updateSearchResult()
            } else {
                removeUserFromSearchResults(user.uid)
            }
        }
    }

    private fun checkConditions(user: User): Boolean = user.isVisible && checkQueryText(user)

    private fun checkQueryText(user: User): Boolean {
        return user.service.contains(queryText, true)
                || user.name.contains(queryText, true)
                || user.username.contains(queryText, true)
    }

    private fun removeUserFromSearchResults(uid: String?) {
        if (uid != null) {
            tempSearchResult.remove(uid)
            updateSearchResult()
        }
    }

    private fun updateSearchResult() {
        searchResult.value = tempSearchResult
    }

    private fun createAnonymousUser(): User {
        return User(
            uid = "",
            name = Constants.Auth.DEFAULT_USER_NAME,
            username = "",
            email = Constants.Auth.DEFAULT_USER_MAIL,
            service = "",
            isVisible = false,
            isOnline = false,
            location = Constants.Map.DEFAULT_LOCATION
        )
    }

    private fun saveUserNameAndEmail(user: User) {
        val data = HashMap<String, Any>()
        data[NAME_KEY] = user.name
        data[EMAIL_KEY] = user.email

        saveUserDataRemote(data, { /* Do nothing */ }, { /* Do nothing */ })
    }

    private fun saveUserDataRemote(data: HashMap<String, Any>, onSuccess: () -> Unit, onError: () -> Unit) {
        if (isAuthorized) {
            firestore.collection(USERS_COLLECTION).document(currentUserUid)
                .set(data, SetOptions.merge())  // this is needed to update only the required data if the user exists
                .addOnSuccessListener {
                    Timber.tag(TAG).d("User data successfully written")
                    onSuccess()
                }
                .addOnFailureListener { error ->
                    Timber.tag(TAG).d("Error writing user data")
                    onError()
                }

        } else {
            onError()
        }
    }

    private fun startGettingCurrentUserUpdates() {
        currentUserListenerRegistration = startGettingUserUpdates(currentUserUid) { user ->
            // Turn off visibility if user is visible and has no services
            // (this is needed in case service has been cleared on the backend)
            if (!user.hasService && user.isVisible) saveUserVisibility(false) { /* Do nothing */ }

            // If current user is visible, start sharing location,
            // otherwise stop sharing.
            LocationManager.shareLocation(user.isVisible)

            currentUser.value = user
        }
    }

    private fun stopGettingCurrentUserUpdates() = currentUserListenerRegistration?.remove()

    private fun startGettingUserUpdates(uid: String, onSuccess: (User) -> Unit): ListenerRegistration? {
        var listenerRegistration: ListenerRegistration? = null

        if (isAuthorized) {
            listenerRegistration = firestore.collection(USERS_COLLECTION).document(uid)
                .addSnapshotListener { snapshot, firebaseFirestoreException ->
                    if (firebaseFirestoreException == null) {
                        if (snapshot != null && snapshot.exists()) {
                            Timber.tag(TAG).d("Listen success")
                            onSuccess(getUserFromDocumentSnapshot(snapshot))

                        } else {
                            Timber.tag(TAG).d("Listen failed")
                        }

                    } else {
                        Timber.tag(TAG).d(firebaseFirestoreException)
                    }
                }
        }

        return listenerRegistration
    }

    private fun getUserFromDocumentSnapshot(doc: DocumentSnapshot) = getUserFromDocumentSnapshot(doc, null)

    private fun getUserFromDocumentSnapshot(doc: DocumentSnapshot, geoPoint: GeoPoint?): User {
        val location = if (geoPoint != null) {
            getUserLocationFromGeoPoint(geoPoint)
        } else {
            getUserLocationFromDocumentSnapshot(doc)
        }

        return User(
            uid = doc.id,
            name = doc.getString(NAME_KEY) ?: Constants.Auth.DEFAULT_USER_NAME,
            username = doc.getString(USERNAME_KEY) ?: "",
            email = doc.getString(EMAIL_KEY) ?: Constants.Auth.DEFAULT_USER_MAIL,
            service = doc.getString(SERVICE_KEY) ?: "",
            isVisible = doc.getBoolean(IS_VISIBLE_KEY) ?: false,
            isOnline = doc.getBoolean(IS_ONLINE_KEY) ?: false,
            location = location
        )
    }

    private fun getUserLocationFromGeoPoint(geoPoint: GeoPoint) = LatLng(geoPoint.latitude, geoPoint.longitude)

    private fun getUserLocationFromDocumentSnapshot(doc: DocumentSnapshot): LatLng {
        val coordinatesList = doc.get(LOCATION_KEY) as List<*>?

        return if (coordinatesList != null && coordinatesList.size == 2) {
            LatLng(coordinatesList[0] as Double, coordinatesList[1] as Double)
        } else {
            Constants.Map.DEFAULT_LOCATION
        }
    }

    private fun getMessageFromDocumentSnapshot(doc: DocumentSnapshot): Message {
        return Message(
            senderUid = doc.getString(SENDER_UID_KEY) ?: "",
            timestamp = doc.getTimestamp(TIMESTAMP_KEY)?.seconds ?: 0,
            text = doc.getString(MESSAGE_TEXT_KEY) ?: ""
        )
    }
}