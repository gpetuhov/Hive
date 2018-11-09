package com.gpetuhov.android.hive.presentation.presenter

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.gpetuhov.android.hive.application.HiveApp
import com.gpetuhov.android.hive.domain.repository.Repo
import com.gpetuhov.android.hive.presentation.view.DetailsFragmentView
import javax.inject.Inject

@InjectViewState
class DetailsFragmentPresenter : MvpPresenter<DetailsFragmentView>() {

    @Inject lateinit var repo: Repo

    init {
        HiveApp.appComponent.inject(this)
    }

    // === Public methods ===

    fun navigateUp() = viewState.navigateUp()

    fun openChat() = viewState.openChat()

    // This is needed to change user details in the UI if changed on the backend
    fun onResume(uid: String) = repo.startGettingSecondUserUpdates(uid)

    fun onPause() = repo.stopGettingSecondUserUpdates()
}