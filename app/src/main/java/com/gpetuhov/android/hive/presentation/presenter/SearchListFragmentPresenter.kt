package com.gpetuhov.android.hive.presentation.presenter

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.gpetuhov.android.hive.presentation.view.SearchListFragmentView
import com.gpetuhov.android.hive.util.Constants

@InjectViewState
class SearchListFragmentPresenter : MvpPresenter<SearchListFragmentView>() {

    var queryLatitude = Constants.Map.DEFAULT_LATITUDE
    var queryLongitude = Constants.Map.DEFAULT_LONGITUDE
    var queryRadius = Constants.Map.DEFAULT_RADIUS
    var queryText = ""

    // === Public methods ===

    fun navigateUp() = viewState.navigateUp()
}