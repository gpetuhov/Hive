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

    @Before
    fun init() {
        val testAppComponent = DaggerTestAppComponent.builder().build()
        testAppComponent.inject(this)

        HiveApp.application = context as HiveApp
        HiveApp.appComponent = testAppComponent
    }

    @Test
    fun signOutSuccess() {
        testSignOutInteractor(true, true)
    }

    @Test
    fun signOutError() {
        testSignOutInteractor(false, true)
    }

    @Test
    fun signOutNetworkError() {
        testSignOutInteractor(false, false)
    }

    private fun testSignOutInteractor(isSuccess: Boolean, isOnline: Boolean) {
        (network as TestNetworkManager).onlineResult = isOnline
        (auth as TestAuthManager).actionSuccess = isSuccess

        var successCounter = 0
        var errorCounter = 0

        val callback = object : SignOutInteractor.Callback {
            override fun onSignOutSuccess() {
                successCounter++
            }

            override fun onSignOutError(errorMessage: String) {
                errorCounter++

                val expectedMessage = if (isOnline) Constants.SIGN_OUT_ERROR else Constants.SIGN_OUT_NETWORK_ERROR
                assertEquals(expectedMessage, errorMessage)
            }
        }

        val interactor = SignOutInteractor(callback)
        interactor.execute()

        assertEquals(if (isSuccess) 1 else 0, successCounter)
        assertEquals(if (isSuccess) 0 else 1, errorCounter)
    }
}