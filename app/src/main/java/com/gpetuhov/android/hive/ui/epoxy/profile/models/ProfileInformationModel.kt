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

    @EpoxyAttribute lateinit var language: String
    @EpoxyAttribute lateinit var onLanguageClick: () -> Unit

    override fun bind(holder: ProfileInformationHolder) {
        holder.residence.text = residence
        holder.residence.setOnClickListener { onResidenceClick() }

        holder.language.text = language
        holder.language.setOnClickListener { onLanguageClick() }
    }
}

class ProfileInformationHolder : KotlinHolder() {
    val residence by bind<TextView>(R.id.user_residence)
    val language by bind<TextView>(R.id.user_language)
}