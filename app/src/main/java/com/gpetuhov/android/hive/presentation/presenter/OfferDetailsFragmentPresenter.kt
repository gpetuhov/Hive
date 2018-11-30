package com.gpetuhov.android.hive.presentation.presenter

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.gpetuhov.android.hive.application.HiveApp
import com.gpetuhov.android.hive.domain.repository.Repo
import com.gpetuhov.android.hive.presentation.view.OfferDetailsFragmentView
import javax.inject.Inject

@InjectViewState
class OfferDetailsFragmentPresenter : MvpPresenter<OfferDetailsFragmentView>() {

    @Inject lateinit var repo: Repo

    var userUid = ""

    init {
        HiveApp.appComponent.inject(this)
    }

    // === Public methods ===

    fun navigateUp() = viewState.navigateUp()

    fun openChat() {
        repo.clearMessages()
        viewState.openChat()
    }

    fun openUserDetails() {
        // TODO: implement this
    }

    // --- Lifecycle ---

    // This is needed to change user details in the UI if changed on the backend
    fun onResume() = repo.startGettingSecondUserUpdates(userUid)

    fun onPause() = repo.stopGettingSecondUserUpdates()
}