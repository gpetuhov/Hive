package com.gpetuhov.android.hive.interactors

import android.content.Context
import com.gpetuhov.android.hive.application.HiveApp
import com.gpetuhov.android.hive.domain.interactor.SaveTwitterInteractor
import com.gpetuhov.android.hive.domain.interactor.base.SaveUserPropertyInteractor
import com.gpetuhov.android.hive.domain.repository.Repo
import com.gpetuhov.android.hive.utils.Constants
import com.gpetuhov.android.hive.utils.TestRepository
import com.gpetuhov.android.hive.utils.dagger.components.DaggerTestAppComponent
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import javax.inject.Inject

class SaveTwitterInteractorTest {

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
    fun saveTwitterSuccess() {
        testSaveTwitterInteractor(true)
    }

    @Test
    fun saveTwitterError() {
        testSaveTwitterInteractor(false)
    }

    private fun testSaveTwitterInteractor(isSuccess: Boolean) {
        (repo as TestRepository).isSuccess = isSuccess

        var errorCounter = 0

        val callback = object : SaveUserPropertyInteractor.Callback {
            override fun onSaveError(errorMessage: String) {
                errorCounter++
                assertEquals(Constants.SAVE_TWITTER_ERROR, errorMessage)
            }
        }

        val interactor = SaveTwitterInteractor(callback)
        interactor.save(Constants.DUMMY_TWITTER)

        assertEquals(if (isSuccess) Constants.DUMMY_TWITTER else "", (repo as TestRepository).twitter)
        assertEquals(if (isSuccess) 0 else 1, errorCounter)
    }
}