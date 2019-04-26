package com.gpetuhov.android.hive.presentation.view

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

interface ReviewsAllFragmentView : MvpView {

    // === Progress bar ===

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun showProgress()

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun hideProgress()

    // === Update UI ===

    @StateStrategyType(SkipStrategy::class)
    fun updateUI()

    // === Show offer ===

    @StateStrategyType(SkipStrategy::class)
    fun openOffer(offerUid: String)

    @StateStrategyType(SkipStrategy::class)
    fun updateOffer(offerUid: String)

    // === Common ===

    @StateStrategyType(SkipStrategy::class)
    fun navigateUp()
}
