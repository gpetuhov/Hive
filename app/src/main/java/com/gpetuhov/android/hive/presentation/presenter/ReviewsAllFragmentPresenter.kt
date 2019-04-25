package com.gpetuhov.android.hive.presentation.presenter

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.gpetuhov.android.hive.presentation.view.ReviewsAllFragmentView

@InjectViewState
class ReviewsAllFragmentPresenter : MvpPresenter<ReviewsAllFragmentView>() {

    var reviewCount = 0
    var rating = 0.0F

    // === Public methods ===

    // --- Navigation ---

    fun navigateUp() = viewState.navigateUp()
}