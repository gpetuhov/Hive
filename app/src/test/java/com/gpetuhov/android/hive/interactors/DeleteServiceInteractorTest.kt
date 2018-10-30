package com.gpetuhov.android.hive.interactors

import android.content.Context
import com.gpetuhov.android.hive.application.HiveApp
import com.gpetuhov.android.hive.domain.interactor.DeleteServiceInteractor
import com.gpetuhov.android.hive.domain.repository.Repo
import com.gpetuhov.android.hive.utils.Constants
import com.gpetuhov.android.hive.utils.TestRepository
import com.gpetuhov.android.hive.utils.dagger.components.DaggerTestAppComponent
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import javax.inject.Inject

class DeleteServiceInteractorTest {

    @Inject lateinit var context: Context
    @Inject lateinit var repo: Repo

    @Before
    fun init() {
        val testAppComponent = DaggerTestAppComponent.builder().build()
        testAppComponent.inject(this)

        HiveApp.application = context as HiveApp
        HiveApp.appComponent = testAppComponent
    }

    @Test
    fun deleteServiceSuccess() {
        testDeleteServiceInteractor(true)
    }

    @Test
    fun deleteServiceError() {
        testDeleteServiceInteractor(false)
    }

    private fun testDeleteServiceInteractor(isSuccess: Boolean) {
        (repo as TestRepository).isSuccess = isSuccess

        var errorCounter = 0

        val callback = object : DeleteServiceInteractor.Callback {
            override fun onDeleteServiceError(errorMessage: String) {
                errorCounter++
                assertEquals(Constants.DELETE_SERVICE_ERROR, errorMessage)
            }
        }

        val interactor = DeleteServiceInteractor(callback)
        interactor.execute()

        assertEquals("", (repo as TestRepository).service)
        assertEquals(if (isSuccess) 0 else 1, errorCounter)
    }
}