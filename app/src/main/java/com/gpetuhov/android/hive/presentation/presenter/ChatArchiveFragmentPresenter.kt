package com.gpetuhov.android.hive.presentation.presenter

import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.gpetuhov.android.hive.application.HiveApp
import com.gpetuhov.android.hive.domain.repository.Repo
import com.gpetuhov.android.hive.presentation.presenter.base.BaseChatPresenter
import com.gpetuhov.android.hive.presentation.view.ChatArchiveFragmentView
import javax.inject.Inject

@InjectViewState
class ChatArchiveFragmentPresenter : MvpPresenter<ChatArchiveFragmentView>(), BaseChatPresenter {

    override var lastScrollPosition: Int = 0
    override var scrollSum: Int = 0
    override lateinit var scrollListener: RecyclerView.OnScrollListener

    @Inject lateinit var repo: Repo

    var secondUserUid = ""

    init {
        HiveApp.appComponent.inject(this)
        initScrollListener()
    }

    override fun showScrollDownButton() = viewState.showScrollDownButton()

    override fun hideScrollDownButton() = viewState.hideScrollDownButton()

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
}