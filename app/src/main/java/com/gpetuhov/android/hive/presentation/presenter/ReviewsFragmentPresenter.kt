package com.gpetuhov.android.hive.presentation.presenter

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.gpetuhov.android.hive.application.HiveApp
import com.gpetuhov.android.hive.domain.repository.Repo
import com.gpetuhov.android.hive.presentation.view.ReviewsFragmentView
import javax.inject.Inject

@InjectViewState
class ReviewsFragmentPresenter : MvpPresenter<ReviewsFragmentView>() {

    @Inject lateinit var repo: Repo

    var offerUid = ""
    var isCurrentUser = false

    init {
        HiveApp.appComponent.inject(this)
    }

    // === Public methods ===

    fun postReview() = viewState.postReview(offerUid)

    fun navigateUp() = viewState.navigateUp()

    // --- Lifecycle ---

    fun onResume() = repo.startGettingReviewsUpdates(offerUid, isCurrentUser)

    fun onPause() = repo.stopGettingReviewsUpdates()
}