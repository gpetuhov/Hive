package com.gpetuhov.android.hive.ui.epoxy.offer.details.models

import android.widget.TextView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.gpetuhov.android.hive.R
import com.gpetuhov.android.hive.ui.epoxy.holder.KotlinHolder

@EpoxyModelClass(layout = R.layout.offer_details_title_view)
abstract class OfferDetailsTitleModel : EpoxyModelWithHolder<OfferDetailsTitleHolder>() {

    @EpoxyAttribute lateinit var title: String

    override fun bind(holder: OfferDetailsTitleHolder) {
        holder.title.text = title
    }
}

class OfferDetailsTitleHolder : KotlinHolder() {
    val title by bind<TextView>(R.id.offer_details_title)
}