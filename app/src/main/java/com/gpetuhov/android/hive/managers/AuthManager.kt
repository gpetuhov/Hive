package com.gpetuhov.android.hive.managers

import android.app.Activity
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.gpetuhov.android.hive.util.Constants

class AuthManager {

    private var firebaseAuth = FirebaseAuth.getInstance()
    private lateinit var authStateListener: FirebaseAuth.AuthStateListener

    fun init(activity: Activity, resultCode: Int) {
        authStateListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            val user = firebaseAuth.currentUser

            if (user != null) {
                // User is signed in
                onSignedInInitialize(user.displayName ?: Constants.Auth.DEFAULT_USER_NAME)

            } else {
                // User is signed out
                onSignedOutCleanup()

                val providers = arrayListOf(
                    AuthUI.IdpConfig.EmailBuilder().build()
//                    AuthUI.IdpConfig.PhoneBuilder().build(),
//                    AuthUI.IdpConfig.GoogleBuilder().build(),
//                    AuthUI.IdpConfig.FacebookBuilder().build(),
//                    AuthUI.IdpConfig.TwitterBuilder().build()
                )

                activity.startActivityForResult(
                    AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setIsSmartLockEnabled(false)
                        .setAvailableProviders(providers)
                        .build(),
                    resultCode
                )
            }
        }
    }

    fun startListenAuth() {
        firebaseAuth.addAuthStateListener(authStateListener)
    }

    fun stopListenAuth() {
        firebaseAuth.removeAuthStateListener(authStateListener)
    }

    private fun onSignedInInitialize(userName: String) {
        // TODO: implement this
    }

    private fun onSignedOutCleanup() {
        // TODO: implement this
    }
}