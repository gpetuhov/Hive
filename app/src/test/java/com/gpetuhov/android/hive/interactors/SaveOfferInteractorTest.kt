package com.gpetuhov.android.hive.interactors

import android.content.Context
import com.gpetuhov.android.hive.application.HiveApp
import com.gpetuhov.android.hive.domain.interactor.SaveOfferInteractor
import com.gpetuhov.android.hive.domain.repository.Repo
import com.gpetuhov.android.hive.utils.Constants
import com.gpetuhov.android.hive.utils.TestRepository
import com.gpetuhov.android.hive.utils.dagger.components.DaggerTestAppComponent
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import javax.inject.Inject

class SaveOfferInteractorTest {

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
    fun saveServiceSuccess() {
        testSaveServiceInteractor(true)
    }

    @Test
    fun saveServiceError() {
        testSaveServiceInteractor(false)
    }

    private fun testSaveServiceInteractor(isSuccess: Boolean) {
        (repo as TestRepository).isSuccess = isSuccess

        var errorCounter = 0

        val callback = object : SaveOfferInteractor.Callback {
            override fun onSaveOfferError(errorMessage: String) {
                errorCounter++
                assertEquals(Constants.SAVE_OFFER_ERROR, errorMessage)
            }
        }

        val interactor = SaveOfferInteractor(callback)
        interactor.saveOffer(Constants.DUMMY_OFFER)

        assertEquals(if (isSuccess) Constants.DUMMY_OFFER else "", (repo as TestRepository).offer)
        assertEquals(if (isSuccess) 0 else 1, errorCounter)
    }
}