package com.gpetuhov.android.hive.ui.epoxy.profile.models

import android.widget.TextView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.gpetuhov.android.hive.R
import com.gpetuhov.android.hive.ui.epoxy.base.KotlinHolder
import com.gpetuhov.android.hive.util.setVisible

@EpoxyModelClass(layout = R.layout.profile_offers_header_view)
abstract class ProfileOffersHeaderModel : EpoxyModelWithHolder<ProfileOffersHeaderHolder>() {

    @EpoxyAttribute var noActiveOffersWarningVisible = false

    override fun bind(holder: ProfileOffersHeaderHolder) {
        holder.noActiveOffersWarning.setVisible(noActiveOffersWarningVisible)
    }
}

class ProfileOffersHeaderHolder : KotlinHolder() {
    val noActiveOffersWarning by bind<TextView>(R.id.user_no_active_offers_warning)
}