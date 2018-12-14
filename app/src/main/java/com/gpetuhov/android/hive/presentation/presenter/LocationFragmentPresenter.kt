package com.gpetuhov.android.hive.presentation.presenter

import android.os.Bundle
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.gpetuhov.android.hive.application.HiveApp
import com.gpetuhov.android.hive.domain.repository.Repo
import com.gpetuhov.android.hive.managers.LocationMapManager
import com.gpetuhov.android.hive.presentation.view.LocationFragmentView
import javax.inject.Inject

@InjectViewState
class LocationFragmentPresenter : MvpPresenter<LocationFragmentView>() {

    @Inject lateinit var repo: Repo
    @Inject lateinit var locationMapManager: LocationMapManager

    var userUid = ""

    init {
        HiveApp.appComponent.inject(this)
    }

    // === Public methods ===

    fun initMap(googleMap: GoogleMap) = locationMapManager.initMap(googleMap)

    fun updateLocation(location: LatLng) = locationMapManager.updateLocation(location)

    fun navigateUp() = viewState.navigateUp()

    // --- Lifecycle ---

    fun onCreateView(savedInstanceState: Bundle?) {
        locationMapManager.restoreMapState(savedInstanceState)
    }

    fun onResume() = repo.startGettingSecondUserUpdates(userUid)

    fun onPause() {
        locationMapManager.saveMapState("")
        repo.stopGettingSecondUserUpdates()
    }

    fun onSaveInstanceState(outState: Bundle) = locationMapManager.saveOutState(outState)
}