package com.gpetuhov.android.hive.ui.epoxy.review.controller

import android.content.Context
import com.airbnb.epoxy.EpoxyController
import com.gpetuhov.android.hive.R
import com.gpetuhov.android.hive.application.HiveApp
import com.gpetuhov.android.hive.presentation.presenter.ReviewsFragmentPresenter
import com.gpetuhov.android.hive.ui.epoxy.review.models.reviewItem
import com.gpetuhov.android.hive.ui.epoxy.review.models.reviewTotals
import com.gpetuhov.android.hive.util.getDateTimeFromTimestamp
import javax.inject.Inject

class ReviewsListController(private val presenter: ReviewsFragmentPresenter, onModelBuild: () -> Unit) : EpoxyController() {

    @Inject lateinit var context: Context

    init {
        HiveApp.appComponent.inject(this)
        addModelBuildListener { onModelBuild() }
    }

    override fun buildModels() {
        reviewTotals {
            id("review_totals")
            totalReviews("${context.getString(R.string.total_reviews)}: ${presenter.reviewCount}")

            val ratingText = "%.2f".format(presenter.rating)
            averageRating("${context.getString(R.string.average_rating)}: $ratingText")

            rating(presenter.rating)
        }

        presenter.reviewsList.forEach { review ->
            reviewItem {
                id(review.uid)
                userPicUrl(review.authorUserPicUrl)
                username(review.authorName)
                time(getDateTimeFromTimestamp(review.timestamp))
                reviewText(review.text)
                rating(review.rating)
                ratingVisible(true)
                controlsVisible(review.isFromCurrentUser)
                onEditClick { presenter.editReview(review.uid) }
                onDeleteClick { presenter.showDeleteReviewDialog(review.uid) }
            }
        }
    }
}