package com.gpetuhov.android.hive.repository

import com.google.android.gms.maps.model.LatLng
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.gpetuhov.android.hive.util.Constants
import timber.log.Timber
import java.util.*

class Repository {

    companion object {
        private const val TAG = "Repository"
        private const val USERS_COLLECTION = "users"
        private const val LAT_KEY = "lat"
        private const val LON_KEY = "lon"
    }

    private val firestore = FirebaseFirestore.getInstance()
    private lateinit var registration: ListenerRegistration

//    private val userName = UUID.randomUUID().toString()
    private val userName = "111"

    fun writeLocation(location: LatLng) {
        // Create a new user with a first and last name
        val user = HashMap<String, Any>()
        user[LAT_KEY] = location.latitude
        user[LON_KEY] = location.longitude

        // Add a new document with a generated ID
        firestore.collection(USERS_COLLECTION).document(userName)
            .set(user)
            .addOnSuccessListener {
                Timber.tag(TAG).d("DocumentSnapshot successfully written")
            }
            .addOnFailureListener { error ->
                Timber.tag(TAG).d("Error writing document")
            }
    }

    fun startGettingLocationUpdates(onSuccess: (MutableList<LatLng>) -> (Unit)) {
        // TODO: exclude current user location from collection
        // TODO: maybe it is doc.id ???

        registration = firestore.collection(USERS_COLLECTION)
            .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                val locationList = mutableListOf<LatLng>()

                if (firebaseFirestoreException == null) {
                    if (querySnapshot != null) {
                        Timber.tag(TAG).d("Listen success")

                        for (doc in querySnapshot) {
                            val location = LatLng(
                                doc.getDouble(LAT_KEY) ?: Constants.Map.DEFAULT_LATITUDE,
                                doc.getDouble(LON_KEY) ?: Constants.Map.DEFAULT_LONGITUDE
                            )

                            locationList.add(location)
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

    private fun deleteUser() {
        firestore.collection(USERS_COLLECTION).document(userName)
            .delete()
            .addOnSuccessListener {
                Timber.tag(TAG).d("DocumentSnapshot successfully deleted")
            }
            .addOnFailureListener {
                Timber.tag(TAG).d("Error deleting document")
            }
    }
}