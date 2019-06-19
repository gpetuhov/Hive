package com.gpetuhov.android.hive.presentation.view

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

interface ChatArchiveFragmentView : MvpView {

    @StateStrategyType(SkipStrategy::class)
    fun scrollDown()

    @StateStrategyType(SkipStrategy::class)
    fun showScrollDownButton()

    @StateStrategyType(SkipStrategy::class)
    fun hideScrollDownButton()

    @StateStrategyType(SkipStrategy::class)
    fun openUserDetails()

    @StateStrategyType(SkipStrategy::class)
    fun navigateUp()
}