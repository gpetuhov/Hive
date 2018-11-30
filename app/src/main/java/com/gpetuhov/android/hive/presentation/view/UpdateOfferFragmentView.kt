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

    // === Save offer ===

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun enableSaveOfferButton()

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun disableSaveOfferButton()

    // === Delete offer ===

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun enableDeleteOfferButton()

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun disableDeleteOfferButton()

    // === Progress bar ===

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun showProgress()

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun hideProgress()

    // === Common ===

    @StateStrategyType(SkipStrategy::class)
    fun updateUI()

    @StateStrategyType(SkipStrategy::class)
    fun navigateUp()

    @StateStrategyType(SkipStrategy::class)
    fun showToast(message: String)
}