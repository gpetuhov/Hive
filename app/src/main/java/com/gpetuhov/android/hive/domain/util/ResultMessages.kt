package com.gpetuhov.android.hive.domain.util

interface ResultMessages {
    fun getSignOutErrorMessage(): String
    fun getSignOutNetworkErrorMessage(): String
    fun getDeleteUserSuccessMessage(): String
    fun getDeleteUserErrorMessage(): String
    fun getDeleteUserNetworkErrorMessage(): String
    fun getSaveUsernameErrorMessage(): String
    fun getSaveOfferErrorMessage(): String
    fun getDeleteOfferErrorMessage(): String
    fun getSaveVisibilityErrorMessage(): String
    fun getSendMessageErrorMessage(): String
    fun getChangeUserPicErrorMessage(): String
}