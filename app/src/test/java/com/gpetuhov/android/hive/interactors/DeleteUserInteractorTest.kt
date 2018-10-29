package com.gpetuhov.android.hive.interactors

import com.gpetuhov.android.hive.application.HiveApp
import com.gpetuhov.android.hive.domain.interactor.DeleteUserInteractor
import com.gpetuhov.android.hive.domain.network.Network
import com.gpetuhov.android.hive.utils.Constants
import com.gpetuhov.android.hive.utils.dagger.components.DaggerTestAppComponent
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import javax.inject.Inject

class DeleteUserInteractorTest {

    @Inject lateinit var testNetworkManager: Network

    @Before
    fun init() {
        val testAppComponent = DaggerTestAppComponent.builder().build()
        testAppComponent.inject(this)

        HiveApp.application = Mockito.mock(HiveApp::class.java)
        HiveApp.appComponent = testAppComponent
    }

    @Test
    fun deleteUserNetworkError() {
        val callback = object : DeleteUserInteractor.Callback {
            override fun onDeleteUserComplete(message: String) {
                assertEquals(Constants.DELETE_USER_NETWORK_ERROR, message)
            }
        }

        val interactor = DeleteUserInteractor(callback)
        interactor.execute()
    }
}