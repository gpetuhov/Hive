package com.gpetuhov.android.hive.presentation.presenter

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.gpetuhov.android.hive.application.HiveApp
import com.gpetuhov.android.hive.domain.interactor.FavoritesInteractor
import com.gpetuhov.android.hive.domain.repository.Repo
import com.gpetuhov.android.hive.managers.LocationMapManager
import com.gpetuhov.android.hive.presentation.view.OfferDetailsFragmentView
import javax.inject.Inject

@InjectViewState
class OfferDetailsFragmentPresenter : MvpPresenter<OfferDetailsFragmentView>(), FavoritesInteractor.Callback {

    @Inject lateinit var repo: Repo
    @Inject lateinit var locationMapManager: LocationMapManager

    var userUid = ""
    var offerUid = ""
    var offerIsFavorite = false

    private var favoritesInteractor = FavoritesInteractor(this)

    init {
        HiveApp.appComponent.inject(this)
    }

    // === FavoritesInteractor.Callback ===

    override fun onFavoritesError(errorMessage: String) = viewState.showToast(errorMessage)

    // === Public methods ===

    fun navigateUp() = viewState.navigateUp()

    fun openChat() {
        repo.clearMessages()
        viewState.openChat()
    }

    fun openUserDetails() = viewState.openUserDetails()

    fun openPhotos(photoUrlList: MutableList<String>) = viewState.openPhotos(photoUrlList)

    fun openLocation() {
        locationMapManager.resetMapState()  // This is needed to move camera to location
        viewState.openLocation(userUid)
    }

    fun openReviews() = viewState.openReviews(userUid, offerUid)

    fun favorite() = favoritesInteractor.favorite(offerIsFavorite, userUid, offerUid)

    // --- Lifecycle ---

    // This is needed to change user details in the UI if changed on the backend
    fun onResume() = repo.startGettingSecondUserOfferUpdates(userUid)

    fun onPause() = repo.stopGettingSecondUserOfferUpdates()
}