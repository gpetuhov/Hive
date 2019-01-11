package com.gpetuhov.android.hive.ui.epoxy.profile.models

import android.widget.TextView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.gpetuhov.android.hive.R
import com.gpetuhov.android.hive.ui.epoxy.base.KotlinHolder

@EpoxyModelClass(layout = R.layout.profile_settings_view)
abstract class SettingsModel : EpoxyModelWithHolder<SettingsHolder>() {

    @EpoxyAttribute var signOutEnabled = true
    @EpoxyAttribute lateinit var onSignOutClick: () -> Unit

    @EpoxyAttribute var deleteAccountEnabled = true
    @EpoxyAttribute lateinit var onDeleteAccountClick: () -> Unit

    @EpoxyAttribute lateinit var onPrivacyPolicyClick: () -> Unit

    @EpoxyAttribute lateinit var appVersion: String

    override fun bind(holder: SettingsHolder) {
        holder.signOut.setOnClickListener { onSignOutClick() }
        holder.signOut.isEnabled = signOutEnabled

        holder.deleteAccount.setOnClickListener { onDeleteAccountClick() }
        holder.deleteAccount.isEnabled = deleteAccountEnabled

        holder.privacyPolicy.setOnClickListener { onPrivacyPolicyClick() }

        holder.appVersion.text = appVersion
    }
}

class SettingsHolder : KotlinHolder() {
    val signOut by bind<TextView>(R.id.signout_button)
    val deleteAccount by bind<TextView>(R.id.delete_user_button)
    val privacyPolicy by bind<TextView>(R.id.privacy_policy)
    val appVersion by bind<TextView>(R.id.app_version)
}