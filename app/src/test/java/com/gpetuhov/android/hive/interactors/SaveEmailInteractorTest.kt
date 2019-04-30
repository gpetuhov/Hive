package com.gpetuhov.android.hive.interactors

import android.content.Context
import com.gpetuhov.android.hive.application.HiveApp
import com.gpetuhov.android.hive.domain.interactor.SaveEmailInteractor
import com.gpetuhov.android.hive.domain.interactor.base.SaveUserPropertyInteractor
import com.gpetuhov.android.hive.domain.repository.Repo
import com.gpetuhov.android.hive.utils.Constants
import com.gpetuhov.android.hive.utils.TestRepository
import com.gpetuhov.android.hive.utils.dagger.components.DaggerTestAppComponent
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import javax.inject.Inject

class SaveEmailInteractorTest {

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
    fun saveEmailSuccess() {
        testSaveEmailInteractor(true)
    }

    @Test
    fun saveEmailError() {
        testSaveEmailInteractor(false)
    }

    private fun testSaveEmailInteractor(isSuccess: Boolean) {
        (repo as TestRepository).isSuccess = isSuccess

        var errorCounter = 0

        val callback = object : SaveUserPropertyInteractor.Callback {
            override fun onSaveError(errorMessage: String) {
                errorCounter++
                assertEquals(Constants.SAVE_EMAIL_ERROR, errorMessage)
            }
        }

        val interactor = SaveEmailInteractor(callback)
        interactor.saveEmail(Constants.DUMMY_EMAIL)

        assertEquals(if (isSuccess) Constants.DUMMY_EMAIL else "", (repo as TestRepository).email)
        assertEquals(if (isSuccess) 0 else 1, errorCounter)
    }
}