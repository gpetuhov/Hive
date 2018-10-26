package com.gpetuhov.android.hive.domain.auth

interface Auth {
    fun signOut(onSuccess: () -> Unit, onError: () -> Unit)
    fun deleteAccount(onSuccess: () -> Unit, onError: () -> Unit)
}