package com.gpetuhov.android.hive.util

import android.content.Context
import com.gpetuhov.android.hive.R
import com.gpetuhov.android.hive.application.HiveApp
import com.gpetuhov.android.hive.domain.util.ResultMessages
import javax.inject.Inject

class ResultMessagesProvider : ResultMessages {

    @Inject lateinit var context: Context

    init {
        HiveApp.appComponent.inject(this)
    }

    override fun getSignOutErrorMessage(): String = getString(R.string.sign_out_error)

    override fun getSignOutNetworkErrorMessage(): String = getString(R.string.sign_out_no_network)

    override fun getDeleteUserSuccessMessage(): String = getString(R.string.delete_account_success)

    override fun getDeleteUserErrorMessage(): String = getString(R.string.delete_account_error)

    override fun getDeleteUserNetworkErrorMessage(): String = getString(R.string.delete_account_no_network)

    override fun getSaveUsernameErrorMessage(): String = getString(R.string.username_save_error)

    override fun getSaveServiceErrorMessage(): String = getString(R.string.service_save_error)

    override fun getDeleteServiceErrorMessage(): String = getString(R.string.service_delete_error)

    override fun getSaveVisibilityErrorMessage(): String = getString(R.string.visibility_save_error)

    override fun getSendMessageErrorMessage(): String = getString(R.string.send_message_error)

    override fun getChangeUserPicErrorMessage(): String = getString(R.string.change_user_picture_error)

    private fun getString(stringId: Int) = context.getString(stringId)
}