package com.gpetuhov.android.hive.domain.interactor

import com.gpetuhov.android.hive.application.HiveApp
import com.gpetuhov.android.hive.domain.auth.Auth
import com.gpetuhov.android.hive.domain.network.Network
import com.gpetuhov.android.hive.domain.util.ResultMessages
import javax.inject.Inject

class DeleteUserInteractor(private val callback: Callback) : Interactor {

    interface Callback {
        fun onDeleteUserComplete(message: String)
    }

    @Inject lateinit var network: Network
    @Inject lateinit var auth: Auth
    @Inject lateinit var resultMessages: ResultMessages

    init {
        HiveApp.appComponent.inject(this)
    }

    override fun execute() {
        // Try to delete account if online only
        if (network.isOnline()) {
            auth.deleteAccount(
                { callback.onDeleteUserComplete(resultMessages.getDeleteUserSuccessMessage()) },
                { callback.onDeleteUserComplete(resultMessages.getDeleteUserErrorMessage()) }
            )
        } else {
            callback.onDeleteUserComplete(resultMessages.getDeleteUserNetworkErrorMessage())
        }
    }
}