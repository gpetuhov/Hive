package com.gpetuhov.android.hive.ui.epoxy.profile.models

import android.widget.Button
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.gpetuhov.android.hive.R
import com.gpetuhov.android.hive.ui.epoxy.holder.KotlinHolder

@EpoxyModelClass(layout = R.layout.profile_settings_view)
abstract class SettingsModel : EpoxyModelWithHolder<SettingsHolder>() {

    @EpoxyAttribute lateinit var onSignOutClick: () -> Unit
    @EpoxyAttribute lateinit var onDeleteAccountClick: () -> Unit

    override fun bind(holder: SettingsHolder) {
        holder.signOut.setOnClickListener { onSignOutClick() }
        holder.deleteAccount.setOnClickListener { onDeleteAccountClick() }
    }
}

class SettingsHolder : KotlinHolder() {
    val signOut by bind<Button>(R.id.signout_button)
    val deleteAccount by bind<Button>(R.id.delete_user_button)
}