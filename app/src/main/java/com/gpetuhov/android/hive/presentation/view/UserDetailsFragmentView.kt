package com.gpetuhov.android.hive.presentation.view

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

interface UserDetailsFragmentView : MvpView {

    @StateStrategyType(SkipStrategy::class)
    fun navigateUp()

    @StateStrategyType(SkipStrategy::class)
    fun openChat()

    @StateStrategyType(SkipStrategy::class)
    fun openOffer(offerUid: String)

    @StateStrategyType(SkipStrategy::class)
    fun openPhotos(selectedPhotoUid: String)
}
