package com.gpetuhov.android.hive.managers

import android.app.Activity
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.gpetuhov.android.hive.R
import timber.log.Timber

class AuthManager {

    companion object {
        private const val TAG = "AuthManager"
    }

    private var firebaseAuth = FirebaseAuth.getInstance()
    private lateinit var authStateListener: FirebaseAuth.AuthStateListener

    fun init(onSignIn: (FirebaseUser) -> (Unit), onSignOut: () -> (Unit)) {
        authStateListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            val user = firebaseAuth.currentUser

            if (user != null) {
                // User is signed in
                Timber.tag(TAG).d("Login successful")
                Timber.tag(TAG).d("User id = ${user.uid}")
                Timber.tag(TAG).d("User name = ${user.displayName}")
                Timber.tag(TAG).d("User email = ${user.email}")

                onSignIn(user)

            } else {
                // User is signed out
                onSignOut()
            }
        }
    }

    fun showLoginScreen(activity: Activity, resultCode: Int) {
        val providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build(),
            AuthUI.IdpConfig.GoogleBuilder().build()
//                    AuthUI.IdpConfig.PhoneBuilder().build(),
//                    AuthUI.IdpConfig.FacebookBuilder().build(),
//                    AuthUI.IdpConfig.TwitterBuilder().build()
        )

        activity.startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setIsSmartLockEnabled(true)
                .setAvailableProviders(providers)
                .setTheme(R.style.AuthTheme)
                .build(),
            resultCode
        )
    }

    fun startListenAuth() {
        firebaseAuth.addAuthStateListener(authStateListener)
    }

    fun stopListenAuth() {
        firebaseAuth.removeAuthStateListener(authStateListener)
    }
}