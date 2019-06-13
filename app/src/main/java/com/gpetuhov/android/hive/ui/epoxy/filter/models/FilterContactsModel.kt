package com.gpetuhov.android.hive.ui.epoxy.filter.models

import android.widget.CheckBox
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.gpetuhov.android.hive.R
import com.gpetuhov.android.hive.ui.epoxy.base.KotlinHolder

@EpoxyModelClass(layout = R.layout.filter_contacts_view)
abstract class FilterContactsModel : EpoxyModelWithHolder<FilterContactsHolder>() {

    @EpoxyAttribute var hasPhone = false
    @EpoxyAttribute lateinit var onHasPhoneClick: (Boolean) -> Unit

    @EpoxyAttribute var hasEmail = false
    @EpoxyAttribute lateinit var onHasEmailClick: (Boolean) -> Unit

    @EpoxyAttribute var hasSkype = false
    @EpoxyAttribute lateinit var onHasSkypeClick: (Boolean) -> Unit

    @EpoxyAttribute var hasFacebook = false
    @EpoxyAttribute lateinit var onHasFacebookClick: (Boolean) -> Unit

    @EpoxyAttribute var hasTwitter = false
    @EpoxyAttribute lateinit var onHasTwitterClick: (Boolean) -> Unit

    @EpoxyAttribute var hasInstagram = false
    @EpoxyAttribute lateinit var onHasInstagramClick: (Boolean) -> Unit

    @EpoxyAttribute var hasYoutube = false
    @EpoxyAttribute lateinit var onHasYoutubeClick: (Boolean) -> Unit

    @EpoxyAttribute var hasWebsite = false
    @EpoxyAttribute lateinit var onHasWebsiteClick: (Boolean) -> Unit

    override fun bind(holder: FilterContactsHolder) {
        holder.hasPhone.isChecked = hasPhone
        holder.hasPhone.setOnClickListener { view -> onHasPhoneClick((view as CheckBox).isChecked) }

        holder.hasEmail.isChecked = hasEmail
        holder.hasEmail.setOnClickListener { view -> onHasEmailClick((view as CheckBox).isChecked) }

        holder.hasSkype.isChecked = hasSkype
        holder.hasSkype.setOnClickListener { view -> onHasSkypeClick((view as CheckBox).isChecked) }

        holder.hasFacebook.isChecked = hasFacebook
        holder.hasFacebook.setOnClickListener { view -> onHasFacebookClick((view as CheckBox).isChecked) }

        holder.hasTwitter.isChecked = hasTwitter
        holder.hasTwitter.setOnClickListener { view -> onHasTwitterClick((view as CheckBox).isChecked) }

        holder.hasInstagram.isChecked = hasInstagram
        holder.hasInstagram.setOnClickListener { view -> onHasInstagramClick((view as CheckBox).isChecked) }

        holder.hasYoutube.isChecked = hasYoutube
        holder.hasYoutube.setOnClickListener { view -> onHasYoutubeClick((view as CheckBox).isChecked) }

        holder.hasWebsite.isChecked = hasWebsite
        holder.hasWebsite.setOnClickListener { view -> onHasWebsiteClick((view as CheckBox).isChecked) }
    }
}

class FilterContactsHolder : KotlinHolder() {
    val hasPhone by bind<CheckBox>(R.id.filter_has_phone)
    val hasEmail by bind<CheckBox>(R.id.filter_has_email)
    val hasSkype by bind<CheckBox>(R.id.filter_has_skype)
    val hasFacebook by bind<CheckBox>(R.id.filter_has_facebook)
    val hasTwitter by bind<CheckBox>(R.id.filter_has_twitter)
    val hasInstagram by bind<CheckBox>(R.id.filter_has_instagram)
    val hasYoutube by bind<CheckBox>(R.id.filter_has_youtube)
    val hasWebsite by bind<CheckBox>(R.id.filter_has_website)
}