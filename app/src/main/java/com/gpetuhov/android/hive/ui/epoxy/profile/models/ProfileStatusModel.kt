package com.gpetuhov.android.hive.ui.epoxy.profile.models

import android.widget.TextView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.gpetuhov.android.hive.R
import com.gpetuhov.android.hive.ui.epoxy.base.KotlinHolder

@EpoxyModelClass(layout = R.layout.profile_status_view)
abstract class ProfileStatusModel : EpoxyModelWithHolder<ProfileStatusHolder>() {

    @EpoxyAttribute lateinit var status: String
    @EpoxyAttribute lateinit var onStatusClick: () -> Unit

    override fun bind(holder: ProfileStatusHolder) {
        holder.status.text = status
        holder.status.setOnClickListener { onStatusClick() }
    }
}

class ProfileStatusHolder : KotlinHolder() {
    val status by bind<TextView>(R.id.user_status)
}