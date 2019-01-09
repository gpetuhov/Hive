package com.gpetuhov.android.hive.interactors

import android.content.Context
import com.gpetuhov.android.hive.application.HiveApp
import com.gpetuhov.android.hive.domain.interactor.SavePhoneInteractor
import com.gpetuhov.android.hive.domain.interactor.SaveUsernameInteractor
import com.gpetuhov.android.hive.domain.repository.Repo
import com.gpetuhov.android.hive.utils.Constants
import com.gpetuhov.android.hive.utils.TestRepository
import com.gpetuhov.android.hive.utils.dagger.components.DaggerTestAppComponent
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import javax.inject.Inject

class SavePhoneInteractorTest {

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
    fun savePhoneSuccess() {
        testSavePhoneInteractor(true)
    }

    @Test
    fun savePhoneError() {
        testSavePhoneInteractor(false)
    }

    private fun testSavePhoneInteractor(isSuccess: Boolean) {
        (repo as TestRepository).isSuccess = isSuccess

        var errorCounter = 0

        val callback = object : SavePhoneInteractor.Callback {
            override fun onSavePhoneError(errorMessage: String) {
                errorCounter++
                assertEquals(Constants.SAVE_PHONE_ERROR, errorMessage)
            }
        }

        val interactor = SavePhoneInteractor(callback)
        interactor.savePhone(Constants.DUMMY_PHONE)

        assertEquals(if (isSuccess) Constants.DUMMY_PHONE else "", (repo as TestRepository).phone)
        assertEquals(if (isSuccess) 0 else 1, errorCounter)
    }
}