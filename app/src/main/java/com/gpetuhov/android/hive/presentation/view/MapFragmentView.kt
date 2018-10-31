package com.gpetuhov.android.hive.presentation.view

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

interface MapFragmentView : MvpView {

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun onSearchStart()

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun onSearchComplete()

    @StateStrategyType(SkipStrategy::class)
    fun showToast(message: String)
}