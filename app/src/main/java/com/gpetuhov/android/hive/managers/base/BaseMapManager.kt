package com.gpetuhov.android.hive.managers.base

import com.google.android.gms.maps.GoogleMap
import com.gpetuhov.android.hive.managers.MapManager
import com.gpetuhov.android.hive.util.Constants
import timber.log.Timber

open class BaseMapManager {

    protected open lateinit var googleMap: GoogleMap

    // === Protected methods ===

    fun initMap(map: GoogleMap, controlsEnabled: Boolean) {
        // When the map is ready, get reference to it
        googleMap = map

        try {
            // Show my location (blue point)
            googleMap.isMyLocationEnabled = true

        } catch (e: SecurityException) {
            Timber.tag("LocationFragment").d("Location permission not granted")
        }

        // Enable compass (will show on map rotate)
        googleMap.uiSettings.isCompassEnabled = true

        // Enable my location button
        googleMap.uiSettings.isMyLocationButtonEnabled = controlsEnabled

        // Enable zoom buttons
        googleMap.uiSettings.isZoomControlsEnabled = controlsEnabled

        // Enable toolbar that opens Google Maps App
        googleMap.uiSettings.isMapToolbarEnabled = controlsEnabled

        // Set minimum and maximum zoom
        googleMap.setMinZoomPreference(Constants.Map.MIN_ZOOM)
        googleMap.setMaxZoomPreference(Constants.Map.MAX_ZOOM)
    }


}