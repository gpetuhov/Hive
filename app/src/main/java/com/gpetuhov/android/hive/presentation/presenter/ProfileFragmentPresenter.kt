package com.gpetuhov.android.hive.presentation.presenter

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.gpetuhov.android.hive.presentation.view.ProfileFragmentView

// This is the presenter for ProfileFragment
// ALL (!!!) user interactions must be performed through this presenter ONLY!
@InjectViewState
class ProfileFragmentPresenter : MvpPresenter<ProfileFragmentView>() {

    fun showSignOutDialog() {
        // We must call ProfileFragmentView's methods not directly, but through ViewState only.
        // This way Moxy will remember current state of the view and will restore it,
        // when the view is recreated.
        viewState.showSignOutDialog()
    }

    fun dismissSignOutDialog() {
        viewState.dismissSignOutDialog()
    }

    fun showDeleteUserDialog() {
        viewState.showDeleteUserDialog()
    }

    fun dismissDeleteUserDialog() {
        viewState.dismissDeleteUserDialog()
    }
}