package com.gpetuhov.android.hive.interactors

import android.content.Context
import com.gpetuhov.android.hive.application.HiveApp
import com.gpetuhov.android.hive.domain.interactor.SaveOfferInteractor
import com.gpetuhov.android.hive.domain.model.Offer
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
    fun saveOfferSuccess() {
        testSaveOfferInteractor(Constants.DUMMY_OFFER, true)
    }

    @Test
    fun saveOfferError() {
        testSaveOfferInteractor(Constants.DUMMY_OFFER, false)
    }

    @Test
    fun saveOfferEmptyTitle() {
        val offer = Constants.DUMMY_OFFER
        offer.title = ""
        testSaveOfferInteractor(offer, true)
    }

    @Test
    fun saveOfferEmptyDescription() {
        val offer = Constants.DUMMY_OFFER
        offer.description = ""
        testSaveOfferInteractor(offer, true)
    }

    @Test
    fun saveOfferEmptyPhoto() {
        val offer = Constants.DUMMY_OFFER
        offer.photoList.clear()
        testSaveOfferInteractor(offer, true)
    }

    private fun testSaveOfferInteractor(offer: Offer, isSuccess: Boolean) {
        (repo as TestRepository).isSuccess = isSuccess

        var successCounter = 0
        var errorCounter = 0

        val callback = object : SaveOfferInteractor.Callback {
            override fun onSaveOfferSuccess() {
                successCounter++
            }

            override fun onSaveOfferError(errorMessage: String) {
                errorCounter++

                val expectedMessage: String = when {
                    offer.title == "" -> Constants.SAVE_OFFER_TITLE_ERROR
                    offer.description == "" -> Constants.SAVE_OFFER_DESCRIPTION_ERROR
                    offer.photoList.isEmpty() -> Constants.SAVE_OFFER_PHOTO_ERROR
                    else -> Constants.SAVE_OFFER_ERROR
                }

                assertEquals(expectedMessage, errorMessage)
            }
        }

        val interactor = SaveOfferInteractor(callback)
        interactor.saveOffer(offer)

        val finalSuccess = isSuccess && offer.title != "" && offer.description != ""

        assertEquals(finalSuccess, !(repo as TestRepository).offerList.isEmpty())
        assertEquals(if (finalSuccess) 1 else 0, successCounter)
        assertEquals(if (finalSuccess) 0 else 1, errorCounter)
    }
}