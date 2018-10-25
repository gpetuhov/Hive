package com.gpetuhov.android.hive.presentation.presenter

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.gpetuhov.android.hive.application.HiveApp
import com.gpetuhov.android.hive.presentation.view.ProfileFragmentView
import com.gpetuhov.android.hive.repository.Repository
import timber.log.Timber
import javax.inject.Inject

// This is the presenter for ProfileFragment
// ALL (!!!) user interactions must be performed through this presenter ONLY!
@InjectViewState
class ProfileFragmentPresenter : MvpPresenter<ProfileFragmentView>() {

    companion object {
        private const val TAG = "ProfileFragPresenter"
    }

    @Inject lateinit var repo: Repository

    // Keeps current text entered in username dialog
    private var tempUsername = ""

    init {
        HiveApp.appComponent.inject(this)
    }

    fun showSignOutDialog() {
        // We must call ProfileFragmentView's methods not directly, but through ViewState only.
        // This way Moxy will remember current state of the view and will restore it,
        // when the view is recreated.
        viewState.showSignOutDialog()
    }

    fun dismissSignOutDialog() = viewState.dismissSignOutDialog()

    fun showDeleteUserDialog() = viewState.showDeleteUserDialog()

    fun dismissDeleteUserDialog() = viewState.dismissDeleteUserDialog()

    fun showUsernameDialog() = viewState.showUsernameDialog()

    fun getPrefill(): String {
        // Prefill dialog with currently entered text or current username
        val username = repo.currentUser.value?.username ?: ""
        return if (tempUsername != "") tempUsername else username
    }

    // TODO: set strategy to not remember this
    fun updateTempUsername(newTempUsername: String) {
        Timber.tag(TAG).d("TempUsername = $newTempUsername")
        tempUsername = newTempUsername
    }

    fun saveUsername() {
        Timber.tag(TAG).d("Username = $tempUsername")
        repo.updateUserUsername(tempUsername, { /* Do nothing */ }, { viewState.onSaveUsernameError() })
        dismissUsernameDialog()
    }

    fun dismissUsernameDialog() {
        tempUsername = ""
        viewState.dismissUsernameDialog()
    }
}