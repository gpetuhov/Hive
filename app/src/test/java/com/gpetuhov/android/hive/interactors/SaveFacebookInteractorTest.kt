package com.gpetuhov.android.hive.interactors

import android.content.Context
import com.gpetuhov.android.hive.application.HiveApp
import com.gpetuhov.android.hive.domain.interactor.SaveFacebookInteractor
import com.gpetuhov.android.hive.domain.repository.Repo
import com.gpetuhov.android.hive.utils.Constants
import com.gpetuhov.android.hive.utils.TestRepository
import com.gpetuhov.android.hive.utils.dagger.components.DaggerTestAppComponent
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import javax.inject.Inject

class SaveFacebookInteractorTest {

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
    fun saveFacebookSuccess() {
        testSaveFacebookInteractor(true)
    }

    @Test
    fun saveFacebookError() {
        testSaveFacebookInteractor(false)
    }

    private fun testSaveFacebookInteractor(isSuccess: Boolean) {
        (repo as TestRepository).isSuccess = isSuccess

        var errorCounter = 0

        val callback = object : SaveFacebookInteractor.Callback {
            override fun onSaveFacebookError(errorMessage: String) {
                errorCounter++
                assertEquals(Constants.SAVE_FACEBOOK_ERROR, errorMessage)
            }
        }

        val interactor = SaveFacebookInteractor(callback)
        interactor.saveFacebook(Constants.DUMMY_FACEBOOK)

        assertEquals(if (isSuccess) Constants.DUMMY_FACEBOOK else "", (repo as TestRepository).facebook)
        assertEquals(if (isSuccess) 0 else 1, errorCounter)
    }
}