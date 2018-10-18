package com.gpetuhov.android.hive.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseUser
import com.gpetuhov.android.hive.application.HiveApp
import com.gpetuhov.android.hive.managers.AuthManager
import com.pawegio.kandroid.startActivity
import javax.inject.Inject

// Just a blank activity to be shown in background behind FirebaseUI during authentication
// (so that the user won't see a bit of main activity on back button pressed in FirebaseUI)
class AuthActivity : AppCompatActivity() {

    companion object {
        private const val RC_SIGN_IN = 102
    }

    @Inject lateinit var authManager: AuthManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        HiveApp.appComponent.inject(this)

        authManager.init(this, RC_SIGN_IN, this::onSignIn, this::onSignOut)
    }

    override fun onResume() {
        super.onResume()
        authManager.startListenAuth()
    }

    override fun onPause() {
        super.onPause()
        authManager.stopListenAuth()
    }

    private fun onSignIn(firebaseUser: FirebaseUser) {
        startActivity<MainActivity>()
        finish()
    }

    private fun onSignOut() {
        // Do nothing
    }
}