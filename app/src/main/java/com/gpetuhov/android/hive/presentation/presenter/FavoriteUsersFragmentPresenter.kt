package com.gpetuhov.android.hive.presentation.presenter

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.gpetuhov.android.hive.application.HiveApp
import com.gpetuhov.android.hive.domain.repository.Repo
import com.gpetuhov.android.hive.presentation.view.FavoriteUsersFragmentView
import javax.inject.Inject

@InjectViewState
class FavoriteUsersFragmentPresenter : MvpPresenter<FavoriteUsersFragmentView>() {

    @Inject lateinit var repo: Repo

    init {
        HiveApp.appComponent.inject(this)
    }

    // === Public methods ===

    fun showUserDetails(userUid: String) {
        // This is needed to get user details immediately from the already available favorite user list
        repo.initUserDetailsFromFavorites(userUid)
        viewState.showUserDetails()
    }
}