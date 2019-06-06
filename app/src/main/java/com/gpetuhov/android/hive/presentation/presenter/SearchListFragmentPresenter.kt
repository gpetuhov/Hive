package com.gpetuhov.android.hive.presentation.presenter

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.gpetuhov.android.hive.application.HiveApp
import com.gpetuhov.android.hive.domain.interactor.SearchInteractor
import com.gpetuhov.android.hive.domain.repository.Repo
import com.gpetuhov.android.hive.presentation.view.SearchListFragmentView
import com.gpetuhov.android.hive.util.Constants
import javax.inject.Inject

@InjectViewState
class SearchListFragmentPresenter : MvpPresenter<SearchListFragmentView>(), SearchInteractor.Callback {

    @Inject lateinit var repo: Repo

    var queryLatitude = Constants.Map.DEFAULT_LATITUDE
    var queryLongitude = Constants.Map.DEFAULT_LONGITUDE
    var queryRadius = Constants.Map.DEFAULT_RADIUS
    var queryText = ""

    private val searchInteractor = SearchInteractor(this)

    init {
        HiveApp.appComponent.inject(this)
    }

    // === SearchInteractor.Callback ===

    override fun onSearchComplete() = viewState.onSearchComplete()

    // === Public methods ===

    fun navigateUp() = viewState.navigateUp()

    fun search() {
        viewState.onSearchStart()
        searchInteractor.search(queryLatitude, queryLongitude, queryRadius, queryText)
    }

    fun onPause() = repo.stopGettingSearchResultUpdates()
}