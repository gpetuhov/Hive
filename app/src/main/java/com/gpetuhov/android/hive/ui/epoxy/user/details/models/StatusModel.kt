package com.gpetuhov.android.hive.ui.epoxy.user.details.models

import android.view.View
import android.widget.TextView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.gpetuhov.android.hive.R
import com.gpetuhov.android.hive.ui.epoxy.base.KotlinHolder
import com.gpetuhov.android.hive.util.setVisible

@EpoxyModelClass(layout = R.layout.user_details_status_view)
abstract class StatusModel : EpoxyModelWithHolder<UserDetailsStatusHolder>() {

    @EpoxyAttribute lateinit var status: String
    @EpoxyAttribute var statusVisible = true
    @EpoxyAttribute lateinit var onStatusClick: () -> Unit

    @EpoxyAttribute var activitySeparatorVisible = false

    @EpoxyAttribute var stillVisible = false
    @EpoxyAttribute lateinit var onStillClick: () -> Unit

    @EpoxyAttribute var walkVisible = false
    @EpoxyAttribute lateinit var onWalkClick: () -> Unit

    @EpoxyAttribute var runVisible = false
    @EpoxyAttribute lateinit var onRunClick: () -> Unit

    @EpoxyAttribute var bicycleVisible = false
    @EpoxyAttribute lateinit var onBicycleClick: () -> Unit

    @EpoxyAttribute var vehicleVisible = false
    @EpoxyAttribute lateinit var onVehicleClick: () -> Unit

    @EpoxyAttribute var lineVisible = false

    override fun bind(holder: UserDetailsStatusHolder) {
        holder.status.text = status
        holder.status.setVisible(statusVisible)
        holder.status.setOnClickListener { onStatusClick() }

        holder.activitySeparator.setVisible(activitySeparatorVisible)

        holder.still.setVisible(stillVisible)
        holder.still.setOnClickListener { onStillClick() }

        holder.walk.setVisible(walkVisible)
        holder.walk.setOnClickListener { onWalkClick() }

        holder.run.setVisible(runVisible)
        holder.run.setOnClickListener { onRunClick() }

        holder.bicycle.setVisible(bicycleVisible)
        holder.bicycle.setOnClickListener { onBicycleClick() }

        holder.vehicle.setVisible(vehicleVisible)
        holder.vehicle.setOnClickListener { onVehicleClick() }

        holder.line.setVisible(lineVisible)
    }
}

class UserDetailsStatusHolder : KotlinHolder() {
    val status by bind<TextView>(R.id.user_details_status)

    val activitySeparator by bind<View>(R.id.user_details_activity_separator)
    val still by bind<View>(R.id.user_details_activity_still_wrapper)
    val walk by bind<View>(R.id.user_details_activity_walk_wrapper)
    val run by bind<View>(R.id.user_details_activity_run_wrapper)
    val bicycle by bind<View>(R.id.user_details_activity_bicycle_wrapper)
    val vehicle by bind<View>(R.id.user_details_activity_vehicle_wrapper)

    val line by bind<View>(R.id.user_details_status_line)
}