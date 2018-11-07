package com.gpetuhov.android.hive.interactors

import android.content.Context
import com.gpetuhov.android.hive.application.HiveApp
import com.gpetuhov.android.hive.domain.interactor.SendMessageInteractor
import com.gpetuhov.android.hive.domain.repository.Repo
import com.gpetuhov.android.hive.utils.Constants
import com.gpetuhov.android.hive.utils.TestRepository
import com.gpetuhov.android.hive.utils.dagger.components.DaggerTestAppComponent
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import javax.inject.Inject

class SendMessageInteractorTest {

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
    fun sendMessageSuccess() {
        testSendMessageInteractor(true)
    }

    @Test
    fun sendMessageError() {
        testSendMessageInteractor(false)
    }

    private fun testSendMessageInteractor(isSuccess: Boolean) {
        (repo as TestRepository).isSuccess = isSuccess

        var errorCounter = 0

        val callback = object : SendMessageInteractor.Callback {
            override fun onSendMessageError(errorMessage: String) {
                errorCounter++
                assertEquals(Constants.SEND_MESSAGE_ERROR, errorMessage)
            }
        }

        val interactor = SendMessageInteractor(callback)
        interactor.sendMessage(Constants.DUMMY_MESSAGE_TEXT)

        assertEquals(if (isSuccess) Constants.DUMMY_MESSAGE_TEXT else "", (repo as TestRepository).messageText)
        assertEquals(if (isSuccess) 0 else 1, errorCounter)
    }
}