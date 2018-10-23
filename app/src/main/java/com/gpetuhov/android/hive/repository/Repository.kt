package com.gpetuhov.android.hive.repository

import com.google.android.gms.maps.model.LatLng
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.SetOptions
import com.gpetuhov.android.hive.application.HiveApp
import com.gpetuhov.android.hive.managers.AuthManager
import com.gpetuhov.android.hive.model.User
import com.gpetuhov.android.hive.util.Constants
import timber.log.Timber
import java.util.*
import javax.inject.Inject
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

    @Inject lateinit var authManager: AuthManager

    private val firestore = FirebaseFirestore.getInstance()
    private lateinit var registration: ListenerRegistration

    init {
        HiveApp.appComponent.inject(this)

        // Offline data caching is enabled by default in Android.
        // But we enable it explicitly to be sure.
        val settings = FirebaseFirestoreSettings.Builder()
            .setPersistenceEnabled(true)
            .build()

        firestore.firestoreSettings = settings
    }

    fun updateUserNameAndEmail(onSuccess: () -> Unit, onError: () -> Unit) {
        val data = HashMap<String, Any>()
        data[NAME_KEY] = authManager.user.name
        data[EMAIL_KEY] = authManager.user.email

        updateUserData(data, onSuccess, onError)
    }

    fun updateUserUsername(onSuccess: () -> Unit, onError: () -> Unit) {
        val data = HashMap<String, Any>()
        data[USERNAME_KEY] = authManager.user.username

        updateUserData(data, onSuccess, onError)
    }

    fun getUserData(userUid: String, onSuccess: (User) -> Unit, onError: () -> Unit) {
        if (authManager.isAuthorized) {
            firestore.collection(USERS_COLLECTION).document(userUid).get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val document = task.result
                        if (document?.exists() == true) {
                            Timber.tag(TAG).d("User successfully retrieved")
                            onSuccess(getUserFromDocumentSnapshot(document))
                        } else {
                            Timber.tag(TAG).d("No such user")
                            onError()
                        }
                    } else {
                        Timber.tag(TAG).d("Error retrieving user")
                        Timber.tag(TAG).d(task.exception)
                        onError()
                    }
                }

        } else {
            onError()
        }
    }

    fun updateUserLocation(location: LatLng, onSuccess: () -> Unit, onError: () -> Unit) {
        val data = HashMap<String, Any>()
        data[LAT_KEY] = location.latitude
        data[LON_KEY] = location.longitude

        updateUserData(data, onSuccess, onError)
    }

    fun updateUserOnlineStatus(onSuccess: () -> Unit, onError: () -> Unit) {
        val data = HashMap<String, Any>()
        data[IS_ONLINE_KEY] = authManager.user.isOnline

        updateUserData(data, onSuccess, onError)
    }

    fun startGettingResultUpdates(onSuccess: (MutableList<User>) -> Unit) {
        if (authManager.isAuthorized) {
            registration = firestore.collection(USERS_COLLECTION)
                .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                    val resultList = mutableListOf<User>()

                    if (firebaseFirestoreException == null) {
                        if (querySnapshot != null) {
                            Timber.tag(TAG).d("Listen success")

                            for (doc in querySnapshot) {
                                if (doc.id != authManager.user.uid) {
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

    fun stopGettingResultUpdates() {
        registration.remove()
    }

    fun deleteUserData(onSuccess: () -> Unit, onError: () -> Unit) {
        if (authManager.isAuthorized) {
            firestore.collection(USERS_COLLECTION).document(authManager.user.uid)
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

    private fun updateUserData(data: HashMap<String, Any>, onSuccess: () -> Unit, onError: () -> Unit) {
        if (authManager.isAuthorized) {
            firestore.collection(USERS_COLLECTION).document(authManager.user.uid)
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