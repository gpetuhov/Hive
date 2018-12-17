package com.gpetuhov.android.hive.managers

import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.gpetuhov.android.hive.managers.base.BaseMapManager
import com.gpetuhov.android.hive.util.addSingleMarker
import com.gpetuhov.android.hive.util.moveCamera

class LocationMapManager : BaseMapManager() {

    fun initMap(map: GoogleMap) = super.initMap(null, map, true, false)

    fun updateLocation(location: LatLng) {
        googleMap.addSingleMarker(location)

        // Move camera to location only if there is no saved state
        if (mapState == null) googleMap.moveCamera(location)
    }
}