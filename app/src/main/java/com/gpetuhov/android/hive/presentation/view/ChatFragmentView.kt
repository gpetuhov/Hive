package com.gpetuhov.android.hive.presentation.view

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

interface ChatFragmentView : MvpView {

    @StateStrategyType(SkipStrategy::class)
    fun navigateUp()

    @StateStrategyType(SkipStrategy::class)
    fun clearMessageText()

    @StateStrategyType(SkipStrategy::class)
    fun sendButtonEnabled(isEnabled: Boolean)

    @StateStrategyType(SkipStrategy::class)
    fun showToast(message: String)
}