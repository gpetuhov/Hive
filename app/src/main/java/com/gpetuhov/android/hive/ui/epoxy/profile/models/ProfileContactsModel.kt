package com.gpetuhov.android.hive.ui.epoxy.profile.models

import android.view.View
import android.widget.TextView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.gpetuhov.android.hive.R
import com.gpetuhov.android.hive.ui.epoxy.base.KotlinHolder

@EpoxyModelClass(layout = R.layout.profile_contacts_view)
abstract class ProfileContactsModel : EpoxyModelWithHolder<ProfileContactsHolder>() {

    @EpoxyAttribute lateinit var phone: String
    @EpoxyAttribute lateinit var onPhoneClick: () -> Unit

    @EpoxyAttribute lateinit var email: String
    @EpoxyAttribute lateinit var onEmailClick: () -> Unit

    @EpoxyAttribute lateinit var onUseRegistrationEmailClick: () -> Unit

    @EpoxyAttribute lateinit var skype: String
    @EpoxyAttribute lateinit var onSkypeClick: () -> Unit

    @EpoxyAttribute lateinit var facebook: String
    @EpoxyAttribute lateinit var onFacebookClick: () -> Unit

    @EpoxyAttribute lateinit var twitter: String
    @EpoxyAttribute lateinit var onTwitterClick: () -> Unit

    @EpoxyAttribute lateinit var instagram: String
    @EpoxyAttribute lateinit var onInstagramClick: () -> Unit

    @EpoxyAttribute lateinit var youTube: String
    @EpoxyAttribute lateinit var onYouTubeClick: () -> Unit

    @EpoxyAttribute lateinit var website: String
    @EpoxyAttribute lateinit var onWebsiteClick: () -> Unit

    override fun bind(holder: ProfileContactsHolder) {
        holder.phone.text = phone
        holder.phoneWrapper.setOnClickListener { onPhoneClick() }

        holder.email.text = email
        holder.emailWrapper.setOnClickListener { onEmailClick() }

        holder.useRegistrationEmail.setOnClickListener { onUseRegistrationEmailClick() }

        holder.skype.text = skype
        holder.skypeWrapper.setOnClickListener { onSkypeClick() }

        holder.facebook.text = facebook
        holder.facebookWrapper.setOnClickListener { onFacebookClick() }

        holder.twitter.text = twitter
        holder.twitterWrapper.setOnClickListener { onTwitterClick() }

        holder.instagram.text = instagram
        holder.instagramWrapper.setOnClickListener { onInstagramClick() }

        holder.youTube.text = youTube
        holder.youTubeWrapper.setOnClickListener { onYouTubeClick() }

        holder.website.text = website
        holder.websiteWrapper.setOnClickListener { onWebsiteClick() }
    }
}

class ProfileContactsHolder : KotlinHolder() {
    val phoneWrapper by bind<View>(R.id.user_phone_wrapper)
    val phone by bind<TextView>(R.id.user_phone)
    val emailWrapper by bind<View>(R.id.user_visible_email_wrapper)
    val email by bind<TextView>(R.id.user_visible_email)
    val useRegistrationEmail by bind<TextView>(R.id.user_use_registration_email)
    val skypeWrapper by bind<View>(R.id.user_skype_wrapper)
    val skype by bind<TextView>(R.id.user_skype)
    val facebookWrapper by bind<View>(R.id.user_facebook_wrapper)
    val facebook by bind<TextView>(R.id.user_facebook)
    val twitterWrapper by bind<View>(R.id.user_twitter_wrapper)
    val twitter by bind<TextView>(R.id.user_twitter)
    val instagramWrapper by bind<View>(R.id.user_instagram_wrapper)
    val instagram by bind<TextView>(R.id.user_instagram)
    val youTubeWrapper by bind<View>(R.id.user_youtube_wrapper)
    val youTube by bind<TextView>(R.id.user_youtube)
    val websiteWrapper by bind<View>(R.id.user_website_wrapper)
    val website by bind<TextView>(R.id.user_website)
}