package com.gpetuhov.android.hive.repository

import android.content.Context
import android.net.Uri
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.firestore.*
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.storage.FirebaseStorage
import com.gpetuhov.android.hive.util.Constants
import timber.log.Timber
import java.util.*
import com.gpetuhov.android.hive.domain.repository.Repo
import com.gpetuhov.android.hive.managers.LocationManager
import org.imperiumlabs.geofirestore.GeoFirestore
import org.imperiumlabs.geofirestore.GeoQuery
import org.imperiumlabs.geofirestore.GeoQueryDataEventListener
import java.lang.Exception
import android.graphics.Bitmap
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.storage.UploadTask
import com.gpetuhov.android.hive.domain.model.*
import com.gpetuhov.android.hive.util.Settings
import kotlin.collections.HashMap

// Read and write data to remote storage (Firestore)
class Repository(private val context: Context, private val settings: Settings) : Repo {

    companion object {
        private const val TAG = "Repo"

        // Collections
        private const val USERS_COLLECTION = "users"
        private const val CHATROOMS_COLLECTION = "chatrooms"
        private const val USER_NAME_AND_PIC_COLLECTION = "userNameAndPic"
        private const val MESSAGES_COLLECTION = "messages"
        private const val USER_CHATROOMS_COLLECTION = "userChatrooms"
        private const val CHATROOMS_OF_USER_COLLECTION = "chatroomsOfUser"
        private const val USER_FAVORITES_COLLECTION = "userFavorites"
        private const val FAVORITES_OF_USER_COLLECTION = "favoritesOfUser"
        private const val REVIEWS_COLLECTION = "reviews"
        private const val REVIEWS_OF_OFFER_COLLECTION = "reviewsOfOffer"

        // User
        private const val NAME_KEY = "name"
        private const val USERNAME_KEY = "username"
        private const val EMAIL_KEY = "email"
        private const val DESCRIPTION_KEY = "description"
        private const val USER_PIC_URL_KEY = "userPicUrl"
        private const val OFFER_LIST_KEY = "offerList"
        private const val OFFER_RATING_LIST_KEY = "offerRatingList"
        private const val PHOTO_LIST_KEY = "photoList"
        private const val IS_ONLINE_KEY = "is_online"
        private const val LOCATION_KEY = "l"
        private const val FCM_TOKEN_KEY = "fcm_token"
        private const val CREATION_TIMESTAMP_KEY = "creationTimestamp"
        private const val FIRST_OFFER_PUBLISHED_TIMESTAMP_KEY = "firstOfferPublishedTimestamp"
        private const val PHONE_KEY = "phone"
        private const val VISIBLE_EMAIL_KEY = "visibleEmail"
        private const val SKYPE_KEY = "skype"

        // Photo
        private const val PHOTO_UID_KEY = "photoUid"
        private const val PHOTO_DOWNLOAD_URL_KEY = "photoDownloadUrl"

        // Offer
        private const val OFFER_UID_KEY = "offer_uid"
        private const val OFFER_TITLE_KEY = "offer_title"
        private const val OFFER_DESCRIPTION_KEY = "offer_description"
        private const val OFFER_ACTIVE_KEY = "offer_active"
        private const val OFFER_FREE_KEY = "offer_free"
        private const val OFFER_PRICE_KEY = "offer_price"
        private const val OFFER_PHOTO_LIST_KEY = "offer_photo_list"

        // Offer rating
        private const val OFFER_RATING_KEY = "offer_rating"
        private const val OFFER_REVIEW_COUNT_KEY = "offer_review_count"
        private const val OFFER_LAST_REVIEW_AUTHOR_NAME_KEY = "offer_last_review_author_name"
        private const val OFFER_LAST_REVIEW_AUTHOR_PIC_URL_KEY = "offer_last_review_author_pic"
        private const val OFFER_LAST_REVIEW_TEXT_KEY = "offer_last_review_text"
        private const val OFFER_LAST_REVIEW_TIME_KEY = "offer_last_review_timestamp"

        // Message
        private const val SENDER_UID_KEY = "sender_uid"
        private const val RECEIVER_UID_KEY = "receiver_uid"
        private const val TIMESTAMP_KEY = "timestamp"
        private const val MESSAGE_TEXT_KEY = "message_text"
        private const val MESSAGE_IS_READ_KEY = "isRead"

        // Chatroom
        private const val CHATROOM_SECOND_USER_UID_KEY = "secondUserUid"
        private const val CHATROOM_SECOND_USER_NAME_KEY = "secondUserName"
        private const val CHATROOM_SECOND_USER_PIC_URL_KEY = "secondUserPicUrl"
        private const val CHATROOM_LAST_MESSAGE_TEXT_KEY = "lastMessageText"
        private const val CHATROOM_LAST_MESSAGE_TIMESTAMP_KEY = "lastMessageTimestamp"
        private const val CHATROOM_NEW_MESSAGE_COUNT_KEY = "newMessageCount"

        // Favorites
        private const val FAVORITE_USER_UID_KEY = "userUid"
        private const val FAVORITE_OFFER_UID_KEY = "offerUid"

        // Reviews
        private const val REVIEW_PROVIDER_USER_UID_KEY = "providerUserUid"
        private const val REVIEW_OFFER_UID_KEY = "offerUid"
        private const val REVIEW_AUTHOR_UID_KEY = "authorUid"
        private const val REVIEW_AUTHOR_NAME_KEY = "authorName"
        private const val REVIEW_AUTHOR_USER_PIC_KEY = "authorUserPicUrl"
        private const val REVIEW_TEXT_KEY = "text"
        private const val REVIEW_RATING_KEY = "rating"
        private const val REVIEW_TIMESTAMP_KEY = "timestamp"
        private const val REVIEW_COMMENT_KEY = "comment"
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

    // Second user in the chat and user from search results
    private val secondUser = MutableLiveData<User>()

    // For searchResult the single source of truth is also Firestore.
    // Sequence of updates:
    // 1. Data in Firestore is updated
    // 2. searchResult is updated
    // 3. UI the observes searchResult is updated
    // (Same sequence of changes is used for all other data, that Repository provides)
    private val searchResult = MutableLiveData<MutableMap<String, User>>()
    private val tempSearchResult = mutableMapOf<String, User>()

    // Messages of the current chatroom
    // (chatroom is the chat between current user and second user)
    private val messages = MutableLiveData<MutableList<Message>>()

    // Value is true if unread messages exist
    private val unreadMessagesFlag = MutableLiveData<Boolean>()

    // Chatrooms of the current user
    private val chatrooms = MutableLiveData<MutableList<Chatroom>>()

    // Favorites of the current user
    private val favorites = mutableListOf<Favorite>()

    // Favorite users of the current user
    private val favoriteUsers = MutableLiveData<MutableList<User>>()

    // Favorite offers of the current user
    private val favoriteOffers = MutableLiveData<MutableList<Offer>>()

    private val loadedUsersList = mutableListOf<User>()
    private var loadedUsersCounter = 0

    // Reviews of the current offer
    private val reviews = MutableLiveData<MutableList<Review>>()

    private var isAppInForeground = false

    private var isUserInChatroomsList = false

    private var isUserInChatroom = false

    // True if current user is authorized
    private var isAuthorized = false

    // Query text used to search users on map
    private var queryText = ""

    // Uid of the current chatroom
    private var currentChatRoomUid = ""

    // Counts how many times chatroom list has been updated,
    // since we started listening to its changes.
    private var chatroomsUpdateCounter = 0

    // Firestore
    private val firestore = FirebaseFirestore.getInstance()

    // GeoFirestore is used to query users by location
    private var geoFirestore: GeoFirestore
    private var geoQuery: GeoQuery? = null

    // Cloud Storage
    private var storage = FirebaseStorage.getInstance()

    // Keeps Storage upload tasks for cancellation if needed
    private var uploadTasks = mutableListOf<UploadTask>()

    private var isUserDetailsActive = false
    private var isOfferDetailsActive = false

    // Firestore listeners
    private var currentUserListenerRegistration: ListenerRegistration? = null

    // These four are needed because offer details fragment onResume
    // is called BEFORE user details fragment onPause
    // (so we need separate listener registrations for user details, offer details, location and chat fragments).
    private var secondUserListenerRegistration: ListenerRegistration? = null
    private var secondUserOfferListenerRegistration: ListenerRegistration? = null
    private var secondUserLocationListenerRegistration: ListenerRegistration? = null
    private var secondUserChatListenerRegistration: ListenerRegistration? = null

    private var messagesListenerRegistration: ListenerRegistration? = null
    private var chatroomsListenerRegistration: ListenerRegistration? = null
    private var favoritesListenerRegistration: ListenerRegistration? = null
    private var reviewsListenerRegistration: ListenerRegistration? = null

    init {
        // Offline data caching is enabled by default in Android.
        // But we enable it explicitly to be sure.
        val settings = FirebaseFirestoreSettings.Builder()
            .setPersistenceEnabled(true)
            .build()

        firestore.firestoreSettings = settings

        geoFirestore = GeoFirestore(firestore.collection(USERS_COLLECTION))

        resetCurrentUser()
        resetSecondUser()
        clearResult()
        resetChatrooms()
        initUnreadMessagesFlag()
    }

    // === Repo ===
    // --- App status ---

    override fun isForeground() = isAppInForeground

    override fun setForeground(value: Boolean) {
        isAppInForeground = value
    }

    override fun isChatroomListOpen() = isUserInChatroomsList

    override fun setChatroomListOpen(value: Boolean) {
        isUserInChatroomsList = value
    }

    // Return true if chat with secondUserUid is open
    override fun isChatroomOpen(secondUserUid: String) = isUserInChatroom && secondUserUid == secondUserUid()

    override fun setChatroomOpen(value: Boolean) {
        isUserInChatroom = value
    }

    // --- Authentication ---

    override fun onSignIn(user: User) {
        if (!isAuthorized) {
            isAuthorized = true

            // Current user's uid initially comes from Firebase Auth,
            // so we must save it to start getting updates
            // for the corresponding documents in Firestore.
            val tempUser = createAnonymousUser()
            tempUser.uid = user.uid
            currentUser.value = tempUser
            startGettingCurrentUserUpdates()
            startGettingFavoritesUpdates()

            // Current user's name and email initially come from Firebase Auth,
            // so after successful sign in we must write them to Firestore.
            saveUserNameEmailPicAndToken(user)
        }
    }

    override fun onSignOut() {
        if (isAuthorized) {
            isAuthorized = false
            stopGettingCurrentUserUpdates()
            stopGettingFavoritesUpdates()
            stopGettingSearchResultUpdates()
            resetCurrentUser()
        }
    }

    // --- User ---

    override fun currentUser() = currentUser

    override fun secondUser() = secondUser

    override fun currentUserUid() = currentUser.value?.uid ?: ""

    override fun currentUserUsername() = currentUser.value?.username ?: ""

    override fun currentUserDescription() = currentUser.value?.description ?: ""

    override fun currentUserPhone() = currentUser.value?.phone ?: ""

    override fun currentUserVisibleEmail() = currentUser.value?.visibleEmail ?: ""

    override fun currentUserEmail() = currentUser.value?.email ?: ""

    override fun currentUserSkype() = currentUser.value?.skype ?: ""

    override fun saveUserUsername(newUsername: String, onError: () -> Unit) {
        val data = HashMap<String, Any>()
        data[USERNAME_KEY] = newUsername

        // Save user name.
        saveUserDataRemote(data, { updateUserNameAndPicCollection() }, onError)
    }

    override fun saveUserDescription(newDescription: String, onError: () -> Unit) {
        val data = HashMap<String, Any>()
        data[DESCRIPTION_KEY] = newDescription

        // Save user description.
        saveUserDataRemote(data, { /* Do nothing */ }, onError)
    }

    override fun saveUserLocation(newLocation: LatLng) {
        if (isAuthorized && currentUserUid() != "") {
            geoFirestore.setLocation(currentUserUid(), GeoPoint(newLocation.latitude, newLocation.longitude))
        }
    }

    override fun saveUserPhone(newPhone: String, onError: () -> Unit) {
        val data = HashMap<String, Any>()
        data[PHONE_KEY] = newPhone

        // Save user phone
        saveUserDataRemote(data, { /* Do nothing */ }, onError)
    }

    override fun saveUserVisibleEmail(newEmail: String, onError: () -> Unit) {
        val data = HashMap<String, Any>()
        data[VISIBLE_EMAIL_KEY] = newEmail

        // Save user visible email
        saveUserDataRemote(data, { /* Do nothing */ }, onError)
    }

    override fun saveUserSkype(newSkype: String, onError: () -> Unit) {
        val data = HashMap<String, Any>()
        data[SKYPE_KEY] = newSkype

        // Save user Skype
        saveUserDataRemote(data, { /* Do nothing */ }, onError)
    }

    override fun deleteUserDataRemote(onSuccess: () -> Unit, onError: () -> Unit) {
        if (isAuthorized && currentUserUid() != "") {
            firestore.collection(USERS_COLLECTION).document(currentUserUid())
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

    override fun startGettingSecondUserUpdates(uid: String) {
        stopGettingSecondUserUpdates()
        secondUserListenerRegistration = startSecondUserUpdates(uid)
        isUserDetailsActive = true
    }

    override fun stopGettingSecondUserUpdates() {
        isUserDetailsActive = false
        removeListener(secondUserListenerRegistration)
    }

    override fun startGettingSecondUserOfferUpdates(uid: String) {
        stopGettingSecondUserOfferUpdates()
        secondUserOfferListenerRegistration = startSecondUserUpdates(uid)
        isOfferDetailsActive = true
    }

    override fun stopGettingSecondUserOfferUpdates() {
        isOfferDetailsActive = false
        removeListener(secondUserOfferListenerRegistration)
    }

    override fun startGettingSecondUserLocationUpdates(uid: String) {
        stopGettingSecondUserLocationUpdates()
        secondUserLocationListenerRegistration = startSecondUserUpdates(uid)
    }

    override fun stopGettingSecondUserLocationUpdates() = removeListener(secondUserLocationListenerRegistration)

    override fun startGettingSecondUserChatUpdates(uid: String) {
        stopGettingSecondUserChatUpdates()
        secondUserChatListenerRegistration = startSecondUserUpdates(uid)
    }

    override fun stopGettingSecondUserChatUpdates() = removeListener(secondUserChatListenerRegistration)

    // --- Search ---

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

    override fun initSearchUserDetails(uid: String) {
        // Get first update of user details from the search results, which are already available
        val user = searchResult.value?.get(uid)
        if (user != null) secondUser.value = user
    }

    override fun initSecondUser(uid: String, name: String, userPicUrl: String) {
        val secondUserValue = createAnonymousUser()
        secondUserValue.uid = uid
        secondUserValue.name = name
        secondUserValue.userPicUrl = userPicUrl
        secondUser.value = secondUserValue
    }

    override fun saveFcmToken(token: String) {
        val data = HashMap<String, Any>()
        data[FCM_TOKEN_KEY] = token

        saveUserDataRemote(data, { /* Do nothing */ }, { /* Do nothing */ })
    }

    // --- Message ---

    override fun messages(): MutableLiveData<MutableList<Message>> = messages

    override fun startGettingMessagesUpdates() {
        // This is needed for the chat room to have the same name,
        // despite of the uid of the user, who started the conversation.
        currentChatRoomUid = if (currentUserUid() < secondUserUid()) "${currentUserUid()}_${secondUserUid()}" else "${secondUserUid()}_${currentUserUid()}"

        if (isAuthorized && currentChatRoomUid != "") {
            // Chatroom collection consists of chatroom documents with chatroom uids.
            // Chatroom uid is calculated as userUid1_userUid2
            // Each chatroom document contains subcollection, which contains chatroom messages.
            // Hierarchy:
            // Chatrooms_collection -> Chatroom_document -> Messages_collection -> Message_document

            messagesListenerRegistration = getMessagesCollectionReference()
                .orderBy(TIMESTAMP_KEY, Query.Direction.DESCENDING)
                .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                    if (firebaseFirestoreException == null) {
                        Timber.tag(TAG).d("Messages listen success")

                        val messagesList = mutableListOf<Message>()

                        if (querySnapshot != null) {
                            for (doc in querySnapshot.documents) {
                                val message = getMessageFromDocumentSnapshot(doc)
                                messagesList.add(message)

                                // If message is not from the current user and is not read, then mark it as read
                                // (because the receiver has just read this message).
                                // New message counter of the receiver's chatroom
                                // is decremented by the SERVER (Cloud Functions).
                                if (!message.isFromCurrentUser && !message.isRead) markMessageAsRead(message.uid)
                            }

                        } else {
                            Timber.tag(TAG).d("Messages listen failed")
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
            data[SENDER_UID_KEY] = currentUserUid()
            data[RECEIVER_UID_KEY] = secondUserUid()
            data[MESSAGE_TEXT_KEY] = messageText
            data[TIMESTAMP_KEY] = FieldValue.serverTimestamp()  // Get timestamp on the server, not on the device
            data[MESSAGE_IS_READ_KEY] = false

            // Send message to the current chatroom
            getMessagesCollectionReference()
                .add(data)
                .addOnSuccessListener {
                    Timber.tag(TAG).d("Message successfully sent")
                }
                .addOnFailureListener { error ->
                    Timber.tag(TAG).d("Error sending message")
                    onError()
                }

            // Chatrooms for current and second user are updated from the Cloud Functions
            // on every new chat message creation.

        } else {
            onError()
        }
    }

    override fun clearMessages() {
        messages.value = mutableListOf()
    }

    // --- Chatroom ---

    override fun chatrooms() = chatrooms

    override fun startGettingChatroomsUpdates() {
        if (isAuthorized && currentUserUid() != "") {
            // We keep a collection of chatrooms for every user.
            // This is needed to easily display a list of all chats,
            // that current user participates in.
            // Chatrooms of users are saved in a separate collection.
            // Hierarchy:
            // userChatrooms_collection -> User_document -> chatroomsOfUser_collection -> Chatroom_document
            // (Note that chatrooms of users are saved NOT in the users collection,
            // but in a separate userChatrooms collection)

            chatroomsUpdateCounter = 0

            chatroomsListenerRegistration = getChatroomsCollectionReference()
                .orderBy(CHATROOM_LAST_MESSAGE_TIMESTAMP_KEY, Query.Direction.DESCENDING)
                .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                    if (firebaseFirestoreException == null) {
                        Timber.tag(TAG).d("Listen success")

                        val chatroomList = mutableListOf<Chatroom>()
                        var chatroomsContainUnreadMessages = false

                        if (querySnapshot != null) {
                            for (doc in querySnapshot.documents) {
                                val chatroom = getChatroomFromDocumentSnapshot(doc)
                                chatroomList.add(chatroom)
                                if (chatroom.newMessageCount > 0) chatroomsContainUnreadMessages = true
                            }

                        } else {
                            Timber.tag(TAG).d("Listen failed")
                        }

                        // Do not call update unread messages flag
                        // on first time listener is triggered,
                        // because first time is just the first read from Firestore
                        // (nothing has changed yet).
                        if (chatroomsUpdateCounter > 0) setUnreadMessagesExist(chatroomsContainUnreadMessages)

                        chatrooms.value = chatroomList

                        chatroomsUpdateCounter++

                    } else {
                        Timber.tag(TAG).d(firebaseFirestoreException)
                    }
                }
        }
    }

    override fun stopGettingChatroomsUpdates() {
        chatroomsListenerRegistration?.remove() ?: Unit
        chatroomsUpdateCounter = 0
    }

    // === Unread messages ===

    override fun unreadMessagesExist() = unreadMessagesFlag

    override fun setUnreadMessagesExist(value: Boolean) {
        unreadMessagesFlag.value = value
        settings.setUnreadMessagesExist(value)
    }

    // === User pic ===

    override fun changeUserPic(selectedImageUri: Uri, onError: () -> Unit) {
        // Get path to current user's pic in Cloud Storage
        // (every user has his own folder with the same name as user's uid).
        // File in the cloud will be recreated on every new upload, so there
        // will be no unused old files.
        val storagePath = "${currentUserUid()}/userpic.jpg"

        uploadImage(
            selectedImageUri,
            storagePath,
            Constants.Image.USER_PIC_SIZE,
            true,
            false,
            { downloadUrl -> saveUserPicUrl(downloadUrl) },
            onError
        )
    }

    // === User photo ===

    override fun addUserPhoto(selectedImageUri: Uri, onError: () -> Unit) {
        addImage(
            selectedImageUri,
            true,
            { photoUid, downloadUrl -> saveUserPhotoUrl(photoUid, downloadUrl) },
            onError
        )
    }

    override fun deleteUserPhoto(photoUid: String, onError: () -> Unit) {
        // Photo is deleted in 2 steps:
        // 1. Delete photo from user photo list in Firestore
        // 2. On success delete image file from Cloud Storage

        val photoList = currentUserPhotoList()

        val photoIndex = photoList.indexOfFirst { it.uid == photoUid }

        if (photoIndex >= 0 && photoIndex < photoList.size) {
            photoList.removeAt(photoIndex)
            saveUserPhotoList(photoList, { deleteImage(photoUid, true) }, onError)

        } else {
            onError()
        }
    }

    // === Offer ===

    override fun currentUserOfferList() = currentUser.value?.offerList ?: mutableListOf()

    override fun saveOffer(offer: Offer?, onSuccess: () -> Unit, onError: () -> Unit) {
        if (isAuthorized && offer != null) {
            val offerList = currentUserOfferList()
            var photoUidsToDeleteFromStorage = mutableListOf<String>()

            // If offer uid is not empty, then offer already exist.
            // Update it with new data.
            if (offer.uid != "") {
                // Remove deleted photos from offer photo list
                // and save uids of deleted photos.
                photoUidsToDeleteFromStorage = removeDeletedPhotosFromList(offer)

                // Update existing offer
                updateOfferList(offerList, offer.uid) { offerIndex -> offerList[offerIndex] = offer }

            } else {
                // If offer uid is empty, then offer does not exist.
                // Just generate uid and add new offer with it.
                offer.uid = UUID.randomUUID().toString()
                offerList.add(offer)
            }

            saveOfferList(
                offerList,
                {
                    // If offer list successfully updated in Firestore,
                    // remove deleted photos from Cloud Storage.
                    removeOfferPhotosFromStorage(photoUidsToDeleteFromStorage)
                },
                onError
            )

            // Do not wait for save offer to the server success, just call onSuccess()
            onSuccess()

        } else {
            onError()
        }
    }

    override fun deleteOffer(offerUid: String, onSuccess: () -> Unit, onError: () -> Unit) {
        if (isAuthorized && offerUid != "") {
            val offerList = currentUserOfferList()

            var photoUidsToDeleteFromStorage = mutableListOf<String>()

            updateOfferList(offerList, offerUid) { offerIndex ->
                // Save offer photo uids
                photoUidsToDeleteFromStorage = offerList[offerIndex].photoList.map { it.uid }.toMutableList()
                // Remove offer from list
                offerList.removeAt(offerIndex)
            }

            saveOfferList(
                offerList,
                {
                    // If offer list successfully updated in Firestore,
                    // remove photos of the deleted offer from Cloud Storage.
                    removeOfferPhotosFromStorage(photoUidsToDeleteFromStorage)
                },
                onError
            )

            // Do not wait for delete offer from the server success, just call onSuccess()
            onSuccess()

        } else {
            onError()
        }
    }

    override fun addOfferPhoto(selectedImageUri: Uri, onSuccess: (Photo) -> Unit, onError: () -> Unit) {
        addImage(
            selectedImageUri,
            false,
            { photoUid, downloadUrl -> onSuccess(Photo(photoUid, downloadUrl)) },
            onError
        )
    }

    override fun deleteOfferPhotoFromStorage(photoUid: String) = deleteImage(photoUid, false)

    override fun cancelPhotoUploadTasks() {
        uploadTasks.forEach { it.cancel() }
        uploadTasks.clear()
    }

    // --- Favorites ---

    override fun favoriteUsers() = favoriteUsers

    override fun favoriteOffers() = favoriteOffers

    override fun addFavorite(userUid: String, offerUid: String, onError: () -> Unit) {
        if (isAuthorized && userUid != "" && offerUid != "" && currentUserUid() != "") {
            val data = HashMap<String, Any>()
            data[FAVORITE_USER_UID_KEY] = userUid
            data[FAVORITE_OFFER_UID_KEY] = offerUid

            getFavoritesCollectionReference()
                .document("$userUid$offerUid")
                .set(data, SetOptions.merge())
                .addOnSuccessListener {
                    Timber.tag(TAG).d("Favorite successfully added")
                }
                .addOnFailureListener { error ->
                    Timber.tag(TAG).d("Error adding favorite")
                    onError()
                }

        } else {
            onError()
        }
    }

    override fun removeFavorite(userUid: String, offerUid: String, onError: () -> Unit) {
        if (isAuthorized && userUid != "" && offerUid != "" && currentUserUid() != "") {
            getFavoritesCollectionReference()
                .document("$userUid$offerUid")
                .delete()
                .addOnSuccessListener {
                    Timber.tag(TAG).d("Favorite successfully removed")
                }
                .addOnFailureListener {
                    Timber.tag(TAG).d("Error removing favorite")
                    onError()
                }

        } else {
            onError()
        }
    }

    // 1. Load all users that are in favorites list
    // (both for favorite users and offers, but without duplicates)
    // 2. From the loaded user list select those that are favorite - into favorite users list
    // 3. From the loaded user list select favorite offers - into favorite offers list
    override fun loadFavorites(onComplete: () -> Unit) {
        loadedUsersList.clear()
        loadedUsersCounter = 0

        // Get user uids to load without duplicates
        val userUidsToLoad = getUserUidsToLoad(favorites)
        val userUidsToLoadSize = userUidsToLoad.size

        // Filter favorites that are users
        val favoriteUsersList = favorites.filter { !it.isOffer() }.toMutableList()

        // Filter favorites that are offers
        val favoriteOffersList = favorites.filter { it.isOffer() }.toMutableList()

        // Remove those users and offers, that are not favorite any more
        removeNonFavoriteUsers(favoriteUsersList)
        removeNonFavoriteOffers(favoriteOffersList)

        if (userUidsToLoadSize > 0) {
            // Load users for favorite users and offers
            userUidsToLoad.forEach { userUid ->
                loadUser(
                    userUid,
                    { user -> addLoadedUser(user, userUidsToLoadSize, favoriteUsersList, favoriteOffersList, onComplete) },
                    { addLoadedUser(null, userUidsToLoadSize, favoriteUsersList, favoriteOffersList, onComplete) }
                )
            }

        } else {
            onComplete()
        }
    }

    override fun initUserDetailsFromFavorites(uid: String) {
        // Get first update of user details from the loaded users list, which is already available
        val user = loadedUsersList.firstOrNull { it.uid == uid }
        if (user != null) secondUser.value = user
    }

    // --- Reviews ---

    override fun reviews(): MutableLiveData<MutableList<Review>> = reviews

    override fun startGettingReviewsUpdates(offerUid: String, isCurrentUser: Boolean) {
        val userUid = if (isCurrentUser) currentUserUid() else secondUserUid()

        if (isAuthorized && offerUid != "" && userUid != "") {
            reviewsListenerRegistration = getReviewsCollectionReference(userUid, offerUid)
                .orderBy(REVIEW_TIMESTAMP_KEY, Query.Direction.DESCENDING)
                .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                    if (firebaseFirestoreException == null) {
                        Timber.tag(TAG).d("Reviews listen success")

                        val reviewsList = mutableListOf<Review>()

                        if (querySnapshot != null) {
                            for (doc in querySnapshot.documents) {
                                val review = getReviewFromDocumentSnapshot(doc)
                                reviewsList.add(review)
                            }

                        } else {
                            Timber.tag(TAG).d("Reviews listen failed")
                        }

                        reviews.value = reviewsList

                    } else {
                        Timber.tag(TAG).d(firebaseFirestoreException)
                    }
                }
        }
    }

    override fun stopGettingReviewsUpdates() = reviewsListenerRegistration?.remove() ?: Unit

    override fun saveReview(reviewUid: String, offerUid: String, text: String, rating: Float, onSuccess: () -> Unit, onError: () -> Unit) {
        val data = HashMap<String, Any>()

        data[REVIEW_PROVIDER_USER_UID_KEY] = secondUserUid()
        data[REVIEW_OFFER_UID_KEY] = offerUid
        data[REVIEW_AUTHOR_UID_KEY] = currentUserUid()
        data[REVIEW_AUTHOR_NAME_KEY] = currentUserNameOrUsername()
        data[REVIEW_AUTHOR_USER_PIC_KEY] = currentUserPicUrl()
        data[REVIEW_TEXT_KEY] = text
        data[REVIEW_RATING_KEY] = rating
        data[REVIEW_TIMESTAMP_KEY] = FieldValue.serverTimestamp()

        val reviewUidToSave = if (reviewUid != "") reviewUid else UUID.randomUUID().toString()

        saveReviewData(secondUserUid(), offerUid, reviewUidToSave, data, onSuccess, onError)
    }

    override fun clearReviews() {
        reviews.value = mutableListOf()
    }

    override fun deleteReview(offerUid: String, reviewUid: String, onSuccess: () -> Unit, onError: () -> Unit) {
        if (isAuthorized && offerUid != "" && reviewUid != "" && secondUserUid() != "") {
            getReviewsCollectionReference(secondUserUid(), offerUid)
                .document(reviewUid)
                .delete()
                .addOnSuccessListener {
                    Timber.tag(TAG).d("Review successfully deleted")
                    onSuccess()
                }
                .addOnFailureListener {
                    Timber.tag(TAG).d("Error deleting review")
                    onError()
                }

        } else {
            onError()
        }
    }

    override fun saveComment(reviewUid: String, offerUid: String, commentText: String, onSuccess: () -> Unit, onError: () -> Unit) {
        val data = HashMap<String, Any>()
        data[REVIEW_COMMENT_KEY] = commentText

        saveReviewData(currentUserUid(), offerUid, reviewUid, data, onSuccess, onError)
    }

    // === Private methods ===
    // --- User ---

    private fun resetCurrentUser() {
        currentUser.value = createAnonymousUser()
    }

    private fun resetSecondUser() {
        secondUser.value = createAnonymousUser()
    }

    private fun createAnonymousUser(): User {
        return User(
            uid = "",
            name = Constants.Auth.DEFAULT_USER_NAME,
            username = "",
            email = Constants.Auth.DEFAULT_USER_MAIL,
            userPicUrl = "",
            description = "",
            isOnline = false,
            location = Constants.Map.DEFAULT_LOCATION
        )
    }

    private fun secondUserUid() = secondUser.value?.uid ?: ""

    private fun saveUserNameEmailPicAndToken(user: User) {
        // First, get current FCM token
        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener { task ->
                // When getting FCM token task is complete,
                // save it and user's name and email into Firestore
                var token = ""
                if (task.isSuccessful) token = task.result?.token ?: ""

                val data = HashMap<String, Any>()
                data[NAME_KEY] = user.name
                data[EMAIL_KEY] = user.email
                data[FCM_TOKEN_KEY] = token
                data[CREATION_TIMESTAMP_KEY] = user.creationTimestamp

                // If user from Firebase Auth has user pic
                if (user.userPicUrl != "") {
                    // Load existing user data from Firestore to see if it already has user pic set,
                    // and update it with user pic from Auth if not.
                    loadUser(
                        currentUserUid(),

                        // On load success, update data with user pic URL if needed
                        { existingUser -> saveUserDataWithUserPicIfNeeded(data, user.userPicUrl, existingUser) },

                        // On load error, just update name, email and token
                        { saveUserDataWithoutUserPic(data) }
                    )

                } else {
                    saveUserDataWithoutUserPic(data)
                }
            }
    }

    private fun loadUser(userUid: String, onSuccess: (User) -> Unit, onError: () -> Unit) {
        firestore.collection(USERS_COLLECTION).document(userUid).get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val document = task.result

                    if (document != null && document.exists()) {
                        onSuccess(getUserFromDocumentSnapshot(document))

                    } else {
                        onError()
                    }

                } else {
                    onError()
                }
            }
    }

    private fun saveUserDataWithUserPicIfNeeded(data: HashMap<String, Any>, userPicUrl: String, existingUser: User) {
        // If existing user data has no user pic,
        // update it with user pic from Firebase Auth
        if (existingUser.userPicUrl == "") data[USER_PIC_URL_KEY] = userPicUrl

        saveUserDataRemote(data, { updateUserNameAndPicCollection() }, { /* Do nothing */ })
    }

    private fun saveUserDataWithoutUserPic(data: HashMap<String, Any>) =
        saveUserDataRemote(data, { updateUserNameAndPicCollection() }, { /* Do nothing */ })

    private fun saveUserDataRemote(data: HashMap<String, Any>, onSuccess: () -> Unit, onError: () -> Unit) {
        if (isAuthorized && currentUserUid() != "") {
            firestore.collection(USERS_COLLECTION).document(currentUserUid())
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
        stopGettingCurrentUserUpdates()
        currentUserListenerRegistration = startGettingUserUpdates(currentUserUid()) { user ->
            // If current user has active offer, start sharing location,
            // otherwise stop sharing.
            LocationManager.shareLocation(user.hasActiveOffer())

            currentUser.value = user
        }
    }

    private fun stopGettingCurrentUserUpdates() = currentUserListenerRegistration?.remove()

    private fun startGettingUserUpdates(uid: String, onSuccess: (User) -> Unit): ListenerRegistration? {
        var listenerRegistration: ListenerRegistration? = null

        if (isAuthorized && uid != "") {
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

    private fun startSecondUserUpdates(uid: String) = startGettingUserUpdates(uid) { user -> updateSecondUser(user) }

    private fun updateSecondUser(user: User) {
        secondUser.value = user
    }

    private fun removeListener(listenerRegistration: ListenerRegistration?) = listenerRegistration?.remove() ?: Unit

    private fun getUserFromDocumentSnapshot(doc: DocumentSnapshot) = getUserFromDocumentSnapshot(doc, null)

    private fun getUserFromDocumentSnapshot(doc: DocumentSnapshot, geoPoint: GeoPoint?): User {
        val location = if (geoPoint != null) {
            getUserLocationFromGeoPoint(geoPoint)
        } else {
            getUserLocationFromDocumentSnapshot(doc)
        }

        val user = User(
            uid = doc.id,
            name = doc.getString(NAME_KEY) ?: Constants.Auth.DEFAULT_USER_NAME,
            username = doc.getString(USERNAME_KEY) ?: "",
            email = doc.getString(EMAIL_KEY) ?: Constants.Auth.DEFAULT_USER_MAIL,
            userPicUrl = doc.getString(USER_PIC_URL_KEY) ?: "",
            description = doc.getString(DESCRIPTION_KEY) ?: "",
            isOnline = doc.getBoolean(IS_ONLINE_KEY) ?: false,
            location = location,
            isFavorite = isFavorite(doc.id, ""),
            creationTimestamp = doc.getLong(CREATION_TIMESTAMP_KEY) ?: 0,
            firstOfferPublishedTimestamp = (doc.getTimestamp(FIRST_OFFER_PUBLISHED_TIMESTAMP_KEY)?.seconds ?: 0) * 1000,
            phone = doc.getString(PHONE_KEY) ?: "",
            visibleEmail = doc.getString(VISIBLE_EMAIL_KEY) ?: "",
            skype = doc.getString(SKYPE_KEY) ?: ""
        )

        user.offerList = getOfferListFromDocumentSnapshot(doc.id, doc)
        user.photoList = getPhotoListFromDocumentSnapshot(doc)

        // Offer ratings are stored in a separate list in user, not inside offer list.
        // This is needed to prevent overwriting offer ratings when updating offers.
        // Here we copy ratings from offer rating list to the corresponding offers
        // for convenient use.
        updateOfferRatings(user, doc)

        return user
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

    private fun getOfferListFromDocumentSnapshot(userUid: String, doc: DocumentSnapshot): MutableList<Offer> {
        val offerSnapshotList = doc.get(OFFER_LIST_KEY) as List<*>?

        val offerList = mutableListOf<Offer>()

        if (offerSnapshotList != null) {
            for (offerSnapshot in offerSnapshotList) {
                val offerMap = offerSnapshot as HashMap<*, *>

                val offerUid = offerMap[OFFER_UID_KEY] as String?
                val offerTitle = offerMap[OFFER_TITLE_KEY] as String?
                val offerDescription = offerMap[OFFER_DESCRIPTION_KEY] as String?
                val offerActive = offerMap[OFFER_ACTIVE_KEY] as Boolean?
                val offerFree = offerMap[OFFER_FREE_KEY] as Boolean?
                val offerPrice = longOrDoubleToDouble(offerMap[OFFER_PRICE_KEY])

                if (
                    offerUid != null
                    && offerUid != ""
                    && offerTitle != null
                    && offerTitle != ""
                    && offerDescription != null
                    && offerDescription != ""
                    && offerActive != null
                    && offerFree != null
                ) {
                    val offer = Offer(
                        offerUid,
                        userUid,
                        offerTitle,
                        offerDescription,
                        offerPrice,
                        offerFree,
                        offerActive,
                        isFavorite(userUid, offerUid)
                    )

                    val offerPhotoList = getPhotoListFromPhotoSnapshotList(offerMap[OFFER_PHOTO_LIST_KEY] as List<*>?)
                    offer.photoList = offerPhotoList

                    offerList.add(offer)
                }
            }
        }

        return offerList
    }

    private fun getPhotoListFromDocumentSnapshot(doc: DocumentSnapshot): MutableList<Photo> =
        getPhotoListFromPhotoSnapshotList(doc.get(PHOTO_LIST_KEY) as List<*>?)

    private fun getPhotoListFromPhotoSnapshotList(photoSnapshotList: List<*>?): MutableList<Photo> {
        val photoList = mutableListOf<Photo>()

        if (photoSnapshotList != null) {
            for (photoSnapshot in photoSnapshotList) {
                val photoMap = photoSnapshot as HashMap<*, *>

                val photoUid = photoMap[PHOTO_UID_KEY] as String?
                val photoDownloadUrl = photoMap[PHOTO_DOWNLOAD_URL_KEY] as String?

                if (
                    photoUid != null
                    && photoUid != ""
                    && photoDownloadUrl != null
                    && photoDownloadUrl != ""
                ) {
                    val photo = Photo(photoUid, photoDownloadUrl)
                    photoList.add(photo)
                }
            }
        }

        return photoList
    }

    private fun updateOfferRatings(user: User, doc: DocumentSnapshot) {
        val offerRatingList = getOfferRatingListFromDocumentSnapshot(doc)
        val offerList = user.offerList

        offerRatingList.forEach { rating ->
            val offer = offerList.firstOrNull { offerItem -> offerItem.uid == rating.offerUid }

            if (offer != null) {
                offer.rating = rating.rating
                offer.reviewCount = rating.reviewCount
                offer.lastReviewAuthorName = rating.lastReviewAuthorName
                offer.lastReviewAuthorUserPicUrl = rating.lastReviewAuthorUserPicUrl
                offer.lastReviewText = rating.lastReviewText
                offer.lastReviewTimestamp = rating.lastReviewTimestamp
            }
        }
    }

    private fun getOfferRatingListFromDocumentSnapshot(doc: DocumentSnapshot): MutableList<Rating> {
        val offerRatingSnapshotList = doc.get(OFFER_RATING_LIST_KEY) as List<*>?

        val offerRatingList = mutableListOf<Rating>()

        if (offerRatingSnapshotList != null) {
            for (offerRatingSnapshot in offerRatingSnapshotList) {
                val offerRatingMap = offerRatingSnapshot as HashMap<*, *>

                val offerUid = offerRatingMap[OFFER_UID_KEY] as String?
                val offerRating = longOrDoubleToFloat(offerRatingMap[OFFER_RATING_KEY])
                val offerReviewCount = (offerRatingMap[OFFER_REVIEW_COUNT_KEY] as Long? ?: 0).toInt()
                val offerLastReviewAuthorName = offerRatingMap[OFFER_LAST_REVIEW_AUTHOR_NAME_KEY] as String? ?: ""
                val offerLastReviewAuthorUserPicUrl = offerRatingMap[OFFER_LAST_REVIEW_AUTHOR_PIC_URL_KEY] as String? ?: ""
                val offerLastReviewText = offerRatingMap[OFFER_LAST_REVIEW_TEXT_KEY] as String? ?: ""
                val offerLastReviewTimestamp = (offerRatingMap[OFFER_LAST_REVIEW_TIME_KEY] as Date?)?.time ?: (System.currentTimeMillis() / 1000)

                if (offerUid != null && offerUid != "") {
                    val rating = Rating(
                        offerUid,
                        offerRating,
                        offerReviewCount,
                        offerLastReviewAuthorName,
                        offerLastReviewAuthorUserPicUrl,
                        offerLastReviewText,
                        offerLastReviewTimestamp
                    )

                    offerRatingList.add(rating)
                }
            }
        }

        return offerRatingList
    }

    private fun currentUserNameOrUsername() = currentUser.value?.getUsernameOrName() ?: ""

    private fun currentUserPhotoList() = currentUser.value?.photoList ?: mutableListOf()

    private fun currentUserPicUrl() = currentUser.value?.userPicUrl ?: ""

    private fun saveUserPicUrl(newUserPicUrl: String) {
        val data = HashMap<String, Any>()
        data[USER_PIC_URL_KEY] = newUserPicUrl

        saveUserDataRemote(data, { updateUserNameAndPicCollection() }, { /* Do nothing */ })
    }

    private fun saveUserPhotoUrl(photoUid: String, photoDownloadUrl: String) {
        val photoList = mutableListOf<Photo>()
        photoList.addAll(currentUserPhotoList())

        val photo = Photo(photoUid, photoDownloadUrl)
        photoList.add(photo)

        saveUserPhotoList(photoList, { /* Do nothing */ }, { /* Do nothing */ })
    }

    private fun saveUserPhotoList(photoList: MutableList<Photo>, onSuccess: () -> Unit, onError: () -> Unit) {
        val data = HashMap<String, Any>()
        data[PHOTO_LIST_KEY] = getPhotoListForSaving(photoList)

        saveUserDataRemote(data, onSuccess, onError)
    }

    // --- Search ---

    private fun clearResult() {
        clearTempResult()
        updateSearchResult()
    }

    private fun clearTempResult() = tempSearchResult.clear()

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

    private fun checkConditions(user: User): Boolean = user.hasActiveOffer() && checkQueryText(user)

    private fun checkQueryText(user: User): Boolean {
        return user.name.contains(queryText, true)
                || user.username.contains(queryText, true)
                || offersContainQueryText(user)
    }

    private fun offersContainQueryText(user: User): Boolean {
        var result = false
        val offerList = user.offerList

        if (offerList.size > 0) {
            for (i in offerList.indices) {
                val offer = offerList[i]
                if (offer.isActive && (offer.title.contains(queryText, true) || offer.description.contains(queryText, true))) {
                    result = true
                    user.offerSearchResultIndex = i
                    break
                }
            }
        }

        return result
    }

    private fun removeUserFromSearchResults(uid: String?) {
        if (uid != null) {
            tempSearchResult.remove(uid)
            updateSearchResult()
        }
    }

    private fun updateSearchResult() {
        val tempResult = mutableMapOf<String, User>()
        tempResult.putAll(tempSearchResult)
        searchResult.value = tempResult
    }

    // --- Message ---

    private fun getMessageFromDocumentSnapshot(doc: DocumentSnapshot): Message {
        val senderUid = doc.getString(SENDER_UID_KEY) ?: ""

        return Message(
            uid = doc.id,
            timestamp = getTimestampFromDocumentSnapshot(doc, TIMESTAMP_KEY),
            text = doc.getString(MESSAGE_TEXT_KEY) ?: "",
            isFromCurrentUser = senderUid == currentUserUid(),
            isRead = doc.getBoolean(MESSAGE_IS_READ_KEY) ?: false
        )
    }

    private fun getTimestampFromDocumentSnapshot(doc: DocumentSnapshot, timestampKey: String) =
        doc.getTimestamp(timestampKey)?.seconds ?: (System.currentTimeMillis() / 1000)

    private fun getMessagesCollectionReference(): CollectionReference {
        return firestore
            .collection(CHATROOMS_COLLECTION).document(currentChatRoomUid)
            .collection(MESSAGES_COLLECTION)
    }

    private fun markMessageAsRead(messageUid: String) {
        if (isAuthorized && currentChatRoomUid != "" && messageUid != "") {
            val data = HashMap<String, Any>()
            data[MESSAGE_IS_READ_KEY] = true

            getMessagesCollectionReference()
                .document(messageUid)
                .set(data, SetOptions.merge())
                .addOnSuccessListener {
                    Timber.tag(TAG).d("Message mark as read successfully")
                }
                .addOnFailureListener { error ->
                    Timber.tag(TAG).d("Error mark message as read")
                }
        }
    }

    private fun initUnreadMessagesFlag() {
        unreadMessagesFlag.value = settings.isUnreadMessagesExist()
    }

    // --- Chatroom ---

    private fun resetChatrooms() {
        chatrooms.value = mutableListOf()
    }

    private fun getChatroomsCollectionReference(): CollectionReference {
        return firestore
            .collection(USER_CHATROOMS_COLLECTION).document(currentUserUid())
            .collection(CHATROOMS_OF_USER_COLLECTION)
    }

    private fun getChatroomFromDocumentSnapshot(doc: DocumentSnapshot): Chatroom {
        return Chatroom(
            secondUserUid = doc.getString(CHATROOM_SECOND_USER_UID_KEY) ?: "",
            secondUserName = doc.getString(CHATROOM_SECOND_USER_NAME_KEY) ?: "",
            secondUserPicUrl = doc.getString(CHATROOM_SECOND_USER_PIC_URL_KEY) ?: "",
            lastMessageText = doc.getString(CHATROOM_LAST_MESSAGE_TEXT_KEY) ?: "",
            lastMessageTimestamp = getTimestampFromDocumentSnapshot(doc, CHATROOM_LAST_MESSAGE_TIMESTAMP_KEY),
            newMessageCount = doc.getLong(CHATROOM_NEW_MESSAGE_COUNT_KEY) ?: 0
        )
    }

    // Calling this will write data to a special collection,
    // which in turn will trigger Cloud Function that updates chatrooms
    // (this is needed to update chatrooms on username and userpic change).
    private fun updateUserNameAndPicCollection() {
        if (isAuthorized && currentUserUid() != "") {
            val data = HashMap<String, Any>()
            data[NAME_KEY] = currentUser.value?.name ?: ""
            data[USERNAME_KEY] = currentUser.value?.username ?: ""
            data[USER_PIC_URL_KEY] = currentUserPicUrl()

            firestore.collection(USER_NAME_AND_PIC_COLLECTION).document(currentUserUid())
                .set(data, SetOptions.merge())
                .addOnSuccessListener {
                    Timber.tag(TAG).d("userNameAndPic successfully written")
                }
                .addOnFailureListener { error ->
                    Timber.tag(TAG).d("Error writing userNameAndPic")
                }
        }
    }

    // --- Photo ---

    private fun getPhotoStoragePath(photoUid: String, userPhoto: Boolean): String {
        val subfolderName = if (userPhoto) "user_photos" else "offer_photos"
        return "${currentUserUid()}/$subfolderName/$photoUid.jpg"
    }

    private fun addImage(selectedImageUri: Uri, userPhoto: Boolean, onSuccess: (String, String) -> Unit, onError: () -> Unit) {
        val photoUid = UUID.randomUUID().toString()
        val storagePath = getPhotoStoragePath(photoUid, userPhoto)

        uploadImage(
            selectedImageUri,
            storagePath,
            if (userPhoto) Constants.Image.USER_PHOTO_SIZE else Constants.Image.OFFER_PHOTO_SIZE,
            false,
            !userPhoto,
            { downloadUrl -> onSuccess(photoUid, downloadUrl) },
            onError
        )
    }

    private fun uploadImage(selectedImageUri: Uri, storagePath: String, downsampleSize: Int, centerCrop: Boolean, offerPhoto: Boolean, onSuccess: (String) -> Unit, onError: () -> Unit) {
        if (isAuthorized) {
            // This is because resizeImage() must run in background
            GlobalScope.launch {
                val storageRef = storage.reference.child(storagePath)

                // Resize selected image
                val byteArray = resizeImage(selectedImageUri, downsampleSize, centerCrop)

                // Upload resized image to Cloud Storage
                if (byteArray != null) {
                    val uploadTask = storageRef.putBytes(byteArray)

                    if (offerPhoto) uploadTasks.add(uploadTask)

                    uploadTask
                        .addOnFailureListener {
                            uploadTasks.remove(uploadTask)
                            onError()
                        }
                        .addOnSuccessListener {
                            uploadTasks.remove(uploadTask)
                            getDownloadUrl(storageRef, { downloadUrl -> onSuccess(downloadUrl) }, onError)
                        }

                } else {
                    onError()
                }
            }

        } else {
            onError()
        }
    }

    // Resize selected image to take less space and traffic
    private fun resizeImage(selectedImageUri: Uri, size: Int, centerCrop: Boolean): ByteArray? {
        try {
            val requestOptions = RequestOptions().override(size).skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE)
            if (centerCrop) requestOptions.centerCrop()

            // Resize image.
            // This must run in background!
            val bitmap = Glide.with(context)
                .asBitmap()
                .load(selectedImageUri)
                .apply(requestOptions)
                .submit().get()

            // Compress into JPEG
            val outStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, Constants.Image.JPEG_QUALITY, outStream)

            return outStream.toByteArray()

        } catch (e: Exception) {
            return null
        }
    }

    private fun deleteImage(photoUid: String, userPhoto: Boolean) {
        val storagePath = getPhotoStoragePath(photoUid, userPhoto)
        val storageRef = storage.reference.child(storagePath)

        storageRef.delete()
            .addOnSuccessListener { /* Do nothing */ }
            .addOnFailureListener { /* Do nothing */ }
    }

    private fun getPhotoListForSaving(photoList: MutableList<Photo>): MutableList<HashMap<String, Any>> {
        val photoListForSaving = mutableListOf<HashMap<String, Any>>()

        for (photoItem in photoList) {
            val photoForSaving = HashMap<String, Any>()
            photoForSaving[PHOTO_UID_KEY] = photoItem.uid
            photoForSaving[PHOTO_DOWNLOAD_URL_KEY] = photoItem.downloadUrl

            photoListForSaving.add(photoForSaving)
        }

        return photoListForSaving
    }

    // --- File download URL ---

    private fun getDownloadUrl(storageRef: StorageReference, onSuccess: (String) -> Unit, onError: () -> Unit) {
        // After the file has been uploaded, we can get its download URL
        storageRef.downloadUrl
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val downloadUri = task.result
                    Timber.tag(TAG).d("Download url = $downloadUri")

                    // Get download URL and pass it into onSuccess()
                    onSuccess(downloadUri.toString())

                } else {
                    onError()
                }
            }
    }

    // --- Offer ---

    private fun updateOfferList(offerList: MutableList<Offer>, offerUid: String, update: (Int) -> Unit) {
        val offerIndex = offerList.indexOfFirst { it.uid == offerUid }

        if (offerIndex >= 0 && offerIndex < offerList.size) {
            update(offerIndex)
        }
    }

    private fun saveOfferList(offerList: MutableList<Offer>, onSuccess: () -> Unit, onError: () -> Unit) {
        val offerListForSaving = mutableListOf<HashMap<String, Any>>()

        for (offerItem in offerList) {
            val offerForSaving = HashMap<String, Any>()
            offerForSaving[OFFER_UID_KEY] = offerItem.uid
            offerForSaving[OFFER_TITLE_KEY] = offerItem.title
            offerForSaving[OFFER_DESCRIPTION_KEY] = offerItem.description
            offerForSaving[OFFER_ACTIVE_KEY] = offerItem.isActive
            offerForSaving[OFFER_FREE_KEY] = offerItem.isFree
            offerForSaving[OFFER_PRICE_KEY] = offerItem.price
            offerForSaving[OFFER_PHOTO_LIST_KEY] = getPhotoListForSaving(offerItem.photoList)

            offerListForSaving.add(offerForSaving)
        }

        val data = HashMap<String, Any>()
        data[OFFER_LIST_KEY] = offerListForSaving

        // If current user has no first offer published timestamp yet, then init it with server timestamp
        val firstOfferPublishedTimestamp = currentUser.value?.firstOfferPublishedTimestamp ?: 0
        if (firstOfferPublishedTimestamp == 0L) {
            data[FIRST_OFFER_PUBLISHED_TIMESTAMP_KEY] = FieldValue.serverTimestamp()
        }

        saveUserDataRemote(data, onSuccess, onError)
    }

    // Remove deleted photos from photo list of the offer,
    // return deleted photo uids list.
    private fun removeDeletedPhotosFromList(offer: Offer): MutableList<String> {
        val deletedPhotoUids = mutableListOf<String>()

        // We use iterator, because here we change list while iterating through it
        val iterator = offer.photoList.listIterator()
        while (iterator.hasNext()) {
            val photo = iterator.next()
            if (photo.isDeleted) {
                deletedPhotoUids.add(photo.uid)
                iterator.remove()
            }
        }

        return deletedPhotoUids
    }

    // Remove offer photos from Cloud Storage
    private fun removeOfferPhotosFromStorage(photoUidsToDeleteFromStorage: MutableList<String>) {
        for (photoUid in photoUidsToDeleteFromStorage) {
            deleteImage(photoUid, false)
        }
    }

    // --- Favorites ---

    private fun startGettingFavoritesUpdates() {
        stopGettingFavoritesUpdates()

        if (isAuthorized && currentUserUid() != "") {
            favoritesListenerRegistration = getFavoritesCollectionReference()
                .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                    if (firebaseFirestoreException == null) {
                        Timber.tag(TAG).d("Listen success")

                        val favoritesList = mutableListOf<Favorite>()

                        if (querySnapshot != null) {
                            for (doc in querySnapshot.documents) {
                                val favorite = getFavoriteFromDocumentSnapshot(doc)
                                favoritesList.add(favorite)
                            }

                        } else {
                            Timber.tag(TAG).d("Listen failed")
                        }

                        favorites.clear()
                        favorites.addAll(favoritesList)

                        loadFavorites { /* Do nothing */ }
                        reloadSecondUser()

                    } else {
                        Timber.tag(TAG).d(firebaseFirestoreException)
                    }
                }
        }
    }

