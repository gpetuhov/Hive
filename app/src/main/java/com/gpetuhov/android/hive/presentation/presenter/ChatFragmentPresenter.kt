package com.gpetuhov.android.hive.presentation.presenter

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.gpetuhov.android.hive.application.HiveApp
import com.gpetuhov.android.hive.domain.interactor.SendMessageInteractor
import com.gpetuhov.android.hive.domain.repository.Repo
import com.gpetuhov.android.hive.presentation.view.ChatFragmentView
import com.gpetuhov.android.hive.ui.adapter.MessagesAdapter
import javax.inject.Inject

@InjectViewState
class ChatFragmentPresenter :
    MvpPresenter<ChatFragmentView>(),
    SendMessageInteractor.Callback,
    MessagesAdapter.Callback {

    @Inject lateinit var repo: Repo

    // Two-way data binding is used for this property
    var messageText = ""

    var secondUserUid = ""

    private val sendMessageInteractor = SendMessageInteractor(this)

    init {
        HiveApp.appComponent.inject(this)
    }

    // === SendMessageInteractor.Callback ===

    override fun onSendMessageError(errorMessage: String) = viewState.showToast(errorMessage)

    // === MessagesAdapter.Callback ===

    override fun onMessagesUpdated() = scrollToLastMessage()

    // === Public methods ===

    fun onTextChanged(s: CharSequence?) =
        viewState.sendButtonEnabled(s?.toString()?.trim { it <= ' ' }?.isNotEmpty() ?: false)

    fun sendMessage() {
        sendMessageInteractor.sendMessage(messageText)
        viewState.clearMessageText()
    }

    // We don't have to initialize second user in the repo before opening user details,
    // because second user has already been initialized while opening chat fragment.
    fun openUserDetails() = viewState.openUserDetails()

    fun scrollToLastMessage() = viewState.scrollToLastMessage()

    fun showScrollDownButton() = viewState.showScrollDownButton()

    fun hideScrollDownButton() = viewState.hideScrollDownButton()

    fun navigateUp() = viewState.navigateUp()

    // === Lifecycle methods ===

    fun onResume() {
        repo.startGettingMessagesUpdates()
        repo.startGettingSecondUserUpdates(secondUserUid)
    }

    fun onPause() {
        repo.stopGettingMessagesUpdates()
        repo.stopGettingSecondUserUpdates()
    }
}