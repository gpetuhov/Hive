package com.gpetuhov.android.hive.ui.epoxy.map.models

import android.widget.TextView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.LatLng
import com.gpetuhov.android.hive.R
import com.gpetuhov.android.hive.ui.epoxy.base.KotlinHolder
import com.gpetuhov.android.hive.util.Constants
import com.gpetuhov.android.hive.util.addSingleMarker
import com.gpetuhov.android.hive.util.moveCamera

@EpoxyModelClass(layout = R.layout.user_offer_map_view)
abstract class MapModel : EpoxyModelWithHolder<MapHolder>() {

    @EpoxyAttribute lateinit var onClick: () -> Unit

    private var map: GoogleMap? = null

    // If we annotate location with @EpoxyAttribute,
    // then MapModel will be rebind on every location change.
    private var location = Constants.Map.DEFAULT_LOCATION

    private var distance = ""

    private var holder: MapHolder? = null

    // This is called, when the model is bind to view.
    // Here we create map and init it with current location.
    override fun bind(holder: MapHolder) {
        this.holder = holder

        // We need to call this for the map to show up
        holder.mapView.onCreate(null)

        holder.mapView.getMapAsync { googleMap ->
            map = googleMap
            googleMap.uiSettings.isMapToolbarEnabled = false
            googleMap.setOnMapClickListener { onClick() }
            updateMap(location, distance)
        }
    }

    // This is needed to prevent rebinding MapModel on every location change
    // (and so map recreation).
    // If the map is null (model is not bind yet), just update location,
    // otherwise update map.
    fun updateMap(location: LatLng, distance: String) {
        this.location = location
        this.distance = distance

        holder?.distance?.text = distance

        if (map != null) {
            map?.moveCamera(location)
            map?.addSingleMarker(location)
        }
    }
}

class MapHolder : KotlinHolder() {
    val mapView by bind<MapView>(R.id.user_offer_map_view)
    val distance by bind<TextView>(R.id.user_offer_distance)
}