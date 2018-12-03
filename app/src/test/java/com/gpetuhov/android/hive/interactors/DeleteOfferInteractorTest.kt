package com.gpetuhov.android.hive.interactors

import android.content.Context
import com.gpetuhov.android.hive.application.HiveApp
import com.gpetuhov.android.hive.domain.interactor.DeleteOfferInteractor
import com.gpetuhov.android.hive.domain.repository.Repo
import com.gpetuhov.android.hive.utils.Constants
import com.gpetuhov.android.hive.utils.TestRepository
import com.gpetuhov.android.hive.utils.dagger.components.DaggerTestAppComponent
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import javax.inject.Inject

class DeleteOfferInteractorTest {

    @Inject lateinit var context: Context
    @Inject lateinit var repo: Repo

    @Before
    fun init() {
        val testAppComponent = DaggerTestAppComponent.builder().build()
        testAppComponent.inject(this)

        HiveApp.application = context as HiveApp
        HiveApp.appComponent = testAppComponent

        (repo as TestRepository).offerList.add(Constants.DUMMY_OFFER)
    }

    @Test
    fun deleteOfferSuccess() {
        testDeleteOfferInteractor(true)
    }

    @Test
    fun deleteOfferError() {
        testDeleteOfferInteractor(false)
    }

    private fun testDeleteOfferInteractor(isSuccess: Boolean) {
        (repo as TestRepository).isSuccess = isSuccess

        var successCounter = 0
        var errorCounter = 0

        val callback = object : DeleteOfferInteractor.Callback {
            override fun onDeleteOfferSuccess() {
                successCounter++
            }

            override fun onDeleteOfferError(errorMessage: String) {
                errorCounter++
                assertEquals(Constants.DELETE_OFFER_ERROR, errorMessage)
            }
        }

        val interactor = DeleteOfferInteractor(callback)
        interactor.deleteOffer(Constants.DUMMY_OFFER.uid)

        assertEquals(isSuccess, (repo as TestRepository).offerList.isEmpty())
        assertEquals(if (isSuccess) 1 else 0, successCounter)
        assertEquals(if (isSuccess) 0 else 1, errorCounter)
    }
}