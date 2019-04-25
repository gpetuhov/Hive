package com.gpetuhov.android.hive.ui.epoxy.review.controller

import android.content.Context
import com.airbnb.epoxy.EpoxyController
import com.gpetuhov.android.hive.R
import com.gpetuhov.android.hive.application.HiveApp
import com.gpetuhov.android.hive.presentation.presenter.ReviewsAllFragmentPresenter
import com.gpetuhov.android.hive.ui.epoxy.review.models.reviewItem
import com.gpetuhov.android.hive.ui.epoxy.review.models.reviewTotals
import com.gpetuhov.android.hive.util.getDateTimeFromTimestamp
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

        presenter.allReviews.forEach { review ->
            reviewItem {
                id(review.uid)
                userPicUrl(review.authorUserPicUrl)
                username(review.authorName)
                time(getDateTimeFromTimestamp(review.timestamp))
                reviewText(review.text)
                rating(review.rating)
                ratingVisible(true)
                controlsVisible(false)
                onEditClick { /* Do nothing */ }
                onDeleteClick { /* Do nothing */ }

                commentVisible(false)

                onCommentClick { /* Do nothing */ }
                commentTextVisible(review.hasComment())
                commentText(review.comment)
                commentControlsVisible(false )
                onCommentEditClick { /* Do nothing */ }
                onCommentDeleteClick { /* Do nothing */ }
            }
        }
    }
}