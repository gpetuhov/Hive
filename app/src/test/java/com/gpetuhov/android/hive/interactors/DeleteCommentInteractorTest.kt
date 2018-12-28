package com.gpetuhov.android.hive.interactors

import android.content.Context
import com.gpetuhov.android.hive.application.HiveApp
import com.gpetuhov.android.hive.domain.interactor.DeleteCommentInteractor
import com.gpetuhov.android.hive.domain.repository.Repo
import com.gpetuhov.android.hive.utils.Constants
import com.gpetuhov.android.hive.utils.TestRepository
import com.gpetuhov.android.hive.utils.dagger.components.DaggerTestAppComponent
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import javax.inject.Inject

class DeleteCommentInteractorTest {

    @Inject lateinit var context: Context
    @Inject lateinit var repo: Repo

    @Before
    fun init() {
        val testAppComponent = DaggerTestAppComponent.builder().build()
        testAppComponent.inject(this)

        HiveApp.application = context as HiveApp
        HiveApp.appComponent = testAppComponent

        (repo as TestRepository).commentText = Constants.DUMMY_REVIEW.comment
    }

    @Test
    fun deleteCommentSuccess() {
        testDeleteCommentInteractor(true)
    }

    @Test
    fun deleteCommentError() {
        testDeleteCommentInteractor(false)
    }

    private fun testDeleteCommentInteractor(isSuccess: Boolean) {
        (repo as TestRepository).isSuccess = isSuccess

        var successCounter = 0
        var errorCounter = 0

        val callback = object : DeleteCommentInteractor.Callback {
            override fun onDeleteCommentSuccess() {
                successCounter++
            }

            override fun onDeleteCommentError(errorMessage: String) {
                errorCounter++
                assertEquals(Constants.DELETE_COMMENT_ERROR, errorMessage)
            }
        }

        val interactor = DeleteCommentInteractor(callback)
        interactor.deleteComment("", "54gh254")

        assertEquals(if (isSuccess) "" else Constants.DUMMY_REVIEW.comment, (repo as TestRepository).commentText)
        assertEquals(if (isSuccess) 1 else 0, successCounter)
        assertEquals(if (isSuccess) 0 else 1, errorCounter)
    }
}