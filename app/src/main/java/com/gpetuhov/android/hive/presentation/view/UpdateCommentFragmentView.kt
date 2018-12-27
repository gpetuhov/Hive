package com.gpetuhov.android.hive.presentation.view

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

interface UpdateCommentFragmentView : MvpView {

    // === Save comment ===

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun disableButtons()

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun enableButtons()

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun showProgress()

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun hideProgress()

    // === Quit comment update ===

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun showQuitCommentUpdateDialog()

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun dismissQuitCommentUpdateDialog()

    // === Common ===

    @StateStrategyType(SkipStrategy::class)
    fun navigateUp()

    @StateStrategyType(SkipStrategy::class)
    fun showToast(message: String)
}
