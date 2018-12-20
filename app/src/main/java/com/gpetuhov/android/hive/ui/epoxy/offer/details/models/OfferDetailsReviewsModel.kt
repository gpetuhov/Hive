package com.gpetuhov.android.hive.ui.epoxy.offer.details.models

import android.widget.RatingBar
import android.widget.TextView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.gpetuhov.android.hive.R
import com.gpetuhov.android.hive.ui.epoxy.base.KotlinHolder
import com.gpetuhov.android.hive.util.setVisible

@EpoxyModelClass(layout = R.layout.offer_details_reviews_view)
abstract class OfferDetailsReviewsModel : EpoxyModelWithHolder<OfferDetailsReviewsHolder>() {

    @EpoxyAttribute lateinit var reviewsActionText: String
    @EpoxyAttribute lateinit var onReviewsActionClick: () -> Unit

    @EpoxyAttribute var rating = 0.0F

    override fun bind(holder: OfferDetailsReviewsHolder) {
        holder.reviewsActionText.text = reviewsActionText
        holder.reviewsActionText.setOnClickListener { onReviewsActionClick() }

        val ratingBarVisible = rating != 0.0F
        holder.ratingBar.setVisible(ratingBarVisible)
        holder.ratingBar.rating = rating
    }
}

class OfferDetailsReviewsHolder : KotlinHolder() {
    val reviewsActionText by bind<TextView>(R.id.offer_details_reviews_action_text)
    val ratingBar by bind<RatingBar>(R.id.offer_details_rating_bar)
}