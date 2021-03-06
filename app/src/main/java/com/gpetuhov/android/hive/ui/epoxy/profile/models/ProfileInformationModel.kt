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

    @EpoxyAttribute lateinit var education: String
    @EpoxyAttribute lateinit var onEducationClick: () -> Unit

    @EpoxyAttribute lateinit var work: String
    @EpoxyAttribute lateinit var onWorkClick: () -> Unit

    @EpoxyAttribute lateinit var interests: String
    @EpoxyAttribute lateinit var onInterestsClick: () -> Unit

    override fun bind(holder: ProfileInformationHolder) {
        holder.residence.text = residence
        holder.residence.setOnClickListener { onResidenceClick() }

        holder.language.text = language
        holder.language.setOnClickListener { onLanguageClick() }

        holder.education.text = education
        holder.education.setOnClickListener { onEducationClick() }

        holder.work.text = work
        holder.work.setOnClickListener { onWorkClick() }

        holder.interests.text = interests
        holder.interests.setOnClickListener { onInterestsClick() }
    }
}

class ProfileInformationHolder : KotlinHolder() {
    val residence by bind<TextView>(R.id.user_residence)
    val language by bind<TextView>(R.id.user_language)
    val education by bind<TextView>(R.id.user_education)
    val work by bind<TextView>(R.id.user_work)
    val interests by bind<TextView>(R.id.user_interests)
}