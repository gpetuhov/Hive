package com.gpetuhov.android.hive.presentation.presenter

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.gpetuhov.android.hive.presentation.view.ReviewsFragmentView

@InjectViewState
class ReviewsFragmentPresenter : MvpPresenter<ReviewsFragmentView>() {

    var offerUid = ""

    // === Public methods ===

    fun postReview() = viewState.postReview(offerUid)

    fun navigateUp() = viewState.navigateUp()
}