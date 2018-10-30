package com.gpetuhov.android.hive.interactors

import android.content.Context
import com.gpetuhov.android.hive.application.HiveApp
import com.gpetuhov.android.hive.domain.interactor.SaveVisibilityInteractor
import com.gpetuhov.android.hive.domain.repository.Repo
import com.gpetuhov.android.hive.utils.Constants
import com.gpetuhov.android.hive.utils.TestRepository
import com.gpetuhov.android.hive.utils.dagger.components.DaggerTestAppComponent
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import javax.inject.Inject

class SaveVisibilityInteractorTest {

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
    fun saveVisibilitySuccess() {
        testSaveVisibilityInteractor(true)
    }

    @Test
    fun saveVisibilityError() {
        testSaveVisibilityInteractor(false)
    }

    private fun testSaveVisibilityInteractor(isSuccess: Boolean) {
        (repo as TestRepository).isSuccess = isSuccess

        var errorCounter = 0

        val callback = object : SaveVisibilityInteractor.Callback {
            override fun onSaveVisibilityError(errorMessage: String) {
                errorCounter++
                assertEquals(Constants.SAVE_VISIBILITY_ERROR, errorMessage)
            }
        }

        val interactor = SaveVisibilityInteractor(callback)
        interactor.saveVisibility(true)

        assertEquals(isSuccess, (repo as TestRepository).isVisible)
        assertEquals(if (isSuccess) 0 else 1, errorCounter)
    }
}