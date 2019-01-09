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

    override fun bind(holder: ProfileContactsHolder) {
        holder.phone.text = phone
        holder.phoneWrapper.setOnClickListener { onPhoneClick() }

        holder.email.text = email
        holder.emailWrapper.setOnClickListener { onEmailClick() }

        holder.useRegistrationEmail.setOnClickListener { onUseRegistrationEmailClick() }
    }
}

class ProfileContactsHolder : KotlinHolder() {
    val phoneWrapper by bind<View>(R.id.user_phone_wrapper)
    val phone by bind<TextView>(R.id.user_phone)
    val emailWrapper by bind<View>(R.id.user_visible_email_wrapper)
    val email by bind<TextView>(R.id.user_visible_email)
    val useRegistrationEmail by bind<TextView>(R.id.user_use_registration_email)
}