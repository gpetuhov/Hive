package com.gpetuhov.android.hive.presentation.presenter

import androidx.lifecycle.LifecycleOwner
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.gpetuhov.android.hive.application.HiveApp
import com.gpetuhov.android.hive.domain.repository.Repo
import com.gpetuhov.android.hive.presentation.view.ChatArchiveFragmentView
import javax.inject.Inject

@InjectViewState
class ChatArchiveFragmentPresenter : MvpPresenter<ChatArchiveFragmentView>() {

    @Inject lateinit var repo: Repo

    init {
        HiveApp.appComponent.inject(this)
    }

    // === Public methods ===

    fun getChatArchivePagingOptions(lifecycleOwner: LifecycleOwner) = repo.getChatArchivePagingOptions(lifecycleOwner)

    fun navigateUp() = viewState.navigateUp()
}