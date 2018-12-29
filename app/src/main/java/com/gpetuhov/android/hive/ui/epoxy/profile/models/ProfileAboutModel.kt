package com.gpetuhov.android.hive.ui.epoxy.profile.models

import android.widget.TextView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.gpetuhov.android.hive.R
import com.gpetuhov.android.hive.ui.epoxy.base.KotlinHolder

@EpoxyModelClass(layout = R.layout.profile_about_view)
abstract class ProfileAboutModel : EpoxyModelWithHolder<ProfileAboutHolder>() {

    @EpoxyAttribute lateinit var description: String
    @EpoxyAttribute lateinit var onDescriptionClick: () -> Unit

    override fun bind(holder: ProfileAboutHolder) {
        holder.description.text = description
        holder.description.setOnClickListener { onDescriptionClick() }
    }
}

class ProfileAboutHolder : KotlinHolder() {
    val description by bind<TextView>(R.id.user_description_textview)
}