package com.gpetuhov.android.hive.presentation.view

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

interface UpdateOfferFragmentView : MvpView {

    // === Change title ===

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

    // === Quit offer update ===

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun showQuitOfferUpdateDialog()

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun dismissQuitOfferUpdateDialog()

    // === Change price ===

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun showPriceDialog()

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun dismissPriceDialog()

    // === Add photo ===

    @StateStrategyType(SkipStrategy::class)
    fun choosePhoto()

    // === Delete photo ===

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun showDeletePhotoDialog()

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun dismissDeletePhotoDialog()

    // === Open photos ===

    @StateStrategyType(SkipStrategy::class)
    fun openPhotos(photoUrlList: MutableList<String>)

    // === Open reviews ===

    @StateStrategyType(SkipStrategy::class)
    fun openReviews(userUid: String, offerUid: String)

    // === Common ===

    @StateStrategyType(SkipStrategy::class)
    fun updateUI()

    @StateStrategyType(SkipStrategy::class)
    fun navigateUp()

    @StateStrategyType(SkipStrategy::class)
    fun showToast(message: String)
}