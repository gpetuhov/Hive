package com.gpetuhov.android.hive.managers

import android.app.Activity
import android.content.Context
import com.afollestad.materialdialogs.MaterialDialog
import com.firebase.ui.auth.AuthUI
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.gpetuhov.android.hive.R
import com.gpetuhov.android.hive.application.HiveApp
import com.gpetuhov.android.hive.domain.auth.Auth
import com.gpetuhov.android.hive.domain.model.User
import com.gpetuhov.android.hive.domain.network.Network
import com.gpetuhov.android.hive.repository.Repository
import com.gpetuhov.android.hive.util.Constants
import timber.log.Timber
import javax.inject.Inject

// Manages authentication with Firebase Auth
class AuthManager : Auth {

    companion object {
        private const val TAG = "AuthManager"
    }

    @Inject lateinit var context: Context
    @Inject lateinit var repo: Repository
    @Inject lateinit var network: Network

    private var firebaseAuth = FirebaseAuth.getInstance()
    private lateinit var authStateListener: FirebaseAuth.AuthStateListener
    private var noNetworkDialog: MaterialDialog? = null

    init {
        HiveApp.appComponent.inject(this)
    }

    override fun signOut(onSuccess: () -> Unit, onError: () -> Unit) {
        // When signing out, first set status to offline,
        // and only after that sign out (because after signing out,
        // updating backend will be impossible)
        // We are signing out regardless of online status update success
        repo.updateUserOnlineStatus(false) {
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

    override fun deleteAccount(onSuccess: () -> Unit, onError: () -> Unit) {
        deleteAccount(context, onSuccess, onError)
    }

    fun init(onSignIn: () -> Unit, onSignOut: () -> Unit) {
        authStateListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            val firebaseUser = firebaseAuth.currentUser

            if (firebaseUser != null) {
                // User is signed in
                Timber.tag(TAG).d("Login successful")
                Timber.tag(TAG).d("User id = ${firebaseUser.uid}")
                Timber.tag(TAG).d("User name = ${firebaseUser.displayName}")
                Timber.tag(TAG).d("User email = ${firebaseUser.email}")

                repo.onSignIn(convertFirebaseUser(firebaseUser))
                onSignIn()

            } else {
                // User is signed out
                Timber.tag(TAG).d("User signed out")

                repo.onSignOut()
                onSignOut()
            }
        }
    }

    fun showLoginScreen(activity: Activity, resultCode: Int) {
        if (network.isOnline()) {
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

        } else {
            showNoNetworkDialog(activity, resultCode)
        }
    }

    fun startListenAuth() = firebaseAuth.addAuthStateListener(authStateListener)

    fun stopListenAuth() = firebaseAuth.removeAuthStateListener(authStateListener)

    fun deleteAccount(context: Context?, onSuccess: () -> Unit, onError: () -> Unit) {
        // When deleting account, delete user data from backend first
        // (because after deleting account, the user will be unauthorized,
        // and updating backend will be impossible)

        repo.deleteUserDataRemote({
            // Proceed with account deletion, only if user data has been successfully deleted
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
        },
            onError
        )
    }

    fun dismissDialogs() = dismissNoNetworkDialog()

    private fun convertFirebaseUser(firebaseUser: FirebaseUser): User {
        return User(
            uid = firebaseUser.uid,
            name = firebaseUser.displayName ?: Constants.Auth.DEFAULT_USER_NAME,
            username = "",
            email = firebaseUser.email ?: Constants.Auth.DEFAULT_USER_MAIL,
            isOnline = false,
            location = LatLng(Constants.Map.DEFAULT_LATITUDE, Constants.Map.DEFAULT_LONGITUDE)
        )
    }

    private fun initNoNetworkDialog(activity: Activity, resultCode: Int) {
        noNetworkDialog = MaterialDialog(activity)
            .title(R.string.sign_in_error)
            .message(R.string.sign_in_no_network)
            .cancelable(false)
            .positiveButton { showLoginScreen(activity, resultCode) }
            .negativeButton { closeApp() }
    }

    private fun showNoNetworkDialog(activity: Activity, resultCode: Int) {
        dismissNoNetworkDialog()
        initNoNetworkDialog(activity, resultCode)
        noNetworkDialog?.show()
    }

    private fun dismissNoNetworkDialog() = noNetworkDialog?.dismiss()

    private fun closeApp() = System.exit(0)
}