package com.gpetuhov.android.hive.ui.epoxy.offer.details.models

import android.widget.TextView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.gpetuhov.android.hive.R
import com.gpetuhov.android.hive.ui.epoxy.base.KotlinHolder

@EpoxyModelClass(layout = R.layout.offer_details_reviews_view)
abstract class OfferDetailsReviewsModel : EpoxyModelWithHolder<OfferDetailsReviewsHolder>() {

    @EpoxyAttribute lateinit var reviewsActionText: String
    @EpoxyAttribute lateinit var onReviewsActionClick: () -> Unit

    override fun bind(holder: OfferDetailsReviewsHolder) {
        holder.reviewsActionText.text = reviewsActionText
        holder.reviewsActionText.setOnClickListener { onReviewsActionClick() }
    }
}

class OfferDetailsReviewsHolder : KotlinHolder() {
    val reviewsActionText by bind<TextView>(R.id.offer_details_reviews_action_text)
}