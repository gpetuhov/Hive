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
    fun addOffer()

    // === Common ===

    // We don't need to keep this command in the queue,
    // just run this once on error and don't repeat on view being recreated.
    @StateStrategyType(SkipStrategy::class)
    fun showToast(message: String)
}