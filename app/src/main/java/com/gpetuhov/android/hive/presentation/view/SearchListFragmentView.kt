package com.gpetuhov.android.hive.presentation.view

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

interface SearchListFragmentView : MvpView {

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun onSearchStart()

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun onSearchComplete()

    @StateStrategyType(SkipStrategy::class)
    fun updateUI()

    @StateStrategyType(SkipStrategy::class)
    fun showDetails(offerUid: String)

    @StateStrategyType(SkipStrategy::class)
    fun navigateUp()

    @StateStrategyType(SkipStrategy::class)
    fun showToast(message: String)
}