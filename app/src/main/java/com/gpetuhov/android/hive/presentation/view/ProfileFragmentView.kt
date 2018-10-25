package com.gpetuhov.android.hive.presentation.view

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

// This is the interface, that ProfileFragment must implement.
// Presenter calls methods of this interface.
interface ProfileFragmentView : MvpView {
    fun showSignOutDialog()
    fun dismissSignOutDialog()
    fun showDeleteUserDialog()
    fun dismissDeleteUserDialog()
    fun showUsernameDialog()
    fun dismissUsernameDialog()

    // We don't need to keep this command in the queue,
    // just run this once on error and don't repeat on view being recreated.
    @StateStrategyType(SkipStrategy::class)
    fun onSaveUsernameError()
}