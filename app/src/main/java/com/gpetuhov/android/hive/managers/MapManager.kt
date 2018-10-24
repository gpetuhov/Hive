package com.gpetuhov.android.hive.managers

import android.app.Activity
import android.content.Context
import android.os.Bundle
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.ui.IconGenerator
import com.gpetuhov.android.hive.R
import com.gpetuhov.android.hive.application.HiveApp
import com.gpetuhov.android.hive.model.User
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
    }

    @Inject lateinit var context: Context
    @Inject lateinit var locationManager: LocationManager

    private lateinit var googleMap: GoogleMap

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

            locationManager.getLastLocation { location ->
                val zoom = if (location.latitude == DEFAULT_LATITUDE && location.longitude == DEFAULT_LONGITUDE) NO_ZOOM else DEFAULT_ZOOM

                val cameraPosition = CameraPosition.Builder().target(location).zoom(zoom).build()
                googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
            }

        } catch (e: SecurityException) {
            Timber.tag(TAG).d("Location permission not granted")
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
}