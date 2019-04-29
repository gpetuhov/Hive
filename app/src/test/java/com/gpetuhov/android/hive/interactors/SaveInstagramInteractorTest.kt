package com.gpetuhov.android.hive.interactors

import android.content.Context
import com.gpetuhov.android.hive.application.HiveApp
import com.gpetuhov.android.hive.domain.interactor.SaveInstagramInteractor
import com.gpetuhov.android.hive.domain.repository.Repo
import com.gpetuhov.android.hive.utils.Constants
import com.gpetuhov.android.hive.utils.TestRepository
import com.gpetuhov.android.hive.utils.dagger.components.DaggerTestAppComponent
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import javax.inject.Inject

class SaveInstagramInteractorTest {

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
    fun saveInstagramSuccess() {
        testSaveInstagramInteractor(true)
    }

    @Test
    fun saveInstagramError() {
        testSaveInstagramInteractor(false)
    }

    private fun testSaveInstagramInteractor(isSuccess: Boolean) {
        (repo as TestRepository).isSuccess = isSuccess

        var errorCounter = 0

        val callback = object : SaveInstagramInteractor.Callback {
            override fun onSaveInstagramError(errorMessage: String) {
                errorCounter++
                assertEquals(Constants.SAVE_INSTAGRAM_ERROR, errorMessage)
            }
        }

        val interactor = SaveInstagramInteractor(callback)
        interactor.saveInstagram(Constants.DUMMY_INSTAGRAM)

        assertEquals(if (isSuccess) Constants.DUMMY_INSTAGRAM else "", (repo as TestRepository).instagram)
        assertEquals(if (isSuccess) 0 else 1, errorCounter)
    }
}