package com.gpetuhov.android.hive.ui.epoxy.user.details.models

import android.widget.TextView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.gpetuhov.android.hive.R
import com.gpetuhov.android.hive.ui.epoxy.base.KotlinHolder

@EpoxyModelClass(layout = R.layout.user_offer_location_view)
abstract class LocationModel : EpoxyModelWithHolder<LocationHolder>() {

    @EpoxyAttribute lateinit var distance: String

    override fun bind(holder: LocationHolder) {
        holder.distance.text = distance
    }
}

class LocationHolder : KotlinHolder() {
    val distance by bind<TextView>(R.id.user_offer_distance)
}