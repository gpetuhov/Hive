package com.gpetuhov.android.hive.presentation.view

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

interface SortFragmentView : MvpView {

    // === Clear filter ===

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun showClearSortDialog()

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun dismissClearSortDialog()

    // === Navigation ===

    @StateStrategyType(SkipStrategy::class)
    fun navigateUp()

    // === Update UI ===

    @StateStrategyType(SkipStrategy::class)
    fun updateUI()
}