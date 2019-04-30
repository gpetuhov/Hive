package com.gpetuhov.android.hive.ui.epoxy.profile.models

import android.widget.TextView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.gpetuhov.android.hive.R
import com.gpetuhov.android.hive.ui.epoxy.base.KotlinHolder

@EpoxyModelClass(layout = R.layout.profile_information_view)
abstract class ProfileInformationModel : EpoxyModelWithHolder<ProfileInformationHolder>() {

    @EpoxyAttribute lateinit var residence: String
    @EpoxyAttribute lateinit var onResidenceClick: () -> Unit

    override fun bind(holder: ProfileInformationHolder) {
        holder.residence.text = residence
        holder.residence.setOnClickListener { onResidenceClick() }
    }
}

class ProfileInformationHolder : KotlinHolder() {
    val residence by bind<TextView>(R.id.user_residence)
}