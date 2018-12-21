package com.gpetuhov.android.hive.ui.epoxy.review.controller

import com.airbnb.epoxy.EpoxyController
import com.gpetuhov.android.hive.domain.model.Review
import com.gpetuhov.android.hive.presentation.presenter.ReviewsFragmentPresenter
import com.gpetuhov.android.hive.ui.epoxy.review.models.reviewItem
import com.gpetuhov.android.hive.util.getDateTimeFromTimestamp

class ReviewsListController(private val presenter: ReviewsFragmentPresenter) : EpoxyController() {

    private var reviewsList = mutableListOf<Review>()

    override fun buildModels() {
        reviewsList.forEach { review ->
            reviewItem {
                id(review.uid)
                userPicUrl(review.authorUserPicUrl)
                username(review.authorName)
                time(getDateTimeFromTimestamp(review.timestamp))
                reviewText(review.text)
                rating(review.rating)
            }
        }
    }

    fun changeReviewsList(reviewsList: MutableList<Review>) {
        this.reviewsList = reviewsList
        requestModelBuild()
    }
}