package com.gpetuhov.android.hive.presentation.presenter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.gpetuhov.android.hive.application.HiveApp
import com.gpetuhov.android.hive.domain.interactor.SendMessageInteractor
import com.gpetuhov.android.hive.domain.repository.Repo
import com.gpetuhov.android.hive.managers.NotificationManager
import com.gpetuhov.android.hive.presentation.presenter.base.BaseChatPresenter
import com.gpetuhov.android.hive.presentation.view.ChatFragmentView
import com.gpetuhov.android.hive.ui.adapter.MessagesAdapter
import javax.inject.Inject

@InjectViewState
class ChatFragmentPresenter :
    MvpPresenter<ChatFragmentView>(),
    BaseChatPresenter,
    SendMessageInteractor.Callback,
    MessagesAdapter.Callback {

    @Inject lateinit var repo: Repo
    @Inject lateinit var notificationManager: NotificationManager

    override var lastScrollPosition: Int = 0
    override var scrollSum: Int = 0
    override lateinit var scrollListener: RecyclerView.OnScrollListener

    // Two-way data binding is used for this property
    var messageText = ""

    var secondUserUid = ""
    lateinit var layoutChangeListener: View.OnLayoutChangeListener

    private val sendMessageInteractor = SendMessageInteractor(this)

    init {
        HiveApp.appComponent.inject(this)
        initScrollListener()
        initLayoutChangeListener()
    }

    override fun showScrollDownButton() = viewState.showScrollDownButton()

    override fun hideScrollDownButton() = viewState.hideScrollDownButton()

    // === SendMessageInteractor.Callback ===

    override fun onSendMessageError(errorMessage: String) = viewState.showToast(errorMessage)

    // === MessagesAdapter.Callback ===

    override fun onMessagesUpdated(isChanged: Boolean) =
        if (isChanged) scrollToLastMessage() else restoreScrollPosition()

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

    fun scrollToLastMessage() {
        lastScrollPosition = 0
        viewState.scrollToPosition(0)
        hideScrollDownButton()
    }

    fun openChatArchive() = viewState.openChatArchive()

    fun navigateUp() = viewState.navigateUp()

    // === Lifecycle methods ===

    fun onResume() {
        repo.setChatroomOpen(true)
        repo.startGettingMessagesUpdates()
        repo.startGettingSecondUserChatUpdates(secondUserUid)
    }

    fun onPause() {
        repo.setChatroomOpen(false)
        repo.stopGettingMessagesUpdates()
        repo.stopGettingSecondUserChatUpdates()
    }

    // === Private methods ===

    private fun initLayoutChangeListener() {
        layoutChangeListener =
                View.OnLayoutChangeListener { v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom ->
                    if (bottom != oldBottom) {
                        viewState.scrollToPositionWithOffset(lastScrollPosition)
                    }

                    if (bottom < oldBottom) {
                        // If new screen is smaller, than before,
                        // then keyboard is shown, so hide bottom navigation.
                        viewState.hideBottomNavigation()
                    } else if (bottom > oldBottom) {
                        // If new screen is bigger, than before,
                        // then keyboard is hidden, so show bottom navigation
                        viewState.showBottomNavigation()
                    }
                }
    }

    private fun restoreScrollPosition() = viewState.scrollToPosition(lastScrollPosition)
}