package com.gpetuhov.android.hive.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.gpetuhov.android.hive.application.HiveApp
import com.gpetuhov.android.hive.domain.auth.Auth
import com.pawegio.kandroid.startActivity
import javax.inject.Inject

// Just a blank activity to be shown in background behind FirebaseUI during authentication
// (so that the user won't see a bit of main activity on back button pressed in FirebaseUI)

// Login process (start FirebaseUI) should be initiated only from this activity

class AuthActivity : AppCompatActivity() {

    companion object {
        private const val RC_SIGN_IN = 102
    }

    @Inject lateinit var auth: Auth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        HiveApp.appComponent.inject(this)

        auth.init(this::onSignIn, this::onSignOut)
    }

    override fun onResume() {
        super.onResume()
        auth.startListenAuth()
    }

    override fun onPause() {
        super.onPause()
        auth.stopListenAuth()
    }

    override fun onDestroy() {
        super.onDestroy()
        auth.dismissDialogs()
    }

    private fun onSignIn() {
        startActivity<MainActivity>()
        finish()
    }

    private fun onSignOut() = auth.showLoginScreen(this, RC_SIGN_IN)
}