    private fun stopGettingFavoritesUpdates() = favoritesListenerRegistration?.remove() ?: Unit

    private fun getFavoritesCollectionReference(): CollectionReference {
        return firestore
            .collection(USER_FAVORITES_COLLECTION).document(currentUserUid())
            .collection(FAVORITES_OF_USER_COLLECTION)
    }

    private fun getFavoriteFromDocumentSnapshot(doc: DocumentSnapshot): Favorite {
        return Favorite(
            userUid = doc.getString(FAVORITE_USER_UID_KEY) ?: "",
            offerUid = doc.getString(FAVORITE_OFFER_UID_KEY) ?: ""
        )
    }

    private fun isFavorite(userUid: String, offerUid: String): Boolean {
        val favoriteIndex = favorites.indexOfFirst { it.userUid == userUid && it.offerUid == offerUid }
        return favoriteIndex != -1
    }

    // On favorites list change, reload second user
    // (this is needed to update favorite status of the second user and its offers).
    private fun reloadSecondUser() {
        if (shouldReloadSecondUser()) {
            loadUser(
                secondUserUid(),
                { user -> updateSecondUser(user) },
                { /* Do nothing */ }
            )
        }
    }

    private fun shouldReloadSecondUser() = secondUserUid() != "" && (isUserDetailsActive || isOfferDetailsActive)

