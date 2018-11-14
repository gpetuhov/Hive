package com.gpetuhov.android.hive.presentation.presenter

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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

    companion object {
        private const val MIN_SCROLL = 1
        private const val MIN_SCROLL_SUM = 200
    }

    @Inject lateinit var repo: Repo

    // Two-way data binding is used for this property
    var messageText = ""

    var secondUserUid = ""
    lateinit var scrollListener: RecyclerView.OnScrollListener

    private var scrollSum = 0
    var lastScrollPosition = 0

    private val sendMessageInteractor = SendMessageInteractor(this)

    init {
        HiveApp.appComponent.inject(this)
        initScrollListener()
    }

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
        viewState.hideScrollDownButton()
    }

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

    // === Private methods ===

    private fun initScrollListener() {
        scrollListener = object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                // Reset scroll sum if changed scroll direction
                if (scrollSum < 0 && dy > MIN_SCROLL || scrollSum > 0 && dy < -MIN_SCROLL) scrollSum = 0

                scrollSum += dy

                // Show scroll down button on messages list scroll down for more than MIN_SCROLL_SUM,
                // hide on scroll up for more than MIN_SCROLL_SUM.
                if (scrollSum > MIN_SCROLL_SUM) {
                    viewState.showScrollDownButton()
                } else if (scrollSum < -MIN_SCROLL_SUM) {
                    viewState.hideScrollDownButton()
                }
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                scrollSum = 0

                // Hide scroll down button, if reached bottom of the list
                if (!recyclerView.canScrollVertically(1)) {
                    viewState.hideScrollDownButton()
                }

                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    lastScrollPosition = (recyclerView.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
                }
            }
        }
    }

    private fun restoreScrollPosition() = viewState.scrollToPosition(lastScrollPosition)
}