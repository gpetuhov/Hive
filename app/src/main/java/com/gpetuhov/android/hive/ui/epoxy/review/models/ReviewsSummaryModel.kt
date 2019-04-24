package com.gpetuhov.android.hive.ui.epoxy.review.models

import android.widget.TextView
import androidx.appcompat.widget.AppCompatRatingBar
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.gpetuhov.android.hive.R
import com.gpetuhov.android.hive.ui.epoxy.base.KotlinHolder
import com.gpetuhov.android.hive.util.setVisible

@EpoxyModelClass(layout = R.layout.reviews_summary_view)
abstract class ReviewsSummaryModel : EpoxyModelWithHolder<ReviewsSummaryHolder>() {

    @EpoxyAttribute lateinit var reviewsActionText: String
    @EpoxyAttribute lateinit var onClick: () -> Unit

    @EpoxyAttribute var rating = 0.0F

    override fun bind(holder: ReviewsSummaryHolder) {
        holder.reviewsActionText.text = reviewsActionText
        holder.reviewsActionText.setOnClickListener { onClick() }

        val ratingBarVisible = rating != 0.0F
        holder.ratingBar.setVisible(ratingBarVisible)
        holder.ratingBar.rating = rating
        holder.ratingBar.setOnClickListener { onClick() }
    }
}

class ReviewsSummaryHolder : KotlinHolder() {
    val reviewsActionText by bind<TextView>(R.id.offer_details_reviews_action_text)
    val ratingBar by bind<AppCompatRatingBar>(R.id.offer_details_rating_bar)
}