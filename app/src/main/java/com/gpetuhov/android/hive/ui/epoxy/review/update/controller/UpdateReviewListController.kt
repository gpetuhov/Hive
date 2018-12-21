package com.gpetuhov.android.hive.ui.epoxy.review.update.controller

import android.content.Context
import com.airbnb.epoxy.EpoxyController
import com.gpetuhov.android.hive.R
import com.gpetuhov.android.hive.application.HiveApp
import com.gpetuhov.android.hive.presentation.presenter.UpdateReviewFragmentPresenter
import com.gpetuhov.android.hive.ui.epoxy.review.update.models.updateReviewDetails
import javax.inject.Inject

class UpdateReviewListController(private val presenter: UpdateReviewFragmentPresenter) : EpoxyController() {

    @Inject lateinit var context: Context

    init {
        HiveApp.appComponent.inject(this)
    }

    override fun buildModels() {
        updateReviewDetails {
            id("review_details")
            reviewText(if (presenter.reviewText != "") presenter.reviewText else context.getString(R.string.add_review))
            onReviewTextClick {
                // TODO: implement
            }

            rating(presenter.rating)
            onRatingChange {
                // TODO: implement
            }
        }
    }
}