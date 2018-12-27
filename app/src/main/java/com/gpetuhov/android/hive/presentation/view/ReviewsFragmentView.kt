package com.gpetuhov.android.hive.presentation.view

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

interface ReviewsFragmentView : MvpView {

    // === Post review ===

    @StateStrategyType(SkipStrategy::class)
    fun postReview(offerUid: String)

    // === Edit review ===

    @StateStrategyType(SkipStrategy::class)
    fun editReview(offerUid: String, reviewUid: String, reviewText: String, rating: Float)

    // === Delete review ===

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun showDeleteReviewDialog()

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun dismissDeleteReviewDialog()

    // === Update UI ===

    @StateStrategyType(SkipStrategy::class)
    fun updateUI()

    // === Navigate ===

    @StateStrategyType(SkipStrategy::class)
    fun navigateUp()
}
