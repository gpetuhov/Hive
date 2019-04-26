package com.gpetuhov.android.hive.presentation.presenter

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.gpetuhov.android.hive.application.HiveApp
import com.gpetuhov.android.hive.domain.model.Review
import com.gpetuhov.android.hive.domain.model.User
import com.gpetuhov.android.hive.domain.repository.Repo
import com.gpetuhov.android.hive.presentation.view.ReviewsAllFragmentView
import javax.inject.Inject

@InjectViewState
class ReviewsAllFragmentPresenter : MvpPresenter<ReviewsAllFragmentView>() {

    @Inject lateinit var repo: Repo

    var allReviewCount = 0
    var allRating = 0.0F
    var allReviews = mutableListOf<Review>()

    private var hasData = false

    init {
        HiveApp.appComponent.inject(this)
    }

    // === Public methods ===

    fun getAllReviews(isCurrentUser: Boolean) {
        // This is needed to prevent loading reviews on every screen rotate
        if (!hasData) {
            hasData = true

            viewState.showProgress()

            val user = if (isCurrentUser) repo.currentUser().value else repo.secondUser().value
            calculateRating(user)

            // Notice, that we just load all user reviews WITHOUT listening to updates.
            repo.getAllUserReviews(isCurrentUser) { reviewList ->
                allReviews = reviewList
                viewState.hideProgress()
                viewState.updateUI()
            }
        }
    }

    // --- Navigation ---

    fun navigateUp() = viewState.navigateUp()

    // === Private methods ===

    // We calculate rating based on user.offerList instead of all reviews,
    // because the result is slightly different,
    // and we need it to be the same as in user details.
    private fun calculateRating(user: User?) {
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
    }
}