package com.gpetuhov.android.hive.ui.epoxy.review.models

import android.widget.TextView
import androidx.appcompat.widget.AppCompatRatingBar
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.gpetuhov.android.hive.R
import com.gpetuhov.android.hive.ui.epoxy.base.KotlinHolder

@EpoxyModelClass(layout = R.layout.review_totals_view)
abstract class ReviewTotalsModel : EpoxyModelWithHolder<ReviewTotalsHolder>() {

    @EpoxyAttribute lateinit var totalReviews: String
    @EpoxyAttribute lateinit var averageRating: String
    @EpoxyAttribute var rating = 0.0F

    override fun bind(holder: ReviewTotalsHolder) {
        holder.totalReviews.text = totalReviews
        holder.averageRating.text = averageRating
        holder.ratingBar.rating = rating
    }
}

class ReviewTotalsHolder : KotlinHolder() {
    val totalReviews by bind<TextView>(R.id.review_totals_review_count)
    val averageRating by bind<TextView>(R.id.review_totals_average_rating)
    val ratingBar by bind<AppCompatRatingBar>(R.id.review_totals_rating_bar)
}