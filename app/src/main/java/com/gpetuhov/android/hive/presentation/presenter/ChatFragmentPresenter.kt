package com.gpetuhov.android.hive.presentation.presenter

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.gpetuhov.android.hive.application.HiveApp
import com.gpetuhov.android.hive.domain.repository.Repo
import com.gpetuhov.android.hive.presentation.view.ChatFragmentView
import javax.inject.Inject

@InjectViewState
class ChatFragmentPresenter : MvpPresenter<ChatFragmentView>() {

    @Inject lateinit var repo: Repo

    // Two-way data binding is used for this property
    var messageText = ""

    var userUid = ""

    init {
        HiveApp.appComponent.inject(this)
    }

    // === Public methods ===

    fun navigateUp() = viewState.navigateUp()

    fun sendMessage() {
        // TODO: refactor error message into message provider
        repo.sendMessage(messageText) { viewState.showToast("Error sending message") }
        viewState.clearMessageText()
    }

    fun onResume() = repo.startGettingMessagesUpdates(userUid)

    fun onPause() = repo.stopGettingMessagesUpdates()

    fun onTextChanged(s: CharSequence?) {
        viewState.sendButtonEnabled(s?.toString()?.trim { it <= ' ' }?.isNotEmpty() ?: false)
    }
}