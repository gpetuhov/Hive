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

    // === Edit comment ===

    @StateStrategyType(SkipStrategy::class)
    fun editComment(offerUid: String, reviewUid: String, commentText: String)

    // === Update UI ===

    @StateStrategyType(SkipStrategy::class)
    fun updateUI()

    // === Common ===

    @StateStrategyType(SkipStrategy::class)
    fun navigateUp()

    @StateStrategyType(SkipStrategy::class)
    fun showToast(message: String)
}
