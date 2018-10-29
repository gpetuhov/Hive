package com.gpetuhov.android.hive.interactors

import android.content.Context
import com.gpetuhov.android.hive.application.HiveApp
import com.gpetuhov.android.hive.domain.auth.Auth
import com.gpetuhov.android.hive.domain.interactor.DeleteUserInteractor
import com.gpetuhov.android.hive.domain.network.Network
import com.gpetuhov.android.hive.utils.Constants
import com.gpetuhov.android.hive.utils.TestAuthManager
import com.gpetuhov.android.hive.utils.TestNetworkManager
import com.gpetuhov.android.hive.utils.dagger.components.DaggerTestAppComponent
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import javax.inject.Inject

class DeleteUserInteractorTest {

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
    fun deleteUserSuccess() {
        testDeleteUserInteractor(true, true)
    }

    @Test
    fun deleteUserError() {
        testDeleteUserInteractor(false, true)
    }

    @Test
    fun deleteUserNetworkError() {
        testDeleteUserInteractor(false, false)
    }

    private fun testDeleteUserInteractor(isSuccess: Boolean, isOnline: Boolean) {
        // Set test network manager online / offline
        (network as TestNetworkManager).onlineResult = isOnline

        // Set test auth manager success
        (auth as TestAuthManager).deleteUserSuccess = isSuccess

        // DeleteUserInteractor will return result into this callback
        val callback = object : DeleteUserInteractor.Callback {
            override fun onDeleteUserComplete(message: String) {
                // We know what message should be,
                // because we mock Messages provider in tests.
                val expectedMessage = when {
                    isSuccess -> Constants.DELETE_USER_SUCCESS
                    isOnline -> Constants.DELETE_USER_ERROR
                    else -> Constants.DELETE_USER_NETWORK_ERROR
                }

                assertEquals(expectedMessage, message)
            }
        }

        val interactor = DeleteUserInteractor(callback)
        interactor.execute()
    }
}