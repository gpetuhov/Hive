package com.gpetuhov.android.hive.repository

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.SetOptions
import com.gpetuhov.android.hive.domain.model.User
import com.gpetuhov.android.hive.util.Constants
import timber.log.Timber
import java.util.*
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.DocumentSnapshot
import com.gpetuhov.android.hive.application.HiveApp
import com.gpetuhov.android.hive.domain.repository.Repo
import com.gpetuhov.android.hive.managers.LocationManager
import javax.inject.Inject

// Read and write data to remote storage (Firestore)
class Repository : Repo {

    companion object {
        private const val TAG = "Repo"
        private const val USERS_COLLECTION = "users"
        private const val NAME_KEY = "name"
        private const val USERNAME_KEY = "username"
        private const val EMAIL_KEY = "email"
        private const val SERVICE_KEY = "service"
        private const val IS_VISIBLE_KEY = "is_visible"
        private const val IS_ONLINE_KEY = "is_online"
        private const val LAT_KEY = "lat"
        private const val LON_KEY = "lon"
    }

    @Inject lateinit var context: Context

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

    // For resultList the single source of truth is also Firestore.
    // Sequence of updates:
    // 1. Data in Firestore is updated
    // 2. resultList is updated
    // 3. UI the observes resultList is updated
    private val resultList = MutableLiveData<MutableList<User>>()

    private var isAuthorized = false
    private var currentUserUid: String = ""
    private val firestore = FirebaseFirestore.getInstance()
    private var searchResultListenerRegistration: ListenerRegistration? = null
    private var currentUserListenerRegistration: ListenerRegistration? = null

    init {
        HiveApp.appComponent.inject(this)

        // Offline data caching is enabled by default in Android.
        // But we enable it explicitly to be sure.
        val settings = FirebaseFirestoreSettings.Builder()
            .setPersistenceEnabled(true)
            .build()

        firestore.firestoreSettings = settings

        resetCurrentUser()
        clearResultList()
    }

    override fun onSignIn(user: User) {
        if (!isAuthorized) {
            isAuthorized = true

            // Current user's uid initially comes from Firebase Auth,
            // so we must save it to start getting updates
            // for the corresponding documents in Firestore.
            currentUserUid = user.uid
            startGettingCurrentUserRemoteUpdates()

            // Current user's name and email initially come from Firebase Auth,
            // so after successful sign in we must write them to Firestore.
            saveUserNameAndEmail(user)
        }
    }

    override fun onSignOut() {
        if (isAuthorized) {
            isAuthorized = false
            stopGettingCurrentUserRemoteUpdates()
            stopGettingRemoteResultUpdates()
            resetCurrentUser()
        }
    }

    override fun currentUser() = currentUser

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

    override fun saveUserVisibility(newIsVisible: Boolean, onError: () -> Unit) {
        val data = HashMap<String, Any>()
        data[IS_VISIBLE_KEY] = newIsVisible

        saveUserDataRemote(data, { /* Do nothing */ }, onError)
    }

    override fun saveUserLocation(newLocation: LatLng) {
        val data = HashMap<String, Any>()
        data[LAT_KEY] = newLocation.latitude
        data[LON_KEY] = newLocation.longitude

        saveUserDataRemote(data, { /* Do nothing */ }, { /* Do nothing */ })
    }

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

    override fun resultList() = resultList

    override fun startGettingRemoteResultUpdates() {
        if (isAuthorized) {
            searchResultListenerRegistration = firestore.collection(USERS_COLLECTION)
                .whereEqualTo(IS_VISIBLE_KEY, true)
                .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                    val newResultList = mutableListOf<User>()

                    if (firebaseFirestoreException == null) {
                        if (querySnapshot != null) {
                            Timber.tag(TAG).d("Listen success")

                            for (doc in querySnapshot) {
                                if (doc.id != currentUser.value?.uid) {
                                    newResultList.add(getUserFromDocumentSnapshot(doc))
                                }
                            }

                        } else {
                            Timber.tag(TAG).d("Listen failed")
                        }

                    } else {
                        Timber.tag(TAG).d(firebaseFirestoreException)
                    }

                    resultList.value = newResultList
                }
        }
    }

    override fun stopGettingRemoteResultUpdates() = searchResultListenerRegistration?.remove() ?: Unit

    // === Private methods ===

    private fun resetCurrentUser() {
        currentUser.value = createAnonymousUser()
        currentUserUid = ""
    }

    private fun clearResultList() {
        resultList.value = mutableListOf()
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
            location = LatLng(Constants.Map.DEFAULT_LATITUDE, Constants.Map.DEFAULT_LONGITUDE)
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

    private fun startGettingCurrentUserRemoteUpdates() {
        if (isAuthorized) {
            currentUserListenerRegistration = firestore.collection(USERS_COLLECTION).document(currentUserUid)
                .addSnapshotListener { snapshot, firebaseFirestoreException ->
                    if (firebaseFirestoreException == null) {
                        if (snapshot != null && snapshot.exists()) {
                            Timber.tag(TAG).d("Listen success")
                            val user = getUserFromDocumentSnapshot(snapshot)

                            // If current user is visible, start sharing location,
                            // otherwise stop sharing.
                            LocationManager.shareLocation(context, user.isVisible)

                            currentUser.value = user

                        } else {
                            Timber.tag(TAG).d("Listen failed")
                        }

                    } else {
                        Timber.tag(TAG).d(firebaseFirestoreException)
                    }
                }
        }
    }

    private fun stopGettingCurrentUserRemoteUpdates() = currentUserListenerRegistration?.remove()

    private fun getUserFromDocumentSnapshot(doc: DocumentSnapshot): User {
        val location = LatLng(
            doc.getDouble(LAT_KEY) ?: Constants.Map.DEFAULT_LATITUDE,
            doc.getDouble(LON_KEY) ?: Constants.Map.DEFAULT_LONGITUDE
        )

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
}