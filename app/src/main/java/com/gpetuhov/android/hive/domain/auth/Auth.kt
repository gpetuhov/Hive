package com.gpetuhov.android.hive.domain.auth

import android.app.Activity

interface Auth {
    fun init(onSignIn: () -> Unit, onSignOut: () -> Unit)
    fun showLoginScreen(activity: Activity, resultCode: Int)
    fun startListenAuth()
    fun stopListenAuth()
    fun dismissDialogs()
    fun signOut(onSuccess: () -> Unit, onError: () -> Unit)
    fun deleteAccount(onSuccess: () -> Unit, onError: () -> Unit)
}