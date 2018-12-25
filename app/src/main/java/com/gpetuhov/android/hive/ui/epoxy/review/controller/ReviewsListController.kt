package com.gpetuhov.android.hive.ui.epoxy.review.controller

import android.content.Context
import com.airbnb.epoxy.EpoxyController
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
            totalReviews("Total reviews: ${presenter.reviewCount}")
            averageRating("Average rating: ${presenter.rating}")
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
            }
        }
    }
}