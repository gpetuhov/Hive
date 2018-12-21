package com.gpetuhov.android.hive.presentation.presenter

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.gpetuhov.android.hive.application.HiveApp
import com.gpetuhov.android.hive.domain.interactor.FavoritesInteractor
import com.gpetuhov.android.hive.domain.repository.Repo
import com.gpetuhov.android.hive.presentation.view.FavoriteOffersFragmentView
import javax.inject.Inject

@InjectViewState
class FavoriteOffersFragmentPresenter : MvpPresenter<FavoriteOffersFragmentView>(), FavoritesInteractor.Callback {

    @Inject lateinit var repo: Repo

    private var favoritesInteractor = FavoritesInteractor(this)

    init {
        HiveApp.appComponent.inject(this)
    }

    // === FavoritesInteractor.Callback ===

    override fun onFavoritesError(errorMessage: String) = viewState.showToast(errorMessage)

    // === Public methods ===

    fun showOfferDetails(userUid: String, offerUid: String) {
        // This is needed to get user details immediately from the already available favorite user list
        repo.initUserDetailsFromFavorites(userUid)
        repo.clearReviews()
        viewState.showOfferDetails(offerUid)
    }

    fun removeOfferFromFavorite(userUid: String, offerUid: String) = favoritesInteractor.favorite(true, userUid, offerUid)
}