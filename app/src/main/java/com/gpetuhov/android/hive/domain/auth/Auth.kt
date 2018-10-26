package com.gpetuhov.android.hive.domain.auth

interface Auth {
    fun signOut(onSuccess: () -> Unit, onError: () -> Unit)
}