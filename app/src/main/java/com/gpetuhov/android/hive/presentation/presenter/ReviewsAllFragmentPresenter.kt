package com.gpetuhov.android.hive.presentation.presenter

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.gpetuhov.android.hive.domain.model.Review
import com.gpetuhov.android.hive.presentation.view.ReviewsAllFragmentView

@InjectViewState
class ReviewsAllFragmentPresenter : MvpPresenter<ReviewsAllFragmentView>() {

    var allReviewCount = 0
    var allRating = 0.0F
    var allReviews = mutableListOf<Review>()

    // === Public methods ===

    fun changeReviewsList(reviewList: MutableList<Review>) {
        allReviews = reviewList
        allReviewCount = allReviews.size

        calculateRating()

        viewState.updateUI()
    }

    // --- Navigation ---

    fun navigateUp() = viewState.navigateUp()

    // === Private methods ===

    private fun calculateRating() {
        if (allReviewCount == 0) {
            allRating = 0.0F
        } else {
            var ratingSum = 0.0F

            allReviews.forEach {
                ratingSum += it.rating
            }

            allRating = ratingSum / allReviewCount
        }
    }
}