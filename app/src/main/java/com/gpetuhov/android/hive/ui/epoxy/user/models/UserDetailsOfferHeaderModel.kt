package com.gpetuhov.android.hive.ui.epoxy.user.models

import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.gpetuhov.android.hive.R
import com.gpetuhov.android.hive.ui.epoxy.holder.KotlinHolder

@EpoxyModelClass(layout = R.layout.user_details_offer_header_view)
abstract class UserDetailsOfferHeaderModel : EpoxyModelWithHolder<UserDetailsOfferHeaderHolder>() {

    override fun bind(holder: UserDetailsOfferHeaderHolder) {
        // Do nothing
    }
}

class UserDetailsOfferHeaderHolder : KotlinHolder()