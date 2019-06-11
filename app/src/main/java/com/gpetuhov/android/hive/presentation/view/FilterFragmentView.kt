package com.gpetuhov.android.hive.presentation.view

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

interface FilterFragmentView : MvpView {

    // === Clear filter ===

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun showClearFilterDialog()

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun dismissClearFilterDialog()

    // === Navigation ===

    @StateStrategyType(SkipStrategy::class)
    fun navigateUp()

    // === Update UI ===

    @StateStrategyType(SkipStrategy::class)
    fun updateUI()
}