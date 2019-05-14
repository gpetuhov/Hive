package com.gpetuhov.android.hive.domain.interactor

import com.google.android.gms.maps.model.LatLng
import com.gpetuhov.android.hive.application.HiveApp
import com.gpetuhov.android.hive.domain.interactor.base.SaveUserPropertyInteractor
import com.gpetuhov.android.hive.util.Constants

class SaveLocationInteractor : SaveUserPropertyInteractor() {

    private var newLocation = Constants.Map.DEFAULT_LOCATION

    init {
        HiveApp.appComponent.inject(this)
    }

    // Do not call this directly, call saveLocation() instead!
    override fun execute() {
        repo.saveUserLocation(newLocation)
    }

    fun saveLocation(newLocation: LatLng) {
        this.newLocation = newLocation
        execute()
    }
}