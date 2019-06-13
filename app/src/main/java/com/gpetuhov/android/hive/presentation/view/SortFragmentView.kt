package com.gpetuhov.android.hive.presentation.view

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

interface SortFragmentView : MvpView {

    // === Navigation ===

    @StateStrategyType(SkipStrategy::class)
    fun navigateUp()
}