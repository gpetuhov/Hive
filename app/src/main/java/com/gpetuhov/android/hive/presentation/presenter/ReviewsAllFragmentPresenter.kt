package com.gpetuhov.android.hive.presentation.presenter

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.gpetuhov.android.hive.domain.model.Review
import com.gpetuhov.android.hive.domain.model.User
import com.gpetuhov.android.hive.presentation.view.ReviewsAllFragmentView

@InjectViewState
class ReviewsAllFragmentPresenter : MvpPresenter<ReviewsAllFragmentView>() {

    var allReviewCount = 0
    var allRating = 0.0F
    var allReviews = mutableListOf<Review>()

    // === Public methods ===

    fun changeReviewsList(reviewList: MutableList<Review>) {
        allReviews = reviewList
        viewState.updateUI()
    }

    // We calculate rating based on user.offerList instead of all reviews,
    // because the result is slightly different,
    // and we need it to be the same as in user details.
    fun changeRating(user: User?) {
        allRating = 0.0F
        allReviewCount = 0

        if (user != null) {
            val offerList = user.offerList
            val activeOfferList = offerList.filter { it.isActive }
            val activeOfferWithReviewsList = activeOfferList.filter { it.reviewCount > 0 }

            activeOfferWithReviewsList.forEach {
                allRating += it.rating
                allReviewCount += it.reviewCount
            }

            val activeOfferWithReviewsCount = activeOfferWithReviewsList.size
            if (activeOfferWithReviewsCount > 0) allRating /= activeOfferWithReviewsCount
        }

        viewState.updateUI()
    }

    // --- Navigation ---

    fun navigateUp() = viewState.navigateUp()
}