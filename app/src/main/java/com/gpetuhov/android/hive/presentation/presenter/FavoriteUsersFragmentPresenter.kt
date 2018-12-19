package com.gpetuhov.android.hive.presentation.presenter

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.gpetuhov.android.hive.application.HiveApp
import com.gpetuhov.android.hive.domain.interactor.FavoritesInteractor
import com.gpetuhov.android.hive.domain.repository.Repo
import com.gpetuhov.android.hive.presentation.view.FavoriteUsersFragmentView
import javax.inject.Inject

@InjectViewState
class FavoriteUsersFragmentPresenter : MvpPresenter<FavoriteUsersFragmentView>(), FavoritesInteractor.Callback {

    @Inject lateinit var repo: Repo

    private var favoritesInteractor = FavoritesInteractor(this)

    init {
        HiveApp.appComponent.inject(this)
    }

    // === FavoritesInteractor.Callback ===

    override fun onFavoritesError(errorMessage: String) = viewState.showToast(errorMessage)

    // === Public methods ===

    fun showUserDetails(userUid: String) {
        // This is needed to get user details immediately from the already available favorite user list
        repo.initUserDetailsFromFavorites(userUid)
        viewState.showUserDetails()
    }

    fun showOfferDetails(userUid: String, offerUid: String) {
        repo.initUserDetailsFromFavorites(userUid)
        viewState.showOfferDetails(offerUid)
    }

    fun removeUserFromFavorite(userUid: String) = favoritesInteractor.favorite(true, userUid, "")
}