package com.gpetuhov.android.hive.ui.epoxy.offer.update.models

import android.widget.RatingBar
import android.widget.TextView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.gpetuhov.android.hive.R
import com.gpetuhov.android.hive.ui.epoxy.base.KotlinHolder
import com.gpetuhov.android.hive.util.setVisible

@EpoxyModelClass(layout = R.layout.update_offer_reviews_view)
abstract class UpdateOfferReviewsModel : EpoxyModelWithHolder<UpdateOfferReviewsHolder>() {

    @EpoxyAttribute lateinit var reviewsActionText: String
    @EpoxyAttribute lateinit var onClick: () -> Unit

    @EpoxyAttribute var rating = 0.0F

    override fun bind(holder: UpdateOfferReviewsHolder) {
        holder.reviewsActionText.text = reviewsActionText
        holder.reviewsActionText.setOnClickListener { onClick() }

        val ratingBarVisible = rating != 0.0F
        holder.ratingBar.setVisible(ratingBarVisible)
        holder.ratingBar.rating = rating
        holder.ratingBar.setOnClickListener { if (ratingBarVisible) onClick() }
    }
}

class UpdateOfferReviewsHolder : KotlinHolder() {
    val reviewsActionText by bind<TextView>(R.id.update_offer_reviews_action_text)
    val ratingBar by bind<RatingBar>(R.id.update_offer_rating_bar)
}