package com.gpetuhov.android.hive.presentation.presenter

import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.gpetuhov.android.hive.application.HiveApp
import com.gpetuhov.android.hive.domain.repository.Repo
import com.gpetuhov.android.hive.presentation.view.ChatArchiveFragmentView
import javax.inject.Inject

@InjectViewState
class ChatArchiveFragmentPresenter : MvpPresenter<ChatArchiveFragmentView>() {

    companion object {
        private const val MIN_SCROLL = 1
        private const val MIN_SCROLL_SUM = 200
    }

    @Inject lateinit var repo: Repo

    var secondUserUid = ""

    lateinit var scrollListener: RecyclerView.OnScrollListener

    private var scrollSum = 0

    init {
        HiveApp.appComponent.inject(this)
        initScrollListener()
    }

    // === Public methods ===

    fun getChatArchivePagingOptions(lifecycleOwner: LifecycleOwner) = repo.getChatArchivePagingOptions(lifecycleOwner)

    fun scrollDown() {
        viewState.scrollDown()
        viewState.hideScrollDownButton()
    }

    // --- Navigation ---

    fun openUserDetails() = viewState.openUserDetails()

    fun navigateUp() = viewState.navigateUp()

    // --- Lifecycle methods ---

    fun onResume() = repo.startGettingSecondUserChatArchiveUpdates(secondUserUid)

    fun onPause() = repo.stopGettingSecondUserChatArchiveUpdates()

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
            }
        }
    }
}