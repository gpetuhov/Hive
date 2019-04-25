package com.gpetuhov.android.hive.ui.epoxy.review.controller

import android.content.Context
import com.airbnb.epoxy.EpoxyController
import com.gpetuhov.android.hive.R
import com.gpetuhov.android.hive.application.HiveApp
import com.gpetuhov.android.hive.presentation.presenter.ReviewsAllFragmentPresenter
import com.gpetuhov.android.hive.ui.epoxy.review.models.reviewTotals
import javax.inject.Inject

class ReviewsAllListController(private val presenter: ReviewsAllFragmentPresenter) : EpoxyController() {

    @Inject lateinit var context: Context

    init {
        HiveApp.appComponent.inject(this)
    }

    override fun buildModels() {
        // TODO: refactor this
        reviewTotals {
            id("review_all_totals")
            totalReviews("${context.getString(R.string.total_reviews)}: ${presenter.allReviewCount}")

            val ratingText = "%.2f".format(presenter.allRating)
            averageRating("${context.getString(R.string.average_rating)}: $ratingText")

            rating(presenter.allRating)
        }

        // TODO: show reviews
    }
}