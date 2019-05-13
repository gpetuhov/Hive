package com.gpetuhov.android.hive.ui.epoxy.user.details.models

import android.widget.TextView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.gpetuhov.android.hive.R
import com.gpetuhov.android.hive.ui.epoxy.base.KotlinHolder
import com.gpetuhov.android.hive.util.setVisible

@EpoxyModelClass(layout = R.layout.user_details_status_view)
abstract class UserDetailsStatusModel : EpoxyModelWithHolder<UserDetailsStatusHolder>() {

    @EpoxyAttribute lateinit var status: String
    @EpoxyAttribute var statusVisible = true

    override fun bind(holder: UserDetailsStatusHolder) {
        holder.status.text = status
        holder.status.setVisible(statusVisible)
    }
}

class UserDetailsStatusHolder : KotlinHolder() {
    val status by bind<TextView>(R.id.user_details_status)
}