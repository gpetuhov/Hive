package com.gpetuhov.android.hive.ui.epoxy.profile.models

import android.view.View
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.gpetuhov.android.hive.R
import com.gpetuhov.android.hive.ui.epoxy.holder.KotlinHolder

@EpoxyModelClass(layout = R.layout.profile_add_offer_view)
abstract class AddOfferModel : EpoxyModelWithHolder<AddOfferHolder>() {

    @EpoxyAttribute lateinit var onClick: () -> Unit

    override fun bind(holder: AddOfferHolder) {
        holder.rootView.setOnClickListener { onClick() }
    }
}

class AddOfferHolder : KotlinHolder() {
    val rootView by bind<View>(R.id.profile_add_offer)
}