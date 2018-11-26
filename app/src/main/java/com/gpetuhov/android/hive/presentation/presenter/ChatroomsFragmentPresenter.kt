package com.gpetuhov.android.hive.presentation.presenter

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.gpetuhov.android.hive.application.HiveApp
import com.gpetuhov.android.hive.domain.repository.Repo
import com.gpetuhov.android.hive.managers.NotificationManager
import com.gpetuhov.android.hive.presentation.view.ChatroomsFragmentView
import com.gpetuhov.android.hive.ui.adapter.ChatroomsAdapter
import javax.inject.Inject

@InjectViewState
class ChatroomsFragmentPresenter : MvpPresenter<ChatroomsFragmentView>(), ChatroomsAdapter.Callback {

    @Inject lateinit var repo: Repo
    @Inject lateinit var notificationManager: NotificationManager

    init {
        HiveApp.appComponent.inject(this)
    }

    // === ChatroomsAdapter.Callback ===

    override fun openChat(secondUserUid: String, secondUserName: String, secondUserPicUrl: String) {
        // Before opening the chat, we must initialize the second user with uid and name
        // from the chosen chatroom and clear messages.
        repo.initSecondUser(secondUserUid, secondUserName, secondUserPicUrl)
        repo.clearMessages()
        viewState.openChat()
    }

    // === Lifecycle methods ===

    fun onResume() {
        repo.setChatroomListOpen(true)
        repo.startGettingChatroomsUpdates()
    }

    fun onPause() {
        repo.setChatroomListOpen(false)
        repo.stopGettingChatroomsUpdates()
    }
}