package com.gpetuhov.android.hive.ui.epoxy.offer.details.models

import android.widget.ImageView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.gpetuhov.android.hive.R
import com.gpetuhov.android.hive.ui.epoxy.holder.KotlinHolder

@EpoxyModelClass(layout = R.layout.offer_details_header_view)
abstract class OfferDetailsHeaderModel : EpoxyModelWithHolder<OfferDetailsHeaderHolder>() {

    @EpoxyAttribute lateinit var onBackButtonClick: () -> Unit

    override fun bind(holder: OfferDetailsHeaderHolder) {
        holder.backButton.setOnClickListener { onBackButtonClick() }
    }
}

class OfferDetailsHeaderHolder : KotlinHolder() {
    val backButton by bind<ImageView>(R.id.offer_details_back_button)
}