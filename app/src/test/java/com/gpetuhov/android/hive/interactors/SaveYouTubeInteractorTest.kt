package com.gpetuhov.android.hive.interactors

import android.content.Context
import com.gpetuhov.android.hive.application.HiveApp
import com.gpetuhov.android.hive.domain.interactor.SaveYouTubeInteractor
import com.gpetuhov.android.hive.domain.interactor.base.SaveUserPropertyInteractor
import com.gpetuhov.android.hive.domain.repository.Repo
import com.gpetuhov.android.hive.utils.Constants
import com.gpetuhov.android.hive.utils.TestRepository
import com.gpetuhov.android.hive.utils.dagger.components.DaggerTestAppComponent
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import javax.inject.Inject

class SaveYouTubeInteractorTest {

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
    fun saveYouTubeSuccess() {
        testSaveYouTubeInteractor(true)
    }

    @Test
    fun saveYouTubeError() {
        testSaveYouTubeInteractor(false)
    }

    private fun testSaveYouTubeInteractor(isSuccess: Boolean) {
        (repo as TestRepository).isSuccess = isSuccess

        var errorCounter = 0

        val callback = object : SaveUserPropertyInteractor.Callback {
            override fun onSaveError(errorMessage: String) {
                errorCounter++
                assertEquals(Constants.SAVE_YOUTUBE_ERROR, errorMessage)
            }
        }

        val interactor = SaveYouTubeInteractor(callback)
        interactor.saveYouTube(Constants.DUMMY_YOUTUBE)

        assertEquals(if (isSuccess) Constants.DUMMY_YOUTUBE else "", (repo as TestRepository).youtube)
        assertEquals(if (isSuccess) 0 else 1, errorCounter)
    }
}