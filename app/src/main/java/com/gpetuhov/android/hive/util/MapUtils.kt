package com.gpetuhov.android.hive.util

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

fun GoogleMap.moveCamera(location: LatLng) {
    val zoom = Constants.Map.getZoomForLocation(location)
    val cameraPosition = CameraPosition.Builder().target(location).zoom(zoom).build()
    moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
}

fun GoogleMap.addSingleMarker(location: LatLng) {
    clear()
    addMarker(
        MarkerOptions()
            .position(location)
            .title("")
    )
}