    private fun getUserUidsToLoad(favoriteList: MutableList<Favorite>): MutableList<String> {
        // This is needed to remove duplicates
        // (because favorites may contain equal userUids for different favorite offers)
        val userUidsToLoad = mutableSetOf<String>()
        favoriteList.forEach { userUidsToLoad.add(it.userUid) }
        return userUidsToLoad.toMutableList()
    }

    private fun removeNonFavoriteUsers(favoriteUsersList: MutableList<Favorite>) {
        val currentFavoriteUsers = mutableListOf<User>()
        currentFavoriteUsers.addAll(favoriteUsers.value ?: mutableListOf())

        val iterator = currentFavoriteUsers.listIterator()
        while (iterator.hasNext()) {
            val user = iterator.next()
            val contains = favoriteUsersList.firstOrNull { it.userUid == user.uid } != null

            if (!contains) {
                iterator.remove()
            }
        }

        favoriteUsers.value = currentFavoriteUsers
    }

    private fun removeNonFavoriteOffers(favoriteOffersList: MutableList<Favorite>) {
        val currentFavoriteOffers = mutableListOf<Offer>()
        currentFavoriteOffers.addAll(favoriteOffers.value ?: mutableListOf())

        val iterator = currentFavoriteOffers.listIterator()
        while (iterator.hasNext()) {
            val offer = iterator.next()
            val contains = favoriteOffersList.firstOrNull { it.userUid == offer.userUid && it.offerUid == offer.uid } != null

            if (!contains) {
                iterator.remove()
            }
        }

        favoriteOffers.value = currentFavoriteOffers
    }

