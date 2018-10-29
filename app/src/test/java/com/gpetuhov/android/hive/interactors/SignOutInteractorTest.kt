package com.gpetuhov.android.hive.interactors

import android.content.Context
import com.gpetuhov.android.hive.application.HiveApp
import com.gpetuhov.android.hive.domain.auth.Auth
import com.gpetuhov.android.hive.domain.interactor.SignOutInteractor
import com.gpetuhov.android.hive.domain.network.Network
import com.gpetuhov.android.hive.utils.Constants
import com.gpetuhov.android.hive.utils.TestAuthManager
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

    private var successCounter = 0
    private var errorCounter = 0

    @Before
    fun init() {
        val testAppComponent = DaggerTestAppComponent.builder().build()
        testAppComponent.inject(this)

        HiveApp.application = context as HiveApp
        HiveApp.appComponent = testAppComponent

        successCounter = 0
        errorCounter = 0
    }

    @Test
    fun signOutNetworkError() {
        (network as TestNetworkManager).onlineResult = false

        val callback = object : SignOutInteractor.Callback {
            override fun onSignOutSuccess() {
                successCounter++
            }

            override fun onSignOutError(errorMessage: String) {
                errorCounter++
                assertEquals(Constants.SIGN_OUT_NETWORK_ERROR, errorMessage)
            }
        }

        val interactor = SignOutInteractor(callback)
        interactor.execute()

        assertEquals(successCounter, 0)
        assertEquals(errorCounter, 1)
    }

    @Test
    fun signOutSuccess() {
        (network as TestNetworkManager).onlineResult = true
        (auth as TestAuthManager).actionSuccess = true

        val callback = object : SignOutInteractor.Callback {
            override fun onSignOutSuccess() {
                successCounter++
            }

            override fun onSignOutError(errorMessage: String) {
                errorCounter++
            }
        }

        val interactor = SignOutInteractor(callback)
        interactor.execute()

        assertEquals(successCounter, 1)
        assertEquals(errorCounter, 0)
    }
}