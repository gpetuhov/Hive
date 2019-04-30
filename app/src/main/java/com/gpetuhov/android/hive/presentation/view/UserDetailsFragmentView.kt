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
    fun openPhotos(photoUrlList: MutableList<String>)

    @StateStrategyType(SkipStrategy::class)
    fun openLocation(userUid: String)

    @StateStrategyType(SkipStrategy::class)
    fun openAllReviews()

    @StateStrategyType(SkipStrategy::class)
    fun dialPhone(phone: String)

    @StateStrategyType(SkipStrategy::class)
    fun sendEmail(email: String)

    @StateStrategyType(SkipStrategy::class)
    fun callSkype(skype: String)

    @StateStrategyType(SkipStrategy::class)
    fun openFacebook(facebook: String)

    @StateStrategyType(SkipStrategy::class)
    fun openTwitter(twitter: String)

    @StateStrategyType(SkipStrategy::class)
    fun openInstagram(instagram: String)

    @StateStrategyType(SkipStrategy::class)
    fun openYouTube(youTube: String)

    @StateStrategyType(SkipStrategy::class)
    fun openWeblink(weblink: String)

    @StateStrategyType(SkipStrategy::class)
    fun showToast(message: String)
}
