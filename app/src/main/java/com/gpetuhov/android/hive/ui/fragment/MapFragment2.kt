package com.gpetuhov.android.hive.ui.fragment

import android.os.Bundle
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import timber.log.Timber

class MapFragment2 : SupportMapFragment(), OnMapReadyCallback {

    lateinit var googleMap: GoogleMap

    override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)

        // Asynchronously get reference to the map
        getMapAsync(this)

    }

    override fun onMapReady(map: GoogleMap) {
        Timber.tag("Map").d("222222222222222222222222222222")

        // When the map is ready, get reference to it
        googleMap = map

        // For showing a move to my location button
        try {
            googleMap.isMyLocationEnabled = true
        } catch (e: SecurityException) {
            Timber.tag("Map").d("Location permission not granted")
        }

        // Enable compass (will show on map rotate)
        googleMap.uiSettings.isCompassEnabled = true

        // Enable zoom buttons
        googleMap.uiSettings.isZoomControlsEnabled = true

        // For dropping a marker at a point on the Map
        val sydney = LatLng(-34.0, 151.0)
        googleMap.addMarker(MarkerOptions().position(sydney).title("Marker Title").snippet("Marker Description"))

        // For zooming automatically to the location of the marker
        val cameraPosition = CameraPosition.Builder().target(sydney).zoom(12.0F).build()
        googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))

        // When the map is loaded, do something
        googleMap.setOnMapLoadedCallback {
            // TODO: do something
        }
    }
}