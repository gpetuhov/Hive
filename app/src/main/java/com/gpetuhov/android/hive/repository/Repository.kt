package com.gpetuhov.android.hive.repository

import com.google.firebase.firestore.FirebaseFirestore
import timber.log.Timber

class Repository {

    companion object {
        private const val TAG = "Repository"
    }

    private val firestore = FirebaseFirestore.getInstance()

    private fun writeUser() {
        // Create a new user with a first and last name
        val user = HashMap<Any, Any>()
        user["first"] = "Ada"
        user["last"] = "Lovelace"
        user["born"] = 1815

        // Add a new document with a generated ID
        firestore.collection("users")
            .add(user)
            .addOnSuccessListener { documentReference ->
                Timber.tag(TAG).d("DocumentSnapshot added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { error ->
                Timber.tag(TAG).d("Error adding document")
            }
    }
}