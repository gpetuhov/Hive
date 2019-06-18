package com.gpetuhov.android.hive.presentation.presenter

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.gpetuhov.android.hive.application.HiveApp
import com.gpetuhov.android.hive.domain.repository.Repo
import com.gpetuhov.android.hive.presentation.view.ChatArchiveFragmentView
import com.gpetuhov.android.hive.ui.adapter.MessagesAdapter
import javax.inject.Inject

@InjectViewState
class ChatArchiveFragmentPresenter :
    MvpPresenter<ChatArchiveFragmentView>(),
    MessagesAdapter.Callback {

    @Inject lateinit var repo: Repo

    init {
        HiveApp.appComponent.inject(this)
    }

    // === MessagesAdapter.Callback ===

    override fun onMessagesUpdated(isChanged: Boolean) {
        // TODO
    }

    // === Public methods ===

    // --- Lifecycle methods ---

    fun getMessages() = repo.getChatArchiveMessages()

    fun onResume() {
        // TODO
    }

    fun onPause() {
        // TODO
    }

    // --- Navigation ---

    fun navigateUp() = viewState.navigateUp()
}