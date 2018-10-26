package com.gpetuhov.android.hive.presentation.presenter

import android.content.Context
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.gpetuhov.android.hive.application.HiveApp
import com.gpetuhov.android.hive.domain.interactor.SignOutInteractor
import com.gpetuhov.android.hive.managers.AuthManager
import com.gpetuhov.android.hive.presentation.view.ProfileFragmentView
import com.gpetuhov.android.hive.repository.Repository
import com.gpetuhov.android.hive.util.isOnline
import timber.log.Timber
import javax.inject.Inject

// This is the presenter for ProfileFragment
// ALL (!!!) user interactions must be performed through this presenter ONLY!
// Presenters only control views (in our case ProfileFragment).
// All business logic is implemented in the corresponding interactors at domain layer.
// Presenters invoke this logic by calling interactors' execute() methods.

@InjectViewState
class ProfileFragmentPresenter :
    MvpPresenter<ProfileFragmentView>(),
    SignOutInteractor.Callback {

    companion object {
        private const val TAG = "ProfileFragPresenter"
    }

    @Inject lateinit var repo: Repository
    @Inject lateinit var authManager: AuthManager

    private val signOutInteractor = SignOutInteractor(this)

    // Keeps current text entered in username dialog
    private var tempUsername = ""

    init {
        HiveApp.appComponent.inject(this)
    }

    // === SignOutInteractor.Callback ===

    override fun onSignOutSuccess(message: String) = finishSignOut(message)

    override fun onSignOutError(errorMessage: String) = finishSignOut(errorMessage)

    // === Public methods ===

    // We must call ProfileFragmentView's methods not directly, but through ViewState only.
    // This way Moxy will remember current state of the view and will restore it,
    // when the view is recreated.
    fun showSignOutDialog() {
        viewState.disableSignOutButton()
        viewState.showSignOutDialog()
    }

    fun signOut() {
        viewState.dismissSignOutDialog()
        signOutInteractor.execute()
    }

    fun signOutCancel() {
        viewState.dismissSignOutDialog()
        viewState.enableSignOutButton()
    }

    fun showDeleteUserDialog() {
        viewState.disableDeleteUserButton()
        viewState.showDeleteUserDialog()
    }

    fun deleteUser(context: Context?) {
        viewState.dismissDeleteUserDialog()

        // Try to delete account if online only
        if (isOnline(context)) {
            authManager.deleteAccount(context,
                {
                    viewState.onDeleteUserSuccess()
                    viewState.enableDeleteUserButton()
                },
                {
                    viewState.onDeleteUserError()
                    viewState.enableDeleteUserButton()
                }
            )
        } else {
            viewState.onDeleteUserNetworkError()
            viewState.enableDeleteUserButton()
        }
    }

    fun deleteUserCancel() {
        viewState.dismissDeleteUserDialog()
        viewState.enableDeleteUserButton()
    }

    fun showUsernameDialog() = viewState.showUsernameDialog()

    fun getPrefill(): String {
        // Prefill dialog with currently entered text or current username
        val username = repo.currentUser.value?.username ?: ""
        return if (tempUsername != "") tempUsername else username
    }

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

    private fun finishSignOut(message: String) {
        viewState.showToast(message)
        viewState.enableSignOutButton()
    }
}