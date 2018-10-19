package com.gpetuhov.android.hive.repository

import com.google.android.gms.maps.model.LatLng
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.SetOptions
import com.gpetuhov.android.hive.application.HiveApp
import com.gpetuhov.android.hive.managers.AuthManager
import com.gpetuhov.android.hive.model.User
import com.gpetuhov.android.hive.util.Constants
import timber.log.Timber
import java.util.*
import javax.inject.Inject

class Repository {

    companion object {
        private const val TAG = "Repository"
        private const val USERS_COLLECTION = "users"
        private const val NAME_KEY = "name"
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
    }

    fun updateUserNameAndEmail() {
        val data = HashMap<String, Any>()
        data[NAME_KEY] = authManager.user.name
        data[EMAIL_KEY] = authManager.user.email

        updateUserData(data)
    }

    fun updateUserLocation(location: LatLng) {
        val data = HashMap<String, Any>()
        data[LAT_KEY] = location.latitude
        data[LON_KEY] = location.longitude

        updateUserData(data)
    }

    fun updateUserOnlineStatus() {
        val data = HashMap<String, Any>()
        data[IS_ONLINE_KEY] = authManager.user.isOnline

        updateUserData(data)
    }

    fun startGettingResultUpdates(onSuccess: (MutableList<User>) -> (Unit)) {
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

    fun deleteLocation() {
//        firestore.collection(USERS_COLLECTION).document(authManager.user.uid)
//            .delete()
//            .addOnSuccessListener {
//                Timber.tag(TAG).d("DocumentSnapshot successfully deleted")
//            }
//            .addOnFailureListener {
//                Timber.tag(TAG).d("Error deleting document")
//            }
    }

    private fun updateUserData(data: HashMap<String, Any>) {
        if (authManager.isAuthorized) {
            firestore.collection(USERS_COLLECTION).document(authManager.user.uid)
                .set(data, SetOptions.merge())  // this is needed to update only the required data if the user exists
                .addOnSuccessListener {
                    Timber.tag(TAG).d("User data successfully written")
                }
                .addOnFailureListener { error ->
                    Timber.tag(TAG).d("Error writing user data")
                }
        }
    }

    private fun getUserFromDocumentSnapshot(doc: QueryDocumentSnapshot): User {
        val location = LatLng(
            doc.getDouble(LAT_KEY) ?: Constants.Map.DEFAULT_LATITUDE,
            doc.getDouble(LON_KEY) ?: Constants.Map.DEFAULT_LONGITUDE
        )

        return User(
            uid = doc.id,
            name = doc.getString(NAME_KEY) ?: Constants.Auth.DEFAULT_USER_NAME,
            email = doc.getString(EMAIL_KEY) ?: Constants.Auth.DEFAULT_USER_MAIL,
            isOnline = doc.getBoolean(IS_ONLINE_KEY) ?: false,
            location = location
        )
    }
}