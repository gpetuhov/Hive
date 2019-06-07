package com.gpetuhov.android.hive.managers.base

import android.os.Bundle
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.gpetuhov.android.hive.managers.LocationManager
import com.gpetuhov.android.hive.util.Constants
import com.gpetuhov.android.hive.util.moveCamera
import timber.log.Timber

open class BaseMapManager {

    companion object {
        private const val LAT = "lat"
        private const val LON = "lon"
        private const val ZOOM = "zoom"
        private const val TILT = "tilt"
        private const val BEARING = "bearing"
        private const val MAPTYPE = "maptype"
        private const val QUERY_TEXT = "queryText"
    }

    protected open lateinit var googleMap: GoogleMap
    protected var mapState: MapState? = null

    // === Public methods ===

    // Save map state into MapManager
    // (MapManager is alive during the whole app lifecycle)
    fun saveMapState(queryText: String) {
        if (::googleMap.isInitialized) {
            mapState = MapState(googleMap.cameraPosition, googleMap.mapType, queryText)
        }
    }

    // Save map state into savedInstanceState
    fun saveOutState(outState: Bundle) {
        // Save map state not only in MapManger itself,
        // but also into savedInstanceState bundle,
        // because Android keeps savedInstanceState bundle on orientation change
        // and if the app gets killed by the OS.
        outState.putDouble(LAT, mapState?.cameraPosition?.target?.latitude ?: Constants.Map.DEFAULT_LATITUDE)
        outState.putDouble(LON, mapState?.cameraPosition?.target?.longitude ?: Constants.Map.DEFAULT_LONGITUDE)
        outState.putFloat(ZOOM, mapState?.cameraPosition?.zoom ?: Constants.Map.MIN_ZOOM)
        outState.putFloat(TILT, mapState?.cameraPosition?.tilt ?: Constants.Map.DEFAULT_TILT)
        outState.putFloat(BEARING, mapState?.cameraPosition?.bearing ?: Constants.Map.DEFAULT_BEARING)
        outState.putInt(MAPTYPE, mapState?.mapType ?: GoogleMap.MAP_TYPE_NORMAL)
        outState.putString(QUERY_TEXT, mapState?.queryText ?: "")
    }

    // Restore map state from savedInstanceState, if exists and contains saved map state.
    // Return saved query text or empty string.
    fun restoreMapState(savedInstanceState: Bundle?): String {
        if (savedInstanceState != null) {
            val latitude = savedInstanceState.getDouble(LAT, Constants.Map.DEFAULT_LATITUDE)
            val longitude = savedInstanceState.getDouble(LON, Constants.Map.DEFAULT_LONGITUDE)

            // If there are no latitude and longitude in the saved state,
            // then there is no saved map state in the bundle, so do not update mapState property.
            if (latitude != Constants.Map.DEFAULT_LATITUDE
                && longitude != Constants.Map.DEFAULT_LONGITUDE) {

                val target = LatLng(latitude, longitude)
                val zoom = savedInstanceState.getFloat(ZOOM, Constants.Map.MIN_ZOOM)
                val bearing = savedInstanceState.getFloat(BEARING, Constants.Map.DEFAULT_BEARING)
                val tilt = savedInstanceState.getFloat(TILT, Constants.Map.DEFAULT_TILT)
                val position = CameraPosition.Builder().target(target).zoom(zoom).tilt(tilt).bearing(bearing).build()

                val mapType = savedInstanceState.getInt(MAPTYPE, GoogleMap.MAP_TYPE_NORMAL)

                val queryText = savedInstanceState.getString(QUERY_TEXT, "")

                mapState = MapState(position, mapType, queryText)
            }
        }

        return mapState?.queryText ?: ""
    }

    fun resetMapState() {
        mapState = null
    }

    // === Protected methods ===

    protected fun initMap(locationManager: LocationManager?, map: GoogleMap, controlsEnabled: Boolean, moveToCurrentLocation: Boolean) {
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

        initCameraPosition(locationManager, moveToCurrentLocation)
    }

    protected fun clearGoogleMap() {
        if (::googleMap.isInitialized) googleMap.clear()
    }

    // === Private methods ===

    private fun initCameraPosition(locationManager: LocationManager?, moveToCurrentLocation: Boolean) {
        // If there is saved map state, move camera to saved camera position,
        // and set saved map type.
        if (mapState != null) {
            moveCamera(mapState?.cameraPosition)
            googleMap.mapType = mapState?.mapType ?: GoogleMap.MAP_TYPE_NORMAL

        } else {
            // Otherwise move camera to current location
            if (moveToCurrentLocation) locationManager?.getLastLocation { location -> googleMap.moveCamera(location) }
        }
    }

    private fun moveCamera(cameraPosition: CameraPosition?) =
        googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))

    // === Inner classes ===

    data class MapState(
        var cameraPosition: CameraPosition,
        var mapType: Int,
        var queryText: String
    )
}