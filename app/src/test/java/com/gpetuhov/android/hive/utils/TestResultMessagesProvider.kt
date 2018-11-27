package com.gpetuhov.android.hive.utils

import com.gpetuhov.android.hive.domain.util.ResultMessages

class TestResultMessagesProvider : ResultMessages {
    override fun getSignOutErrorMessage() = Constants.SIGN_OUT_ERROR

    override fun getSignOutNetworkErrorMessage() = Constants.SIGN_OUT_NETWORK_ERROR

    override fun getDeleteUserSuccessMessage() = Constants.DELETE_USER_SUCCESS

    override fun getDeleteUserErrorMessage() = Constants.DELETE_USER_ERROR

    override fun getDeleteUserNetworkErrorMessage() = Constants.DELETE_USER_NETWORK_ERROR

    override fun getSaveUsernameErrorMessage() = Constants.SAVE_USERNAME_ERROR

    override fun getSaveOfferErrorMessage() = Constants.SAVE_OFFER_ERROR

    override fun getDeleteOfferErrorMessage() = Constants.DELETE_OFFER_ERROR

    override fun getSaveVisibilityErrorMessage(): String = Constants.SAVE_VISIBILITY_ERROR

    override fun getSendMessageErrorMessage(): String = Constants.SEND_MESSAGE_ERROR

    override fun getChangeUserPicErrorMessage(): String = ""
}