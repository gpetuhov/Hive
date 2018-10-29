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
    fun deleteUserNetworkError() {
        // Make test network manager "offline"
        (network as TestNetworkManager).onlineResult = false

        // DeleteUserInteractor will return result into this callback
        val callback = object : DeleteUserInteractor.Callback {
            override fun onDeleteUserComplete(message: String) {
                // We know what message will be in "offline",
                // because we mock Messages provider in TestAppModule.
                assertEquals(Constants.DELETE_USER_NETWORK_ERROR, message)
            }
        }

        val interactor = DeleteUserInteractor(callback)
        interactor.execute()
    }

    @Test
    fun deleteUserSuccess() {
        (network as TestNetworkManager).onlineResult = true
        (auth as TestAuthManager).deleteUserSuccess = true

        val callback = object : DeleteUserInteractor.Callback {
            override fun onDeleteUserComplete(message: String) {
                assertEquals(Constants.DELETE_USER_SUCCESS, message)
            }
        }

        val interactor = DeleteUserInteractor(callback)
        interactor.execute()
    }

    @Test
    fun deleteUserError() {
        (network as TestNetworkManager).onlineResult = true
        (auth as TestAuthManager).deleteUserSuccess = false

        val callback = object : DeleteUserInteractor.Callback {
            override fun onDeleteUserComplete(message: String) {
                assertEquals(Constants.DELETE_USER_ERROR, message)
            }
        }

        val interactor = DeleteUserInteractor(callback)
        interactor.execute()
    }
}