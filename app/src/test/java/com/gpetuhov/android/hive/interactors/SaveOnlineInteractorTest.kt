package com.gpetuhov.android.hive.interactors

import android.content.Context
import com.gpetuhov.android.hive.application.HiveApp
import com.gpetuhov.android.hive.domain.interactor.SaveOnlineInteractor
import com.gpetuhov.android.hive.domain.repository.Repo
import com.gpetuhov.android.hive.utils.TestRepository
import com.gpetuhov.android.hive.utils.dagger.components.DaggerTestAppComponent
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import javax.inject.Inject

class SaveOnlineInteractorTest {

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
    fun saveOnlineSuccess() {
        testSaveOnlineInteractor(true)
    }

    @Test
    fun saveOnlineError() {
        testSaveOnlineInteractor(false)
    }

    private fun testSaveOnlineInteractor(isSuccess: Boolean) {
        (repo as TestRepository).isSuccess = isSuccess

        var completeCounter = 0

        val callback = object : SaveOnlineInteractor.Callback {
            override fun onSaveOnlineComplete() {
                completeCounter++
            }
        }

        val interactor = SaveOnlineInteractor(callback)
        interactor.saveVisibility(true)

        assertEquals(isSuccess, (repo as TestRepository).isOnline)
        assertEquals(1, completeCounter)
    }
}