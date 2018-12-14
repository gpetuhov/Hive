package com.gpetuhov.android.hive.presentation.presenter

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.google.android.gms.maps.model.LatLng
import com.gpetuhov.android.hive.application.HiveApp
import com.gpetuhov.android.hive.domain.repository.Repo
import com.gpetuhov.android.hive.presentation.view.LocationFragmentView
import javax.inject.Inject

@InjectViewState
class LocationFragmentPresenter : MvpPresenter<LocationFragmentView>() {

    @Inject lateinit var repo: Repo

    var userUid = ""

    init {
        HiveApp.appComponent.inject(this)
    }

    // === Public methods ===

    fun updateLocation(location: LatLng) {
        // TODO: implement
    }

    fun navigateUp() = viewState.navigateUp()

    // --- Lifecycle ---

    fun onResume() = repo.startGettingSecondUserUpdates(userUid)

    fun onPause() = repo.stopGettingSecondUserUpdates()
}