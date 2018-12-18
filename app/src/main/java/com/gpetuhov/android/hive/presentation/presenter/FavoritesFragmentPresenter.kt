package com.gpetuhov.android.hive.presentation.presenter

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.gpetuhov.android.hive.application.HiveApp
import com.gpetuhov.android.hive.domain.repository.Repo
import com.gpetuhov.android.hive.presentation.view.FavoritesFragmentView
import javax.inject.Inject

@InjectViewState
class FavoritesFragmentPresenter : MvpPresenter<FavoritesFragmentView>() {

    @Inject lateinit var repo: Repo

    init {
        HiveApp.appComponent.inject(this)
    }

    // === Public methods ===

    fun refreshFavorites() {
        viewState.showProgress()
        repo.loadFavorites { viewState.hideProgress() }
    }
}