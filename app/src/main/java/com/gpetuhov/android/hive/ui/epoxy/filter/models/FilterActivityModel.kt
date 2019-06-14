package com.gpetuhov.android.hive.ui.epoxy.filter.models

import android.widget.RadioButton
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.gpetuhov.android.hive.R
import com.gpetuhov.android.hive.ui.epoxy.base.KotlinHolder
import com.gpetuhov.android.hive.ui.epoxy.base.bind

@EpoxyModelClass(layout = R.layout.filter_activity_view)
abstract class FilterActivityModel : EpoxyModelWithHolder<FilterActivityHolder>() {

    @EpoxyAttribute var anyActivity = false
    @EpoxyAttribute lateinit var onAnyActivityClick: () -> Unit

    @EpoxyAttribute var still = false
    @EpoxyAttribute lateinit var onStillClick: () -> Unit

    @EpoxyAttribute var walking = false
    @EpoxyAttribute lateinit var onWalkingClick: () -> Unit

    @EpoxyAttribute var running = false
    @EpoxyAttribute lateinit var onRunningClick: () -> Unit

    @EpoxyAttribute var bicycle = false
    @EpoxyAttribute lateinit var onBicycleClick: () -> Unit

    @EpoxyAttribute var vehicle = false
    @EpoxyAttribute lateinit var onVehicleClick: () -> Unit

    override fun bind(holder: FilterActivityHolder) {
        holder.anyActivity.bind(anyActivity) { onAnyActivityClick() }
        holder.still.bind(still) { onStillClick() }
        holder.walking.bind(walking) { onWalkingClick() }
        holder.running.bind(running) { onRunningClick() }
        holder.bicycle.bind(bicycle) { onBicycleClick() }
        holder.vehicle.bind(vehicle) { onVehicleClick() }
    }
}

class FilterActivityHolder : KotlinHolder() {
    val anyActivity by bind<RadioButton>(R.id.filter_activity_any)
    val still by bind<RadioButton>(R.id.filter_activity_still)
    val walking by bind<RadioButton>(R.id.filter_activity_walking)
    val running by bind<RadioButton>(R.id.filter_activity_running)
    val bicycle by bind<RadioButton>(R.id.filter_activity_bicycle)
    val vehicle by bind<RadioButton>(R.id.filter_activity_vehicle)
}