package com.gpetuhov.android.hive.interactors

import android.app.Application
import com.gpetuhov.android.hive.application.HiveApp
import com.gpetuhov.android.hive.domain.interactor.DeleteUserInteractor
import com.gpetuhov.android.hive.domain.network.Network
import com.gpetuhov.android.hive.utils.dagger.components.DaggerTestAppComponent
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.powermock.core.classloader.annotations.PrepareForTest
import org.powermock.modules.junit4.PowerMockRunner
import org.powermock.api.mockito.PowerMockito
import timber.log.Timber
import javax.inject.Inject


//@RunWith(PowerMockRunner::class)
//@PrepareForTest(HiveApp::class)
class DeleteUserInteractorTest {

    @Inject lateinit var testNetworkManager: Network

    @Before
    fun init() {
        val testAppComponent = DaggerTestAppComponent.builder().build()
        testAppComponent.inject(this)

//        val app = Mockito.mock(HiveApp::class.java)
//        whenever(HiveApp.application).thenReturn(app)
//        whenever(HiveApp.appComponent).thenReturn(testAppComponent)

        HiveApp.application = Mockito.mock(HiveApp::class.java)
        HiveApp.appComponent = testAppComponent

//        PowerMockito.mockStatic(HiveApp::class.java)
//        PowerMockito.`when`(HiveApp.component()).thenReturn(testAppComponent)

    }

    @Test
    fun deleteUserNetworkError() {
        val callback = object : DeleteUserInteractor.Callback {
            override fun onDeleteUserComplete(message: String) {
                assertEquals("network error", message)
            }
        }

        val interactor = DeleteUserInteractor(callback)
        interactor.execute()
    }
}