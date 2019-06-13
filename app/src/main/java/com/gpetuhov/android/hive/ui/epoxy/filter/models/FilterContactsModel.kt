package com.gpetuhov.android.hive.ui.epoxy.filter.models

import android.widget.CheckBox
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.gpetuhov.android.hive.R
import com.gpetuhov.android.hive.ui.epoxy.base.KotlinHolder
import com.gpetuhov.android.hive.ui.epoxy.base.bind

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
        holder.hasPhone.bind(hasPhone) { onHasPhoneClick(it) }
        holder.hasEmail.bind(hasEmail) { onHasEmailClick(it) }
        holder.hasSkype.bind(hasSkype) { onHasSkypeClick(it) }
        holder.hasFacebook.bind(hasFacebook) { onHasFacebookClick(it) }
        holder.hasTwitter.bind(hasTwitter) { onHasTwitterClick(it) }
        holder.hasInstagram.bind(hasInstagram) { onHasInstagramClick(it) }
        holder.hasYoutube.bind(hasYoutube) { onHasYoutubeClick(it) }
        holder.hasWebsite.bind(hasWebsite) { onHasWebsiteClick(it) }
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