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

    @Inject lateinit var repo: Repo

    var secondUserUid = ""

    lateinit var scrollListener: RecyclerView.OnScrollListener

    init {
        HiveApp.appComponent.inject(this)
        initScrollListener()
    }

    // === Public methods ===

    fun getChatArchivePagingOptions(lifecycleOwner: LifecycleOwner) = repo.getChatArchivePagingOptions(lifecycleOwner)

    fun scrollToLastMessage() {
        // TODO
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
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                // TODO
            }
        }
    }
}