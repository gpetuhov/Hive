package com.gpetuhov.android.hive.interactors

import android.content.Context
import com.gpetuhov.android.hive.application.HiveApp
import com.gpetuhov.android.hive.domain.interactor.SaveSkypeInteractor
import com.gpetuhov.android.hive.domain.interactor.base.SaveUserPropertyInteractor
import com.gpetuhov.android.hive.domain.repository.Repo
import com.gpetuhov.android.hive.utils.Constants
import com.gpetuhov.android.hive.utils.TestRepository
import com.gpetuhov.android.hive.utils.dagger.components.DaggerTestAppComponent
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import javax.inject.Inject

class SaveSkypeInteractorTest {

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
    fun saveSkypeSuccess() {
        testSaveSkypeInteractor(true)
    }

    @Test
    fun saveSkypeError() {
        testSaveSkypeInteractor(false)
    }

    private fun testSaveSkypeInteractor(isSuccess: Boolean) {
        (repo as TestRepository).isSuccess = isSuccess

        var errorCounter = 0

        val callback = object : SaveUserPropertyInteractor.Callback {
            override fun onSaveError(errorMessage: String) {
                errorCounter++
                assertEquals(Constants.SAVE_SKYPE_ERROR, errorMessage)
            }
        }

        val interactor = SaveSkypeInteractor(callback)
        interactor.saveSkype(Constants.DUMMY_SKYPE)

        assertEquals(if (isSuccess) Constants.DUMMY_SKYPE else "", (repo as TestRepository).skype)
        assertEquals(if (isSuccess) 0 else 1, errorCounter)
    }
}