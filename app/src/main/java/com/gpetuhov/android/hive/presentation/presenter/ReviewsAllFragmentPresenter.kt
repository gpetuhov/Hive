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

        // TODO: calculate rating

        viewState.updateUI()
    }

    // --- Navigation ---

    fun navigateUp() = viewState.navigateUp()
}