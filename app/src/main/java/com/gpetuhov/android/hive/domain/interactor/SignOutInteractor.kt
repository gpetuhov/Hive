package com.gpetuhov.android.hive.domain.interactor

import com.gpetuhov.android.hive.application.HiveApp
import com.gpetuhov.android.hive.domain.auth.Auth
import com.gpetuhov.android.hive.domain.interactor.base.Interactor
import com.gpetuhov.android.hive.domain.network.Network
import com.gpetuhov.android.hive.domain.util.ResultMessages
import javax.inject.Inject

class SignOutInteractor(private val callback: Callback) : Interactor {

    interface Callback {
        fun onSignOutSuccess()
        fun onSignOutError(errorMessage: String)
    }

    @Inject lateinit var network: Network
    @Inject lateinit var auth: Auth
    @Inject lateinit var resultMessages: ResultMessages

    init {
        HiveApp.appComponent.inject(this)
    }

    override fun execute() {
        // Try to sign out if online only
        if (network.isOnline()) {
            auth.signOut(
                { callback.onSignOutSuccess() },
                { callback.onSignOutError(resultMessages.getSignOutErrorMessage()) }
            )
        } else {
            callback.onSignOutError(resultMessages.getSignOutNetworkErrorMessage())
        }
    }
}