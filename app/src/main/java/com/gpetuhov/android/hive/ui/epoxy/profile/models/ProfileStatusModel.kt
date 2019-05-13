package com.gpetuhov.android.hive.ui.epoxy.profile.models

import android.view.View
import android.widget.TextView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.gpetuhov.android.hive.R
import com.gpetuhov.android.hive.ui.epoxy.base.KotlinHolder
import com.gpetuhov.android.hive.util.setVisible

@EpoxyModelClass(layout = R.layout.profile_status_view)
abstract class ProfileStatusModel : EpoxyModelWithHolder<ProfileStatusHolder>() {

    @EpoxyAttribute lateinit var status: String
    @EpoxyAttribute lateinit var onStatusClick: () -> Unit

    @EpoxyAttribute var activitySeparatorVisible = false
    @EpoxyAttribute var stillVisible = false
    @EpoxyAttribute var walkVisible = false
    @EpoxyAttribute var runVisible = false
    @EpoxyAttribute var bicycleVisible = false
    @EpoxyAttribute var vehicleVisible = false

    override fun bind(holder: ProfileStatusHolder) {
        holder.status.text = status
        holder.status.setOnClickListener { onStatusClick() }

        holder.activitySeparator.setVisible(activitySeparatorVisible)
        holder.still.setVisible(stillVisible)
        holder.walk.setVisible(walkVisible)
        holder.run.setVisible(runVisible)
        holder.bicycle.setVisible(bicycleVisible)
        holder.vehicle.setVisible(vehicleVisible)
    }
}

class ProfileStatusHolder : KotlinHolder() {
    val status by bind<TextView>(R.id.user_status)

    val activitySeparator by bind<View>(R.id.user_activity_separator)
    val still by bind<View>(R.id.user_activity_still_wrapper)
    val walk by bind<View>(R.id.user_activity_walk_wrapper)
    val run by bind<View>(R.id.user_activity_run_wrapper)
    val bicycle by bind<View>(R.id.user_activity_bicycle_wrapper)
    val vehicle by bind<View>(R.id.user_activity_vehicle_wrapper)
}