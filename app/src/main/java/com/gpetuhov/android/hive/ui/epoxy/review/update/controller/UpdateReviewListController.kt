package com.gpetuhov.android.hive.ui.epoxy.review.update.controller

import com.airbnb.epoxy.EpoxyController
import com.gpetuhov.android.hive.presentation.presenter.UpdateReviewFragmentPresenter
import com.gpetuhov.android.hive.ui.epoxy.review.update.models.updateReviewDetails

class UpdateReviewListController(private val presenter: UpdateReviewFragmentPresenter) : EpoxyController() {

    override fun buildModels() {
        updateReviewDetails {
            id("review_details")
            reviewText("")
            onReviewTextClick {
                // TODO: implement
            }

            rating(0.0F)
            onRatingChange {
                // TODO: implement
            }
        }
    }
}