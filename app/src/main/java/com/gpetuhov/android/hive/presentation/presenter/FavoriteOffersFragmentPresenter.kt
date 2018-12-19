package com.gpetuhov.android.hive.presentation.presenter

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.gpetuhov.android.hive.application.HiveApp
import com.gpetuhov.android.hive.domain.repository.Repo
import com.gpetuhov.android.hive.presentation.view.FavoriteOffersFragmentView
import javax.inject.Inject

@InjectViewState
class FavoriteOffersFragmentPresenter : MvpPresenter<FavoriteOffersFragmentView>() {

    @Inject lateinit var repo: Repo

    init {
        HiveApp.appComponent.inject(this)
    }

    // === Public methods ===

    fun showOfferDetails(userUid: String, offerUid: String) {
        // This is needed to get user details immediately from the already available favorite user list
        repo.initUserDetailsFromFavorites(userUid)
        viewState.showOfferDetails(offerUid)
    }
}