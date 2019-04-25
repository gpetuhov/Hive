package com.gpetuhov.android.hive.presentation.presenter

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.gpetuhov.android.hive.application.HiveApp
import com.gpetuhov.android.hive.domain.model.Review
import com.gpetuhov.android.hive.domain.repository.Repo
import com.gpetuhov.android.hive.presentation.view.ReviewsAllFragmentView
import javax.inject.Inject

@InjectViewState
class ReviewsAllFragmentPresenter : MvpPresenter<ReviewsAllFragmentView>() {

    @Inject lateinit var repo: Repo

    var allReviewCount = 0
    var allRating = 0.0F
    var allReviews = mutableListOf<Review>()

    init {
        HiveApp.appComponent.inject(this)
    }

    // === Public methods ===

    fun getAllReviews(isCurrentUser: Boolean) {
        repo.getAllUserReviews(isCurrentUser) { reviewsList ->
            allReviews.addAll(reviewsList)
            allReviewCount = allReviews.size

            // TODO: calculate rating

            viewState.updateUI()
        }
    }

    // --- Navigation ---

    fun navigateUp() = viewState.navigateUp()
}