    private fun addLoadedUser(user: User?, maxUserCount: Int, favoriteUsersList: MutableList<Favorite>, favoriteOffersList: MutableList<Favorite>, onComplete: () -> Unit) {
        loadedUsersCounter++

        if (user != null) loadedUsersList.add(user)

        if (loadedUsersCounter >= maxUserCount) {
            onFavoritesLoadComplete(favoriteUsersList, favoriteOffersList)
            onComplete()
        }
    }

    private fun onFavoritesLoadComplete(favoriteUsersList: MutableList<Favorite>, favoriteOffersList: MutableList<Favorite>) {
        favoriteUsers.value = getFavoriteUsersFromLoaded(favoriteUsersList)
        favoriteOffers.value = getFavoriteOffersFromLoaded(favoriteOffersList)
    }

    private fun getFavoriteUsersFromLoaded(favoriteUsersList: MutableList<Favorite>): MutableList<User> {
        val tempFavoriteUsers = mutableListOf<User>()

        favoriteUsersList.forEach { favorite ->
            val user = loadedUsersList.firstOrNull { it.uid == favorite.userUid }
            if (user != null) tempFavoriteUsers.add(user)
        }

        tempFavoriteUsers.sortBy { it.getUsernameOrName() }

        return tempFavoriteUsers
    }

