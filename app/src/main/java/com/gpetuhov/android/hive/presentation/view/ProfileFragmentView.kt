package com.gpetuhov.android.hive.presentation.view

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

// This is the interface, that ProfileFragment must implement.
// Presenter calls methods of this interface.
interface ProfileFragmentView : MvpView {

    // === Sign out ===

    // We don't need to keep all commands in the queue, just the last one
    @StateStrategyType(AddToEndSingleStrategy::class)
    fun showSignOutDialog()

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun dismissSignOutDialog()

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun enableSignOutButton()

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun disableSignOutButton()

    // === Delete user ===

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun showDeleteUserDialog()

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun dismissDeleteUserDialog()

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun enableDeleteUserButton()

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun disableDeleteUserButton()

    // === Change username ===

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun showUsernameDialog()

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun dismissUsernameDialog()

    // === Change description ===

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun showDescriptionDialog()

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun dismissDescriptionDialog()

    // === Change user pic ===

    @StateStrategyType(SkipStrategy::class)
    fun chooseUserPic()

    // === Add offer ===

    @StateStrategyType(SkipStrategy::class)
    fun updateOffer(offerUid: String)

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

    // === Change phone ===

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun showPhoneDialog()

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun dismissPhoneDialog()

    // === Change email ===

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun showEmailDialog()

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun dismissEmailDialog()

    // === Change Skype ===

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun showSkypeDialog()

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun dismissSkypeDialog()

    // === Change Facebook ===

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun showFacebookDialog()

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun dismissFacebookDialog()

    // === Change Twitter ===

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun showTwitterDialog()

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun dismissTwitterDialog()

    // === Change Instagram ===

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun showInstagramDialog()

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun dismissInstagramDialog()

    // === Change YouTube ===

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun showYouTubeDialog()

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun dismissYouTubeDialog()

    // === Change website ===

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun showWebsiteDialog()

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun dismissWebsiteDialog()

    // === Change residence ===

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun showResidenceDialog()

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun dismissResidenceDialog()

    // === Change language ===

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun showLanguageDialog()

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun dismissLanguageDialog()

    // === Change education ===

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun showEducationDialog()

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun dismissEducationDialog()

    // === Change work ===

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun showWorkDialog()

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun dismissWorkDialog()

    // === Change interests ===

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun showInterestsDialog()

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun dismissInterestsDialog()

    // === Change status ===

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun showStatusDialog()

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun dismissStatusDialog()

    // === Open privacy policy ===

    @StateStrategyType(SkipStrategy::class)
    fun openPrivacyPolicy()

    // === Open all reviews ===

    @StateStrategyType(SkipStrategy::class)
    fun openAllReviews()

    // === Open award ===

    @StateStrategyType(SkipStrategy::class)
    fun openAward(awardType: Int)

    // === Open congratulation ===

    @StateStrategyType(SkipStrategy::class)
    fun openCongratulation(newAwardsList: MutableList<Int>)

    // === Open user activity ===

    @StateStrategyType(SkipStrategy::class)
    fun openUserActivity(userActivityType: Int)

    // === Open user pic

    @StateStrategyType(SkipStrategy::class)
    fun openUserPic(userPicUrl: String)

    // === Delete user pic ===

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun showDeleteUserPicDialog()

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun dismissDeleteUserPicDialog()

    // === Common ===

    // We don't need to keep this command in the queue,
    // just run this once on error and don't repeat on view being recreated.
    @StateStrategyType(SkipStrategy::class)
    fun showToast(message: String)
}