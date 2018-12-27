package com.gpetuhov.android.hive.interactors

import android.content.Context
import com.gpetuhov.android.hive.application.HiveApp
import com.gpetuhov.android.hive.domain.interactor.SaveReviewInteractor
import com.gpetuhov.android.hive.domain.repository.Repo
import com.gpetuhov.android.hive.utils.Constants
import com.gpetuhov.android.hive.utils.TestRepository
import com.gpetuhov.android.hive.utils.dagger.components.DaggerTestAppComponent
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import javax.inject.Inject

class SaveReviewInteractorTest {

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
    fun saveReviewSuccess() {
        testSaveReviewInteractor(Constants.DUMMY_REVIEW.text, Constants.DUMMY_REVIEW.rating, true)
    }

    @Test
    fun saveReviewError() {
        testSaveReviewInteractor(Constants.DUMMY_REVIEW.text, Constants.DUMMY_REVIEW.rating, false)
    }

    @Test
    fun saveReviewEmptyText() {
        testSaveReviewInteractor("", Constants.DUMMY_REVIEW.rating, true)
    }

    @Test
    fun saveReviewZeroRating() {
        testSaveReviewInteractor(Constants.DUMMY_REVIEW.text, 0.0F, true)
    }

    private fun testSaveReviewInteractor(reviewText: String, rating: Float, isSuccess: Boolean) {
        (repo as TestRepository).isSuccess = isSuccess

        var successCounter = 0
        var errorCounter = 0

        val callback = object : SaveReviewInteractor.Callback {
            override fun onSaveReviewSuccess() {
                successCounter++
            }

            override fun onSaveReviewError(errorMessage: String) {
                errorCounter++

                val expectedMessage: String = when {
                    reviewText == "" -> Constants.SAVE_REVIEW_TEXT_ERROR
                    rating == 0.0F -> Constants.SAVE_REVIEW_RATING_ERROR
                    else -> Constants.SAVE_REVIEW_ERROR
                }

                assertEquals(expectedMessage, errorMessage)
            }
        }

        val interactor = SaveReviewInteractor(callback)
        interactor.saveReview("", "54gh254", reviewText, rating)

        val finalSuccess = isSuccess && reviewText != "" && rating != 0.0F

        if (finalSuccess) {
            assertEquals(Constants.DUMMY_REVIEW.text, (repo as TestRepository).reviewText)
            assertEquals(Constants.DUMMY_REVIEW.rating, (repo as TestRepository).rating)
        }

        assertEquals(if (finalSuccess) 1 else 0, successCounter)
        assertEquals(if (finalSuccess) 0 else 1, errorCounter)
    }
}