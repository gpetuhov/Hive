package com.gpetuhov.android.hive.ui.epoxy.map.models

import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.google.android.gms.maps.MapView
import com.gpetuhov.android.hive.R
import com.gpetuhov.android.hive.ui.epoxy.base.KotlinHolder

@EpoxyModelClass(layout = R.layout.user_offer_map_view)
abstract class MapModel : EpoxyModelWithHolder<MapHolder>() {

    override fun bind(holder: MapHolder) {
        holder.mapView.onCreate(null)

        holder.mapView.getMapAsync { googleMap ->
            googleMap.uiSettings.isMapToolbarEnabled = false

            googleMap.setOnMapClickListener {
                // TODO: implement this
            }
        }
    }
}

class MapHolder : KotlinHolder() {
    val mapView by bind<MapView>(R.id.user_offer_map_view)
}