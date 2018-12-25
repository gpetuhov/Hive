package com.gpetuhov.android.hive.presentation.presenter

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.gpetuhov.android.hive.application.HiveApp
import com.gpetuhov.android.hive.domain.model.Review
import com.gpetuhov.android.hive.domain.repository.Repo
import com.gpetuhov.android.hive.presentation.view.ReviewsFragmentView
import javax.inject.Inject

@InjectViewState
class ReviewsFragmentPresenter : MvpPresenter<ReviewsFragmentView>() {

    @Inject lateinit var repo: Repo

    var offerUid = ""
    var isCurrentUser = false
    var reviewsList = mutableListOf<Review>()
    var reviewCount = 0
    var rating = 0.0F

    init {
        HiveApp.appComponent.inject(this)
    }

    // === Public methods ===

    fun changeReviewsList(reviewsList: MutableList<Review>) {
        this.reviewsList = reviewsList
        this.reviewCount = reviewsList.size

        if (reviewCount == 0) {
            rating = 0.0F
        } else {
            var ratingSum = 0.0F
            reviewsList.forEach { ratingSum += it.rating }
            rating = ratingSum / reviewCount
        }

        viewState.updateUI()
    }

    fun postReview() = viewState.postReview(offerUid)

    fun navigateUp() = viewState.navigateUp()

    // --- Lifecycle ---

    fun onResume() = repo.startGettingReviewsUpdates(offerUid, isCurrentUser)

    fun onPause() = repo.stopGettingReviewsUpdates()
}