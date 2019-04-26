package com.gpetuhov.android.hive.ui.epoxy.review.controller

import android.content.Context
import com.gpetuhov.android.hive.application.HiveApp
import com.gpetuhov.android.hive.presentation.presenter.ReviewsAllFragmentPresenter
import com.gpetuhov.android.hive.ui.epoxy.review.models.reviewItem
import com.gpetuhov.android.hive.util.getDateTimeFromTimestamp
import javax.inject.Inject

class ReviewsAllListController(private val presenter: ReviewsAllFragmentPresenter) : ReviewsBaseController() {

    @Inject lateinit var context: Context

    init {
        HiveApp.appComponent.inject(this)
    }

    override fun buildModels() {
        reviewTotals(context, presenter.allReviewCount, presenter.allRating)

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
                showOfferVisible(true)
                onShowOfferClick {
                    // TODO: implement
                }
            }
        }
    }
}