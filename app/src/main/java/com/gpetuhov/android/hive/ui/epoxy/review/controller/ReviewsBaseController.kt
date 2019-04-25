package com.gpetuhov.android.hive.ui.epoxy.review.controller

import android.content.Context
import com.airbnb.epoxy.EpoxyController
import com.gpetuhov.android.hive.R
import com.gpetuhov.android.hive.ui.epoxy.review.models.reviewTotals

abstract class ReviewsBaseController : EpoxyController() {

    protected fun reviewTotals(context: Context, reviewsCount: Int, rating: Float) {
        reviewTotals {
            id("review_totals")
            totalReviews("${context.getString(R.string.total_reviews)}: $reviewsCount")

            val ratingText = "%.2f".format(rating)
            averageRating("${context.getString(R.string.average_rating)}: $ratingText")

            rating(rating)
        }
    }
}