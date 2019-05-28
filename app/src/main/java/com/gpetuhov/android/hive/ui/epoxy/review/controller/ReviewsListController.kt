package com.gpetuhov.android.hive.ui.epoxy.review.controller

import android.content.Context
import com.gpetuhov.android.hive.application.HiveApp
import com.gpetuhov.android.hive.presentation.presenter.ReviewsFragmentPresenter
import com.gpetuhov.android.hive.ui.epoxy.review.models.reviewItem
import com.gpetuhov.android.hive.util.getDateTimeFromTimestamp
import javax.inject.Inject

class ReviewsListController(private val presenter: ReviewsFragmentPresenter, onModelBuild: () -> Unit) : ReviewsBaseController() {

    @Inject lateinit var context: Context

    init {
        HiveApp.appComponent.inject(this)
        addModelBuildListener { onModelBuild() }
    }

    override fun buildModels() {
        reviewTotals(context, presenter.reviewCount, presenter.rating)

        val reviewsListSize = presenter.reviewsList.size
        presenter.reviewsList.forEachIndexed { index, review ->
            val isFirstReview = index == reviewsListSize - 1    // this is because reviews list is sorted, so that first posted reviews come last
            reviewItem {
                id(review.uid)
                userPicUrl(review.authorUserPicUrl)
                username(review.authorName)
                time(getDateTimeFromTimestamp(review.timestamp))
                reviewText(review.text)
                rating(review.rating)
                ratingVisible(true)
                controlsVisible(review.isFromCurrentUser)
                onEditClick { presenter.editReview(review.uid, review.text, review.rating) }
                onDeleteClick { presenter.showDeleteReviewDialog(review.uid, isFirstReview) }

                // TODO: make this a paid feature
//                commentVisible(presenter.isCurrentUser && !review.hasComment())
                commentVisible(false)

                onCommentClick { presenter.editComment(review.uid, review.comment) }
                commentTextVisible(review.hasComment())
                commentText(review.comment)
                commentControlsVisible(presenter.isCurrentUser && review.hasComment())
                onCommentEditClick { presenter.editComment(review.uid, review.comment) }
                onCommentDeleteClick { presenter.showDeleteCommentDialog(review.uid) }
                showOfferVisible(false)
                onShowOfferClick { /* Do nothing */ }
            }
        }
    }
}