    private fun getFavoriteOffersFromLoaded(favoriteOffersList: MutableList<Favorite>): MutableList<Offer> {
        val tempFavoriteOffers = mutableListOf<Offer>()

        favoriteOffersList.forEach { favorite ->
            val user = loadedUsersList.firstOrNull { it.uid == favorite.userUid }
            val offer = user?.getOffer(favorite.offerUid)

            if (offer != null && offer.isActive) tempFavoriteOffers.add(offer)
        }

        tempFavoriteOffers.sortBy { it.title }

        return tempFavoriteOffers
    }

    // --- Reviews ---

    private fun getReviewsCollectionReference(providerUserUid: String, offerUid: String): CollectionReference {
        return firestore
            .collection(REVIEWS_COLLECTION).document("${providerUserUid}_$offerUid")
            .collection(REVIEWS_OF_OFFER_COLLECTION)
    }

    private fun getReviewFromDocumentSnapshot(doc: DocumentSnapshot): Review {
        val authorUid = doc.getString(REVIEW_AUTHOR_UID_KEY) ?: ""

        return Review(
            uid = doc.id,
            providerUserUid = doc.getString(REVIEW_PROVIDER_USER_UID_KEY) ?: "",
            offerUid = doc.getString(REVIEW_OFFER_UID_KEY) ?: "",
            authorUid = authorUid,
            authorName = doc.getString(REVIEW_AUTHOR_NAME_KEY) ?: "",
            authorUserPicUrl = doc.getString(REVIEW_AUTHOR_USER_PIC_KEY) ?: "",
            text = doc.getString(REVIEW_TEXT_KEY) ?: "",
            rating = longOrDoubleToFloat(doc.getDouble(REVIEW_RATING_KEY)),
            timestamp = getTimestampFromDocumentSnapshot(doc, REVIEW_TIMESTAMP_KEY),
            isFromCurrentUser = authorUid == currentUserUid(),
            comment = doc.getString(REVIEW_COMMENT_KEY) ?: ""
        )
    }

