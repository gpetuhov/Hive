package com.gpetuhov.android.hive.ui.epoxy.offer.update.models

import android.widget.TextView
import androidx.appcompat.widget.AppCompatRatingBar
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

    @EpoxyAttribute lateinit var offerStarCount: String

    override fun bind(holder: UpdateOfferReviewsHolder) {
        holder.reviewsActionText.text = reviewsActionText
        holder.reviewsActionText.setOnClickListener { onClick() }

        val ratingBarVisible = rating != 0.0F
        holder.ratingBar.setVisible(ratingBarVisible)
        holder.ratingBar.rating = rating
        holder.ratingBar.setOnClickListener { if (ratingBarVisible) onClick() }

        holder.offerStarCount.text = offerStarCount
    }
}

class UpdateOfferReviewsHolder : KotlinHolder() {
    val reviewsActionText by bind<TextView>(R.id.update_offer_reviews_action_text)
    val ratingBar by bind<AppCompatRatingBar>(R.id.update_offer_rating_bar)
    val offerStarCount by bind<TextView>(R.id.update_offer_star_count)
}