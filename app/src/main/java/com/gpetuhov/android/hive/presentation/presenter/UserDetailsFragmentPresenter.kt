package com.gpetuhov.android.hive.presentation.presenter

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.gpetuhov.android.hive.application.HiveApp
import com.gpetuhov.android.hive.domain.repository.Repo
import com.gpetuhov.android.hive.managers.LocationMapManager
import com.gpetuhov.android.hive.presentation.view.UserDetailsFragmentView
import javax.inject.Inject

@InjectViewState
class UserDetailsFragmentPresenter : MvpPresenter<UserDetailsFragmentView>() {

    @Inject lateinit var repo: Repo
    @Inject lateinit var locationMapManager: LocationMapManager

    var userUid = ""
    var userIsFavorite = false

    init {
        HiveApp.appComponent.inject(this)
    }

    // === Public methods ===

    fun navigateUp() = viewState.navigateUp()

    fun openChat() {
        repo.clearMessages()
        viewState.openChat()
    }

    fun openOffer(offerUid: String) = viewState.openOffer(offerUid)

    fun openPhotos(photoUrlList: MutableList<String>) = viewState.openPhotos(photoUrlList)

    fun openLocation() {
        locationMapManager.resetMapState()  // This is needed to move camera to location
        viewState.openLocation(userUid)
    }

    fun favorite() {
        // TODO: move this into interactor
        // TODO: handle error (show toast)
        repo.addFavorite(userUid, "") { /* Do nothing */ }
    }

    // --- Lifecycle ---

    // This is needed to change user details in the UI if changed on the backend
    fun onResume() = repo.startGettingSecondUserUpdates(userUid)

    fun onPause() = repo.stopGettingSecondUserUpdates()
}