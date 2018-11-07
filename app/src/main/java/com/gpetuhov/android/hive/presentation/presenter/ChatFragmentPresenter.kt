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

    init {
        HiveApp.appComponent.inject(this)
    }

    // === Public methods ===

    fun navigateUp() = viewState.navigateUp()

    fun sendMessage() {
        // TODO: handle onError here
        repo.sendMessage(messageText) { /* Do nothing */ }
        viewState.clearMessageText()
    }
}