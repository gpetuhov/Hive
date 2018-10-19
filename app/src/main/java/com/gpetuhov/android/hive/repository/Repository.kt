package com.gpetuhov.android.hive.repository

import com.google.android.gms.maps.model.LatLng
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.SetOptions
import com.gpetuhov.android.hive.application.HiveApp
import com.gpetuhov.android.hive.managers.AuthManager
import com.gpetuhov.android.hive.util.Constants
import timber.log.Timber
import java.util.*
import javax.inject.Inject

class Repository {

    companion object {
        private const val TAG = "Repository"
        private const val USERS_COLLECTION = "users"
        private const val NAME_KEY = "name"
        private const val LAT_KEY = "lat"
        private const val LON_KEY = "lon"
    }

    @Inject lateinit var authManager: AuthManager

    private val firestore = FirebaseFirestore.getInstance()
    private lateinit var registration: ListenerRegistration

    init {
        HiveApp.appComponent.inject(this)
    }

    fun updateUserName() {
        val data = HashMap<String, Any>()
        data[NAME_KEY] = authManager.user.name

        updateUserData(data)
    }

    fun updateUserLocation(location: LatLng) {
        val data = HashMap<String, Any>()
        data[LAT_KEY] = location.latitude
        data[LON_KEY] = location.longitude

        updateUserData(data)
    }

    fun startGettingLocationUpdates(onSuccess: (MutableList<LatLng>) -> (Unit)) {
        registration = firestore.collection(USERS_COLLECTION)
            .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                val locationList = mutableListOf<LatLng>()

                if (firebaseFirestoreException == null) {
                    if (querySnapshot != null) {
                        Timber.tag(TAG).d("Listen success")

                        for (doc in querySnapshot) {
                            if (doc.id != authManager.user.uid) {
                                val location = LatLng(
                                    doc.getDouble(LAT_KEY) ?: Constants.Map.DEFAULT_LATITUDE,
                                    doc.getDouble(LON_KEY) ?: Constants.Map.DEFAULT_LONGITUDE
                                )

                                locationList.add(location)
                            }
                        }

                    } else {
                        Timber.tag(TAG).d("Listen failed")
                    }

                } else {
                    Timber.tag(TAG).d(firebaseFirestoreException)
                }

                onSuccess(locationList)
            }
    }

    fun stopGettingLocationUpdates() {
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