package com.gpetuhov.android.hive.presentation.view

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

interface UpdateReviewFragmentView : MvpView {

    // === Quit offer update ===

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun showQuitReviewUpdateDialog()

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun dismissQuitReviewUpdateDialog()

    // === Common ===

    @StateStrategyType(SkipStrategy::class)
    fun navigateUp()
}
