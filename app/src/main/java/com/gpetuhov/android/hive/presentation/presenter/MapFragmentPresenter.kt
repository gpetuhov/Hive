package com.gpetuhov.android.hive.presentation.presenter

import android.os.Bundle
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.google.android.gms.maps.GoogleMap
import com.gpetuhov.android.hive.application.HiveApp
import com.gpetuhov.android.hive.domain.model.User
import com.gpetuhov.android.hive.domain.repository.Repo
import com.gpetuhov.android.hive.managers.MapManager
import com.gpetuhov.android.hive.presentation.view.MapFragmentView
import javax.inject.Inject

@InjectViewState
class MapFragmentPresenter : MvpPresenter<MapFragmentView>(), MapManager.Callback {

    @Inject lateinit var mapManager: MapManager
    @Inject lateinit var repo: Repo

    // Current query text from search EditText
    // (binded to view with two-way data binding).
    var queryText = ""

    init {
        HiveApp.appComponent.inject(this)
    }

    // === MapManager.Callback ===

    override fun onMinZoom() = viewState.onMinZoom()

    override fun onMaxZoom() = viewState.onMaxZoom()

    override fun onNormalZoom() = viewState.onNormalZoom()

    // === Public methods ===

    fun initMap(map: GoogleMap) = mapManager.initMap(this, map)

    fun updateMarkers(resultList: MutableList<User>) = mapManager.updateMarkers(resultList)

    fun search() {
        viewState.hideKeyboard()
        repo.search(queryText)
    }

    fun cancelSearch() {
        viewState.clearSearch()
        search()
    }

    fun moveToCurrentLocation() = mapManager.moveToCurrentLocation()

    fun zoomIn() = mapManager.zoomIn()

    fun zoomOut() = mapManager.zoomOut()

    // === Lifecycle calls ===

    fun onCreateView(savedInstanceState: Bundle?) {
        mapManager.restoreMapState(savedInstanceState)
        queryText = mapManager.queryText()
    }

    fun onPause() {
        // Save map state here, because onPause() is guaranteed to be called
        mapManager.saveMapState(queryText)
        repo.stopGettingSearchResultUpdates()
    }

    fun onSaveInstanceState(outState: Bundle) = mapManager.saveOutState(outState)
}