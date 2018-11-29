package com.gpetuhov.android.hive.ui.epoxy.user.models

import android.widget.TextView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.gpetuhov.android.hive.R
import com.gpetuhov.android.hive.ui.epoxy.holder.KotlinHolder

@EpoxyModelClass(layout = R.layout.user_details_offer_header_view)
abstract class UserDetailsOfferHeaderModel : EpoxyModelWithHolder<UserDetailsOfferHeaderHolder>() {

    @EpoxyAttribute lateinit var offerHeader: String

    override fun bind(holder: UserDetailsOfferHeaderHolder) {
        holder.offerHeader.text = offerHeader
    }
}

class UserDetailsOfferHeaderHolder : KotlinHolder() {
    val offerHeader by bind<TextView>(R.id.user_details_offer_header)
}