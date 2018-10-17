package com.gpetuhov.android.hive.repository

import com.google.android.gms.maps.model.LatLng
import com.google.firebase.firestore.FirebaseFirestore
import timber.log.Timber
import java.util.*

class Repository {

    companion object {
        private const val TAG = "Repository"
    }

    private val firestore = FirebaseFirestore.getInstance()
    private val userName = UUID.randomUUID().toString()

    fun writeLocation(location: LatLng) {
        // Create a new user with a first and last name
        val user = HashMap<String, Any>()
        user["lat"] = location.latitude
        user["lon"] = location.longitude

        // Add a new document with a generated ID
        firestore.collection("users").document("111")
            .set(user)
            .addOnSuccessListener {
                Timber.tag(TAG).d("DocumentSnapshot successfully written")
            }
            .addOnFailureListener { error ->
                Timber.tag(TAG).d("Error writing document")
            }
    }

    private fun deleteUser() {

    }
}