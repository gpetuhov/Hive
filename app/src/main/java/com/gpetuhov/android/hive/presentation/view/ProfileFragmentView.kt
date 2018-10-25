package com.gpetuhov.android.hive.presentation.view

import com.arellomobile.mvp.MvpView

// This is the interface, that ProfileFragment must implement.
// Presenter calls methods of this interface.
interface ProfileFragmentView : MvpView {
    fun showSignOutDialog()
    fun dismissSignOutDialog()
    fun showDeleteUserDialog()
    fun dismissDeleteUserDialog()
    fun showUsernameDialog()
    fun dismissUsernameDialog()
}