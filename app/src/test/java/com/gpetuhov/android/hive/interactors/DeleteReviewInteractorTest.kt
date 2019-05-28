package com.gpetuhov.android.hive.interactors

import android.content.Context
import com.gpetuhov.android.hive.application.HiveApp
import com.gpetuhov.android.hive.domain.interactor.DeleteReviewInteractor
import com.gpetuhov.android.hive.domain.repository.Repo
import com.gpetuhov.android.hive.utils.Constants
import com.gpetuhov.android.hive.utils.TestRepository
import com.gpetuhov.android.hive.utils.dagger.components.DaggerTestAppComponent
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import javax.inject.Inject

class DeleteReviewInteractorTest {

    @Inject lateinit var context: Context
    @Inject lateinit var repo: Repo

    @Before
    fun init() {
        val testAppComponent = DaggerTestAppComponent.builder().build()
        testAppComponent.inject(this)

        HiveApp.application = context as HiveApp
        HiveApp.appComponent = testAppComponent

        (repo as TestRepository).reviewList.add(Constants.DUMMY_REVIEW)
    }

    @Test
    fun deleteReviewSuccess() {
        testDeleteReviewInteractor(true)
    }

    @Test
    fun deleteReviewError() {
        testDeleteReviewInteractor(false)
    }

    private fun testDeleteReviewInteractor(isSuccess: Boolean) {
        (repo as TestRepository).isSuccess = isSuccess

        var successCounter = 0
        var errorCounter = 0

        val callback = object : DeleteReviewInteractor.Callback {
            override fun onDeleteReviewSuccess() {
                successCounter++
            }

            override fun onDeleteReviewError(errorMessage: String) {
                errorCounter++
                assertEquals(Constants.DELETE_REVIEW_ERROR, errorMessage)
            }
        }

        val interactor = DeleteReviewInteractor(callback)
        interactor.deleteReview(Constants.DUMMY_REVIEW.offerUid, Constants.DUMMY_REVIEW.uid, false)

        assertEquals(isSuccess, (repo as TestRepository).reviewList.isEmpty())
        assertEquals(if (isSuccess) 1 else 0, successCounter)
        assertEquals(if (isSuccess) 0 else 1, errorCounter)
    }
}