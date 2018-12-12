package com.gpetuhov.android.hive.ui.epoxy.user.models

import android.widget.TextView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.gpetuhov.android.hive.R
import com.gpetuhov.android.hive.ui.epoxy.base.KotlinHolder

@EpoxyModelClass(layout = R.layout.user_details_description_view)
abstract class UserDetailsDescriptionModel : EpoxyModelWithHolder<UserDetailsDescriptionHolder>() {

    @EpoxyAttribute lateinit var description: String

    override fun bind(holder: UserDetailsDescriptionHolder) {
        holder.description.text = description
    }
}

class UserDetailsDescriptionHolder : KotlinHolder() {
    val description by bind<TextView>(R.id.user_details_description)
}