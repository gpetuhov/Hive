package com.gpetuhov.android.hive.managers

import android.app.Activity
import android.content.Context
import android.os.Bundle
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.ui.IconGenerator
import com.gpetuhov.android.hive.R
import com.gpetuhov.android.hive.application.HiveApp
import com.gpetuhov.android.hive.model.User
import com.gpetuhov.android.hive.util.Constants
import com.gpetuhov.android.hive.util.Constants.Map.Companion.DEFAULT_LATITUDE
import com.gpetuhov.android.hive.util.Constants.Map.Companion.DEFAULT_LONGITUDE
import com.gpetuhov.android.hive.util.Constants.Map.Companion.DEFAULT_ZOOM
import com.gpetuhov.android.hive.util.Constants.Map.Companion.NO_ZOOM
import timber.log.Timber
import javax.inject.Inject


// Show search results on map
class MapManager {

    companion object {
        private const val TAG = "MapManager"
        private const val LAT = "lat"
        private const val LON = "lon"
        private const val ZOOM = "zoom"
        private const val TILT = "tilt"
        private const val BEARING = "bearing"
        private const val MAPTYPE = "maptype"
    }

    @Inject lateinit var context: Context
    @Inject lateinit var locationManager: LocationManager

    private lateinit var googleMap: GoogleMap
    private var mapState: MapState? = null

    init {
        HiveApp.appComponent.inject(this)
    }

    // Calling this method forces Google Maps (Google Play Services) to load.
    // So afterwards, when the map is really needed, it will load faster.
    fun initGoogleMaps(activity: Activity, savedInstanceState: Bundle?) {
        val dummyMapView = MapView(activity)
        dummyMapView.onCreate(savedInstanceState)
        dummyMapView.onDestroy()
    }

    fun initMap(map: GoogleMap) {
        // When the map is ready, get reference to it
        googleMap = map

        // For showing a move to my location button
        try {
            googleMap.isMyLocationEnabled = true

        } catch (e: SecurityException) {
            Timber.tag(TAG).d("Location permission not granted")
        }

        // If there is saved map state, move camera to saved camera position,
        // and set saved map type.
        if (mapState != null) {
            googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(mapState?.cameraPosition))
            googleMap.mapType = mapState?.mapType ?: GoogleMap.MAP_TYPE_NORMAL

        } else {
            // Otherwise move camera to current location
            locationManager.getLastLocation { location ->
                val zoom = if (location.latitude == DEFAULT_LATITUDE && location.longitude == DEFAULT_LONGITUDE) NO_ZOOM else DEFAULT_ZOOM

                val cameraPosition = CameraPosition.Builder().target(location).zoom(zoom).build()
                googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
            }
        }

        // Enable compass (will show on map rotate)
        googleMap.uiSettings.isCompassEnabled = true

        // Enable zoom buttons
        googleMap.uiSettings.isZoomControlsEnabled = true

        // When the map is loaded, do something
        googleMap.setOnMapLoadedCallback {
            // TODO: do something
        }
    }

    fun updateMarkers(context: Context?, resultList: MutableList<User>) {
        if (context != null) {
            Timber.tag(TAG).d("Updating markers")

            googleMap.clear()

            for (user in resultList) {
                val statusId = if (user.isOnline) R.string.online else R.string.offline
                val status = context.getString(statusId)
                val name = if (user.username != "") user.username else user.name

                val iconGenerator = IconGenerator(context)

                // TODO: restore these lines, when online status is properly detected
                // If user is online, set marker text color to green
//                if (user.isOnline) {
//                    iconGenerator.setTextAppearance(R.style.greenTextStyle)
//                }

//                val iconBitmap = iconGenerator.makeIcon("${name} \n$status")
                val iconBitmap = iconGenerator.makeIcon(name)

                googleMap.addMarker(
                    MarkerOptions()
                        .position(user.location)
                        .icon(BitmapDescriptorFactory.fromBitmap(iconBitmap))
                )
            }
        }
    }

    // Save map state into MapManager
    // (MapManager is alive during the whole app lifecycle)
    fun saveMapState() {
        mapState = MapState(googleMap.cameraPosition, googleMap.mapType)
    }

    // Save map state into savedInstanceState
    fun saveOutState(outState: Bundle) {
        // Save map state not only in MapManger itself,
        // but also into savedInstanceState bundle,
        // because Android keeps savedInstanceState bundle on orientation change
        // and if the app gets killed by the OS.
        outState.putDouble(LAT, mapState?.cameraPosition?.target?.latitude ?: Constants.Map.DEFAULT_LATITUDE)
        outState.putDouble(LON, mapState?.cameraPosition?.target?.longitude ?: Constants.Map.DEFAULT_LONGITUDE)
        outState.putFloat(ZOOM, mapState?.cameraPosition?.zoom ?: Constants.Map.NO_ZOOM)
        outState.putFloat(TILT, mapState?.cameraPosition?.tilt ?: Constants.Map.DEFAULT_TILT)
        outState.putFloat(BEARING, mapState?.cameraPosition?.bearing ?: Constants.Map.DEFAULT_BEARING)
        outState.putInt(MAPTYPE, mapState?.mapType ?: GoogleMap.MAP_TYPE_NORMAL)
    }

    // Restore map state from savedInstanceState, if exists and contains saved map state
    fun restoreMapState(savedInstanceState: Bundle?) {
        if (savedInstanceState != null) {
            val latitude = savedInstanceState.getDouble(LAT, Constants.Map.DEFAULT_LATITUDE)
            val longitude = savedInstanceState.getDouble(LON, Constants.Map.DEFAULT_LONGITUDE)

            // If there are no latitude and longitude in the saved state,
            // then there is no saved map state in the bundle, so do not update mapState property.
            if (latitude != Constants.Map.DEFAULT_LATITUDE
                && longitude != Constants.Map.DEFAULT_LONGITUDE) {

                val target = LatLng(latitude, longitude)
                val zoom = savedInstanceState.getFloat(ZOOM, Constants.Map.NO_ZOOM)
                val bearing = savedInstanceState.getFloat(BEARING, Constants.Map.DEFAULT_BEARING)
                val tilt = savedInstanceState.getFloat(TILT, Constants.Map.DEFAULT_TILT)
                val position = CameraPosition.Builder().target(target).zoom(zoom).tilt(tilt).bearing(bearing).build()

                val mapType = savedInstanceState.getInt(MAPTYPE, GoogleMap.MAP_TYPE_NORMAL)

                mapState = MapState(position, mapType)
            }
        }
    }

    // === Inner classes ===

    data class MapState(
        var cameraPosition: CameraPosition,
        var mapType: Int
    )
}