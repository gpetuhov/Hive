package com.gpetuhov.android.hive.interactors

import android.content.Context
import com.gpetuhov.android.hive.application.HiveApp
import com.gpetuhov.android.hive.domain.auth.Auth
import com.gpetuhov.android.hive.domain.interactor.SignOutInteractor
import com.gpetuhov.android.hive.domain.network.Network
import com.gpetuhov.android.hive.utils.Constants
import com.gpetuhov.android.hive.utils.TestNetworkManager
import com.gpetuhov.android.hive.utils.dagger.components.DaggerTestAppComponent
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import javax.inject.Inject

class SignOutInteractorTest {

    @Inject lateinit var context: Context
    @Inject lateinit var network: Network
    @Inject lateinit var auth: Auth

    @Before
    fun init() {
        val testAppComponent = DaggerTestAppComponent.builder().build()
        testAppComponent.inject(this)

        HiveApp.application = context as HiveApp
        HiveApp.appComponent = testAppComponent
    }

    @Test
    fun signOutNetworkError() {
        (network as TestNetworkManager).onlineResult = false

        val callback = object : SignOutInteractor.Callback {
            override fun onSignOutSuccess() {
                // Do nothing
            }

            override fun onSignOutError(errorMessage: String) {
                assertEquals(Constants.SIGN_OUT_NETWORK_ERROR, errorMessage)
            }
        }

        val interactor = SignOutInteractor(callback)
        interactor.execute()
    }
}