package com.gpetuhov.android.hive.presentation.view

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

interface UpdateReviewFragmentView : MvpView {

    // === Save review ===

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun disableButtons()

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun enableButtons()

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun showProgress()

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun hideProgress()

    // === Quit review update ===

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun showQuitReviewUpdateDialog()

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun dismissQuitReviewUpdateDialog()

    // === Common ===

    @StateStrategyType(SkipStrategy::class)
    fun navigateUp()

    @StateStrategyType(SkipStrategy::class)
    fun showToast(message: String)
}
