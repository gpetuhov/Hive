package com.gpetuhov.android.hive.interactors

import android.content.Context
import com.gpetuhov.android.hive.application.HiveApp
import com.gpetuhov.android.hive.domain.interactor.SaveUsernameInteractor
import com.gpetuhov.android.hive.domain.repository.Repo
import com.gpetuhov.android.hive.utils.Constants
import com.gpetuhov.android.hive.utils.TestRepository
import com.gpetuhov.android.hive.utils.dagger.components.DaggerTestAppComponent
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import javax.inject.Inject

class SaveUsernameInteractorTest {

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
    fun saveUsernameSuccess() {
        testSaveUsernameInteractor(true)
    }

    @Test
    fun saveUsernameError() {
        testSaveUsernameInteractor(false)
    }

    private fun testSaveUsernameInteractor(isSuccess: Boolean) {
        (repo as TestRepository).isSuccess = isSuccess

        var errorCounter = 0

        val callback = object : SaveUsernameInteractor.Callback {
            override fun onSaveUsernameError(errorMessage: String) {
                errorCounter++
                assertEquals(Constants.SAVE_USERNAME_ERROR, errorMessage)
            }
        }

        val interactor = SaveUsernameInteractor(callback)
        interactor.saveUsername(Constants.DUMMY_USERNAME)

        assertEquals(if (isSuccess) Constants.DUMMY_USERNAME else "", (repo as TestRepository).username)
        assertEquals(if (isSuccess) 0 else 1, errorCounter)
    }
}