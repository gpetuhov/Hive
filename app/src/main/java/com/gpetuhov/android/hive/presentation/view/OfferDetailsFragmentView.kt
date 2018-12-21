package com.gpetuhov.android.hive.presentation.view

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

interface OfferDetailsFragmentView : MvpView {

    @StateStrategyType(SkipStrategy::class)
    fun navigateUp()

    @StateStrategyType(SkipStrategy::class)
    fun openChat()

    @StateStrategyType(SkipStrategy::class)
    fun openUserDetails()

    @StateStrategyType(SkipStrategy::class)
    fun openPhotos(photoUrlList: MutableList<String>)

    @StateStrategyType(SkipStrategy::class)
    fun openLocation(userUid: String)

    @StateStrategyType(SkipStrategy::class)
    fun openReviews(offerUid: String)

    @StateStrategyType(SkipStrategy::class)
    fun postReview(offerUid: String)

    @StateStrategyType(SkipStrategy::class)
    fun showToast(message: String)
}