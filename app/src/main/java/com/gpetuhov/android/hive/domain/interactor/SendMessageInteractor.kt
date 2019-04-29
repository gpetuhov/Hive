package com.gpetuhov.android.hive.domain.interactor

import com.gpetuhov.android.hive.application.HiveApp
import com.gpetuhov.android.hive.domain.interactor.base.Interactor
import com.gpetuhov.android.hive.domain.repository.Repo
import com.gpetuhov.android.hive.domain.util.ResultMessages
import javax.inject.Inject

class SendMessageInteractor(private val callback: Callback) : Interactor {

    interface Callback {
        fun onSendMessageError(errorMessage: String)
    }

    @Inject lateinit var repo: Repo
    @Inject lateinit var resultMessages: ResultMessages

    private var messageText = ""

    init {
        HiveApp.appComponent.inject(this)
    }

    // Do not call this directly!
    override fun execute() =
        repo.sendMessage(messageText) { callback.onSendMessageError(resultMessages.getSendMessageErrorMessage()) }

    // Call this method to send message
    fun sendMessage(messageText: String) {
        this.messageText = messageText
        execute()
    }
}