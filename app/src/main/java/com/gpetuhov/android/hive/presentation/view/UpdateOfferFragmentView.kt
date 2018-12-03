package com.gpetuhov.android.hive.presentation.view

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

interface UpdateOfferFragmentView : MvpView {

    // === Change username ===

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun showTitleDialog()

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun dismissTitleDialog()

    // === Change description ===

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun showDescriptionDialog()

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun dismissDescriptionDialog()

    // === Save, delete offer ===

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun disableButtons()

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun enableButtons()

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun showProgress()

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun hideProgress()

    // === Delete offer ===

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun showDeleteOfferDialog()

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun dismissDeleteOfferDialog()

    // === Common ===

    @StateStrategyType(SkipStrategy::class)
    fun updateUI()

    @StateStrategyType(SkipStrategy::class)
    fun navigateUp()

    @StateStrategyType(SkipStrategy::class)
    fun showToast(message: String)
}