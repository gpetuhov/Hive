package com.gpetuhov.android.hive.domain.util

interface Messages {
    fun getSignOutErrorMessage(): String
    fun getSignOutNetworkErrorMessage(): String
    fun getDeleteUserSuccessMessage(): String
    fun getDeleteUserErrorMessage(): String
    fun getDeleteUserNetworkErrorMessage(): String
    fun getSaveUsernameErrorMessage(): String
    fun getSaveServiceErrorMessage(): String
    fun getDeleteServiceErrorMessage(): String
    fun getSaveVisibilityErrorMessage(): String
}