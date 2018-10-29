package com.gpetuhov.android.hive.utils.dagger

import com.gpetuhov.android.hive.domain.util.Messages
import com.gpetuhov.android.hive.utils.Constants

class TestMessagesProvider : Messages {
    override fun getSignOutErrorMessage() = ""

    override fun getSignOutNetworkErrorMessage() = ""

    override fun getDeleteUserSuccessMessage() = Constants.DELETE_USER_SUCCESS

    override fun getDeleteUserErrorMessage() = Constants.DELETE_USER_ERROR

    override fun getDeleteUserNetworkErrorMessage() = Constants.DELETE_USER_NETWORK_ERROR

    override fun getSaveUsernameErrorMessage() = ""
}