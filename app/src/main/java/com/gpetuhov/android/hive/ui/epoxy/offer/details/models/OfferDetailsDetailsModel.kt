package com.gpetuhov.android.hive.ui.epoxy.offer.details.models

import android.widget.TextView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.gpetuhov.android.hive.R
import com.gpetuhov.android.hive.ui.epoxy.holder.KotlinHolder

@EpoxyModelClass(layout = R.layout.offer_details_details_view)
abstract class OfferDetailsDetailsModel : EpoxyModelWithHolder<OfferDetailsDetailsHolder>() {

    @EpoxyAttribute lateinit var title: String
    @EpoxyAttribute lateinit var description: String

    override fun bind(holder: OfferDetailsDetailsHolder) {
        holder.title.text = title
        holder.description.text = description
    }
}

class OfferDetailsDetailsHolder : KotlinHolder() {
    val title by bind<TextView>(R.id.offer_details_title)
    val description by bind<TextView>(R.id.offer_details_description)
}