package com.gpetuhov.android.hive.utils

import android.app.Activity
import com.gpetuhov.android.hive.domain.auth.Auth

class TestAuthManager : Auth {
    var deleteUserSuccess = false

    override fun init(onSignIn: () -> Unit, onSignOut: () -> Unit) {
    }

    override fun showLoginScreen(activity: Activity, resultCode: Int) {
    }

    override fun startListenAuth() {
    }

    override fun stopListenAuth() {
    }

    override fun dismissDialogs() {
    }

    override fun signOut(onSuccess: () -> Unit, onError: () -> Unit) {
    }

    override fun deleteAccount(onSuccess: () -> Unit, onError: () -> Unit) {
        if (deleteUserSuccess) onSuccess() else onError()
    }
}