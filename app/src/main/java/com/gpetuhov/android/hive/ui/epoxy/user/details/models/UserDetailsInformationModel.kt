package com.gpetuhov.android.hive.ui.epoxy.user.details.models

import android.view.View
import android.widget.TextView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.gpetuhov.android.hive.R
import com.gpetuhov.android.hive.ui.epoxy.base.KotlinHolder
import com.gpetuhov.android.hive.util.setVisible

@EpoxyModelClass(layout = R.layout.user_details_information_view)
abstract class UserDetailsInformationModel : EpoxyModelWithHolder<UserDetailsInformationHolder>() {

    @EpoxyAttribute lateinit var residence: String
    @EpoxyAttribute var residenceVisible = true

    @EpoxyAttribute lateinit var language: String
    @EpoxyAttribute var languageVisible = true
    @EpoxyAttribute var languageSeparatorVisible = true

    override fun bind(holder: UserDetailsInformationHolder) {
        holder.residence.text = residence
        holder.residence.setVisible(residenceVisible)

        holder.language.text = language
        holder.language.setVisible(languageVisible)
        holder.languageSeparator.setVisible(languageSeparatorVisible)
    }
}

class UserDetailsInformationHolder : KotlinHolder() {
    val residence by bind<TextView>(R.id.user_details_residence)
    val language by bind<TextView>(R.id.user_details_language)
    val languageSeparator by bind<View>(R.id.user_details_language_separator)
}