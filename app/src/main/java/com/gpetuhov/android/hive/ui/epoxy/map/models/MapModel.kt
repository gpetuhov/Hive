package com.gpetuhov.android.hive.ui.epoxy.map.models

import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.gpetuhov.android.hive.R
import com.gpetuhov.android.hive.ui.epoxy.base.KotlinHolder
import com.gpetuhov.android.hive.util.Constants

@EpoxyModelClass(layout = R.layout.user_offer_map_view)
abstract class MapModel : EpoxyModelWithHolder<MapHolder>() {

    @EpoxyAttribute lateinit var onClick: () -> Unit

    private var map: GoogleMap? = null
    private var location = Constants.Map.DEFAULT_LOCATION

    override fun bind(holder: MapHolder) {
        // We need to call this for the map to show up
        holder.mapView.onCreate(null)

        holder.mapView.getMapAsync { googleMap ->
            map = googleMap
            googleMap.uiSettings.isMapToolbarEnabled = false
            googleMap.setOnMapClickListener { onClick() }
            updateMap(location)
        }
    }

    fun updateMap(location: LatLng) {
        this.location = location

        if (map != null) {
            val zoom =
                if (location.latitude == Constants.Map.DEFAULT_LATITUDE && location.longitude == Constants.Map.DEFAULT_LONGITUDE) Constants.Map.MIN_ZOOM else Constants.Map.DEFAULT_ZOOM

            val cameraPosition = CameraPosition.Builder().target(location).zoom(zoom).build()
            map?.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))

            map?.clear()

            map?.addMarker(
                MarkerOptions()
                    .position(location)
                    .title("")
            )
        }
    }
}

class MapHolder : KotlinHolder() {
    val mapView by bind<MapView>(R.id.user_offer_map_view)
}