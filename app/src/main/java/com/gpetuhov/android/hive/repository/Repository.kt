package com.gpetuhov.android.hive.repository

import androidx.lifecycle.MutableLiveData
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.SetOptions
import com.gpetuhov.android.hive.application.HiveApp
import com.gpetuhov.android.hive.model.User
import com.gpetuhov.android.hive.util.Constants
import timber.log.Timber
import java.util.*
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.DocumentSnapshot


class Repository {

    companion object {
        private const val TAG = "Repository"
        private const val USERS_COLLECTION = "users"
        private const val NAME_KEY = "name"
        private const val USERNAME_KEY = "username"
        private const val EMAIL_KEY = "email"
        private const val IS_ONLINE_KEY = "is_online"
        private const val LAT_KEY = "lat"
        private const val LON_KEY = "lon"
    }

    // Current user is LiveData so that the UI can easily observe changes and update itself
    val currentUser = MutableLiveData<User>()

    private var isAuthorized = false
    private var currentUserUid: String = ""
    private val firestore = FirebaseFirestore.getInstance()
    private lateinit var searchResultListenerRegistration: ListenerRegistration
    private lateinit var currentUserListenerRegistration: ListenerRegistration

    init {
        HiveApp.appComponent.inject(this)

        // Offline data caching is enabled by default in Android.
        // But we enable it explicitly to be sure.
        val settings = FirebaseFirestoreSettings.Builder()
            .setPersistenceEnabled(true)
            .build()

        firestore.firestoreSettings = settings

        resetCurrentUser()
    }

    fun onSignIn(user: User) {
        if (!isAuthorized) {
            isAuthorized = true
            currentUserUid = user.uid
            startGettingCurrentUserRemoteUpdates()

            // After successful sign in we must write to Firestore
            // user name and email received from Firebase Auth.
            updateUserNameAndEmail(user)
        }
    }

    fun onSignOut() {
        if (isAuthorized) {
            isAuthorized = false
            stopGettingCurrentUserRemoteUpdates()
            stopGettingRemoteResultUpdates()
            resetCurrentUser()
        }
    }

    fun updateUserUsername(newUsername: String, onSuccess: () -> Unit, onError: () -> Unit) {
        val data = HashMap<String, Any>()
        data[USERNAME_KEY] = newUsername

        updateUserDataRemote(data, onSuccess, onError)
    }

    fun updateUserLocation(newLocation: LatLng) {
        val data = HashMap<String, Any>()
        data[LAT_KEY] = newLocation.latitude
        data[LON_KEY] = newLocation.longitude

        updateUserDataRemote(data, { /* Do nothing */ }, { /* Do nothing */ })
    }

    fun updateUserOnlineStatus(newIsOnline: Boolean, onComplete: () -> Unit) {
        val data = HashMap<String, Any>()
        data[IS_ONLINE_KEY] = newIsOnline

        updateUserDataRemote(data, onComplete, onComplete)
    }

    fun startGettingRemoteResultUpdates(onSuccess: (MutableList<User>) -> Unit) {
        if (isAuthorized) {
            searchResultListenerRegistration = firestore.collection(USERS_COLLECTION)
                .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                    val resultList = mutableListOf<User>()

                    if (firebaseFirestoreException == null) {
                        if (querySnapshot != null) {
                            Timber.tag(TAG).d("Listen success")

                            for (doc in querySnapshot) {
                                if (doc.id != currentUser.value?.uid) {
                                    resultList.add(getUserFromDocumentSnapshot(doc))
                                }
                            }

                        } else {
                            Timber.tag(TAG).d("Listen failed")
                        }

                    } else {
                        Timber.tag(TAG).d(firebaseFirestoreException)
                    }

                    onSuccess(resultList)
                }
        }
    }

    fun stopGettingRemoteResultUpdates() {
        searchResultListenerRegistration.remove()
    }

    fun deleteUserDataRemote(onSuccess: () -> Unit, onError: () -> Unit) {
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

    private fun resetCurrentUser() {
        currentUser.value = createAnonymousUser()
        currentUserUid = ""
    }

    private fun createAnonymousUser(): User {
        return User(
            uid = "",
            name = Constants.Auth.DEFAULT_USER_NAME,
            username = "",
            email = Constants.Auth.DEFAULT_USER_MAIL,
            isOnline = false,
            location = LatLng(Constants.Map.DEFAULT_LATITUDE, Constants.Map.DEFAULT_LONGITUDE)
        )
    }

    private fun updateUserNameAndEmail(user: User) {
        val data = HashMap<String, Any>()
        data[NAME_KEY] = user.name
        data[EMAIL_KEY] = user.email

        updateUserDataRemote(data, { /* Do nothing */ }, { /* Do nothing */ })
    }

    private fun updateUserDataRemote(data: HashMap<String, Any>, onSuccess: () -> Unit, onError: () -> Unit) {
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
                            currentUser.value = getUserFromDocumentSnapshot(snapshot)

                        } else {
                            Timber.tag(TAG).d("Listen failed")
                        }

                    } else {
                        Timber.tag(TAG).d(firebaseFirestoreException)
                    }
                }
        }
    }

    private fun stopGettingCurrentUserRemoteUpdates() {
        currentUserListenerRegistration.remove()
    }

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
            isOnline = doc.getBoolean(IS_ONLINE_KEY) ?: false,
            location = location
        )
    }
}