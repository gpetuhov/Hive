package com.gpetuhov.android.hive.presentation.presenter

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.gpetuhov.android.hive.presentation.view.ReviewsAllFragmentView

@InjectViewState
class ReviewsAllFragmentPresenter : MvpPresenter<ReviewsAllFragmentView>() {

    // === Public methods ===

    // --- Navigation ---

    fun navigateUp() = viewState.navigateUp()
}