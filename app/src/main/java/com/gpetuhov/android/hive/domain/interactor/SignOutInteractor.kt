package com.gpetuhov.android.hive.domain.interactor

import com.gpetuhov.android.hive.application.HiveApp
import com.gpetuhov.android.hive.domain.auth.Auth
import com.gpetuhov.android.hive.domain.network.Network
import javax.inject.Inject

class SignOutInteractor(val callback: Callback) : Interactor {

    interface Callback {
        fun onSignOutSuccess()
        fun onSignOutError(errorMessage: String)
    }

    @Inject lateinit var network: Network
    @Inject lateinit var auth: Auth

    init {
        HiveApp.appComponent.inject(this)
    }

    override fun execute() {
        // Try to sign out if online only
        if (network.isOnline()) {
            auth.signOut(
                { callback.onSignOutSuccess() },
                { callback.onSignOutError("error") }
            )
        } else {
            callback.onSignOutError("network error")
        }
    }
}