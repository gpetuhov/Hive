package com.gpetuhov.android.hive.interactors

import android.content.Context
import com.gpetuhov.android.hive.application.HiveApp
import com.gpetuhov.android.hive.domain.interactor.SaveCommentInteractor
import com.gpetuhov.android.hive.domain.repository.Repo
import com.gpetuhov.android.hive.utils.Constants
import com.gpetuhov.android.hive.utils.TestRepository
import com.gpetuhov.android.hive.utils.dagger.components.DaggerTestAppComponent
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import javax.inject.Inject

class SaveCommentInteractorTest {

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
    fun saveCommentSuccess() {
        testSaveCommentInteractor(Constants.DUMMY_REVIEW.comment, true)
    }

    @Test
    fun saveCommentError() {
        testSaveCommentInteractor(Constants.DUMMY_REVIEW.comment, false)
    }

    @Test
    fun saveCommentEmptyText() {
        testSaveCommentInteractor("", true)
    }

    private fun testSaveCommentInteractor(commentText: String, isSuccess: Boolean) {
        (repo as TestRepository).isSuccess = isSuccess

        var successCounter = 0
        var errorCounter = 0

        val callback = object : SaveCommentInteractor.Callback {
            override fun onSaveCommentSuccess() {
                successCounter++
            }

            override fun onSaveCommentError(errorMessage: String) {
                errorCounter++

                val expectedMessage: String = when (commentText) {
                    "" -> Constants.SAVE_COMMENT_TEXT_ERROR
                    else -> Constants.SAVE_COMMENT_ERROR
                }

                assertEquals(expectedMessage, errorMessage)
            }
        }

        val interactor = SaveCommentInteractor(callback)
        interactor.saveComment("", "54gh254", commentText)

        val finalSuccess = isSuccess && commentText != ""

        if (finalSuccess) {
            assertEquals(Constants.DUMMY_REVIEW.comment, (repo as TestRepository).commentText)
        }

        assertEquals(if (finalSuccess) 1 else 0, successCounter)
        assertEquals(if (finalSuccess) 0 else 1, errorCounter)
    }
}