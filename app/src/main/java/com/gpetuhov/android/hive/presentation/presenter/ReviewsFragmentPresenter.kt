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
    var postReviewEnabled = true
    var deleteReviewUid = ""

    init {
        HiveApp.appComponent.inject(this)
    }

    // === Public methods ===

    // --- Init ---

    fun changeReviewsList(reviewsList: MutableList<Review>) {
        this.reviewsList = reviewsList
        this.reviewCount = reviewsList.size

        postReviewEnabled = true
        var reviewFromCurrentUserExist = false

        if (reviewCount == 0) {
            rating = 0.0F
        } else {
            var ratingSum = 0.0F

            reviewsList.forEach {
                ratingSum += it.rating

                if (it.authorUid == repo.currentUserUid()) {
                    reviewFromCurrentUserExist = true
                }
            }

            rating = ratingSum / reviewCount
        }

        // Hide post review button if current offer belongs to current user
        // or if current user has already posted review on the current offer.
        postReviewEnabled = !(isCurrentUser || reviewFromCurrentUserExist)

        viewState.updateUI()
    }

    // --- Edit review ---

    fun editReview(reviewUid: String) {
        // TODO: implement
    }

    // --- Delete review ---

    fun showDeleteReviewDialog(reviewUid: String) {
        deleteReviewUid = reviewUid
        viewState.showDeleteReviewDialog()
    }

    fun deleteReview() {
        viewState.dismissDeleteReviewDialog()

        // TODO: refactor this into interactor
        // TODO: handle on success and on error
        repo.deleteReview(offerUid, deleteReviewUid, {}, {})

//        deleteReviewInteractor.deleteReview(deleteReviewUid)
    }

    fun deleteReviewCancel() = viewState.dismissDeleteReviewDialog()

    // --- Post review ---

    fun postReview() = viewState.postReview(offerUid)

    // --- Navigation ---

    fun navigateUp() = viewState.navigateUp()

    // --- Lifecycle ---

    fun onResume() = repo.startGettingReviewsUpdates(offerUid, isCurrentUser)

    fun onPause() = repo.stopGettingReviewsUpdates()
}