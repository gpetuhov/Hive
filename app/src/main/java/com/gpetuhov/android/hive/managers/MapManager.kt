package com.gpetuhov.android.hive.managers

import android.app.Activity
import android.os.Bundle
import com.google.android.gms.maps.MapView

class MapManager(locationManagerInput: LocationManager) {

    private val locationManager = locationManagerInput

    // Calling this method forces Google Maps (Google Play Services) to load.
    // So afterwards, when the map is really needed, it will load faster.
    fun initGoogleMaps(activity: Activity, savedInstanceState: Bundle?) {
        val dummyMapView = MapView(activity)
        dummyMapView.onCreate(savedInstanceState)
        dummyMapView.onDestroy()
    }
}