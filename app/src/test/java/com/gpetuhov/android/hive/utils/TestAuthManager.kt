package com.gpetuhov.android.hive.utils

import android.app.Activity
import com.gpetuhov.android.hive.domain.auth.Auth

class TestAuthManager : Auth {
    var actionSuccess = false

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
        mockedAction(onSuccess, onError)
    }

    override fun deleteAccount(onSuccess: () -> Unit, onError: () -> Unit) {
        mockedAction(onSuccess, onError)
    }

    private fun mockedAction(onSuccess: () -> Unit, onError: () -> Unit) {
        if (actionSuccess) onSuccess() else onError()
    }
}