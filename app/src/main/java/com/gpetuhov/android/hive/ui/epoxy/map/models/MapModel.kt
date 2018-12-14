package com.gpetuhov.android.hive.ui.epoxy.map.models

import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.gpetuhov.android.hive.R
import com.gpetuhov.android.hive.ui.epoxy.base.KotlinHolder

@EpoxyModelClass(layout = R.layout.user_offer_map_view)
abstract class MapModel : EpoxyModelWithHolder<MapHolder>() {

    override fun bind(holder: MapHolder) {
    }
}

class MapHolder : KotlinHolder() {
}