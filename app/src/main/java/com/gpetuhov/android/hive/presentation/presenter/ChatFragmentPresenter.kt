package com.gpetuhov.android.hive.presentation.presenter

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.gpetuhov.android.hive.application.HiveApp
import com.gpetuhov.android.hive.domain.interactor.SendMessageInteractor
import com.gpetuhov.android.hive.domain.repository.Repo
import com.gpetuhov.android.hive.presentation.view.ChatFragmentView
import javax.inject.Inject

@InjectViewState
class ChatFragmentPresenter : MvpPresenter<ChatFragmentView>(), SendMessageInteractor.Callback {

    @Inject lateinit var repo: Repo

    // Two-way data binding is used for this property
    var messageText = ""

    var userUid = ""

    private val sendMessageInteractor = SendMessageInteractor(this)

    init {
        HiveApp.appComponent.inject(this)
    }

    // === SendMessageInteractor.Callback ===

    override fun onSendMessageError(errorMessage: String) = viewState.showToast(errorMessage)

    // === Public methods ===

    fun onTextChanged(s: CharSequence?) =
        viewState.sendButtonEnabled(s?.toString()?.trim { it <= ' ' }?.isNotEmpty() ?: false)

    fun sendMessage() {
        sendMessageInteractor.sendMessage(messageText)
        viewState.clearMessageText()
    }

    fun navigateUp() = viewState.navigateUp()

    // === Lifecycle methods ===

    fun onResume() = repo.startGettingMessagesUpdates(userUid)

    fun onPause() = repo.stopGettingMessagesUpdates()
}