package com.gpetuhov.android.hive.presentation.view

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.SingleStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

// This is the interface, that ProfileFragment must implement.
// Presenter calls methods of this interface.
//@StateStrategyType(SingleStateStrategy::class)
interface ProfileFragmentView : MvpView {
    fun showSignOutDialog()
    fun dismissSignOutDialog()
    fun showDeleteUserDialog()
    fun dismissDeleteUserDialog()
    fun showUsernameDialog()
    fun dismissUsernameDialog()
    fun onSaveUsernameError()
}