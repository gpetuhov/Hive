package com.gpetuhov.android.hive.ui.epoxy.review.update.models

import android.widget.RatingBar
import android.widget.TextView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.gpetuhov.android.hive.R
import com.gpetuhov.android.hive.ui.epoxy.base.KotlinHolder

@EpoxyModelClass(layout = R.layout.update_review_details_view)
abstract class UpdateReviewDetailsModel : EpoxyModelWithHolder<UpdateReviewDetailsHolder>() {

    @EpoxyAttribute lateinit var reviewText: String
    @EpoxyAttribute lateinit var onReviewTextClick: () -> Unit

    @EpoxyAttribute var rating = 0.0F
    @EpoxyAttribute lateinit var onRatingClick: (Float) -> Unit

    override fun bind(holder: UpdateReviewDetailsHolder) {
        holder.reviewText.text = reviewText
        holder.reviewText.setOnClickListener { onReviewTextClick() }

        holder.ratingBar.rating = rating
        holder.ratingBar.setOnClickListener { view -> onRatingClick((view as RatingBar).rating) }
    }
}

class UpdateReviewDetailsHolder : KotlinHolder() {
    val reviewText by bind<TextView>(R.id.update_review_text)
    val ratingBar by bind<RatingBar>(R.id.update_review_rating_bar)
}