    private fun longOrDoubleToFloat(number: Any?): Float {
        var result = 0.0F

        if (number != null) {
            if (number is Long) {
                result = number.toFloat()
            } else if (number is Double) {
                result = number.toFloat()
            }
        }

        return result
    }

    private fun longOrDoubleToDouble(number: Any?): Double {
        var result = 0.0

        if (number != null) {
            if (number is Long) {
                result = number.toDouble()
            } else if (number is Double) {
                result = number
            }
        }

        return result
    }

    private fun saveReviewData(userUid: String, offerUid: String, reviewUid: String, data: HashMap<String, Any>, onSuccess: () -> Unit, onError: () -> Unit) {
        if (isAuthorized && reviewUid != "" && offerUid != "" && userUid != "") {
            // Write review to Firestore
            getReviewsCollectionReference(userUid, offerUid)
                .document(reviewUid)
                .set(data, SetOptions.merge())  // this is needed to update only the required data if the user exists
                .addOnSuccessListener {
                    // This is called only in ONLINE
                    Timber.tag(TAG).d("Review successfully saved")
                }
                .addOnFailureListener { error ->
                    // This is called only in ONLINE
                    Timber.tag(TAG).d("Error saving review")
                    onError()
                }

            // Do not wait for save review to the server success, just call onSuccess()
            onSuccess()

        } else {
            onError()
        }
    }
}