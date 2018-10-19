package com.gpetuhov.android.hive.managers

import android.app.Activity
import android.content.Context
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.gpetuhov.android.hive.R
import com.gpetuhov.android.hive.model.User
import com.gpetuhov.android.hive.util.Constants
import timber.log.Timber

class AuthManager {

    companion object {
        private const val TAG = "AuthManager"
    }

    var user = createAnonymousUser()

    private var firebaseAuth = FirebaseAuth.getInstance()
    private lateinit var authStateListener: FirebaseAuth.AuthStateListener

    fun init(onSignIn: (User) -> (Unit), onSignOut: () -> (Unit)) {
        authStateListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            val firebaseUser = firebaseAuth.currentUser

            if (firebaseUser != null) {
                // User is signed in
                Timber.tag(TAG).d("Login successful")
                Timber.tag(TAG).d("User id = ${firebaseUser.uid}")
                Timber.tag(TAG).d("User name = ${firebaseUser.displayName}")
                Timber.tag(TAG).d("User email = ${firebaseUser.email}")

                user = convertFirebaseUser(firebaseUser)
                onSignIn(user)

            } else {
                // User is signed out
                user = createAnonymousUser()
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

    fun signOut(context: Context?, onSuccess: () -> (Unit), onError: () -> (Unit)) {
        if (context != null) {
            AuthUI.getInstance()
                .signOut(context)
                .addOnSuccessListener {
                    Timber.tag(TAG).d("Sign out successful")
                    onSuccess()
                }
                .addOnFailureListener {
                    Timber.tag(TAG).d("Sign out error")
                    onError()
                }
        }
    }

    fun deleteAccount(context: Context?, onSuccess: () -> (Unit), onError: () -> (Unit)) {
        if (context != null) {
            AuthUI.getInstance()
                .delete(context)
                .addOnSuccessListener {
                    Timber.tag(TAG).d("User deleted successfully")
                    onSuccess()
                }
                .addOnFailureListener {
                    Timber.tag(TAG).d("Error deleting user")
                    onError()
                }
        }
    }

    private fun createAnonymousUser(): User {
        return User(
            "",
            Constants.Auth.DEFAULT_USER_NAME,
            Constants.Auth.DEFAULT_USER_MAIL
        )
    }

    private fun convertFirebaseUser(firebaseUser: FirebaseUser): User {
        return User(
            firebaseUser.uid,
            firebaseUser.displayName ?: Constants.Auth.DEFAULT_USER_NAME,
            firebaseUser.email ?: Constants.Auth.DEFAULT_USER_MAIL
        )
    }
}