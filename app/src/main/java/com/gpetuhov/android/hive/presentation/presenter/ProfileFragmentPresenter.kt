package com.gpetuhov.android.hive.presentation.presenter

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.gpetuhov.android.hive.application.HiveApp
import com.gpetuhov.android.hive.domain.interactor.DeleteUserInteractor
import com.gpetuhov.android.hive.domain.interactor.SaveUsernameInteractor
import com.gpetuhov.android.hive.domain.interactor.SignOutInteractor
import com.gpetuhov.android.hive.domain.repository.Repo
import com.gpetuhov.android.hive.presentation.view.ProfileFragmentView
import javax.inject.Inject

// This is the presenter for ProfileFragment
// ALL (!!!) user interactions must be performed through this presenter ONLY!
// Presenters only control views (in our case ProfileFragment).
// All business logic is implemented in the corresponding interactors at domain layer.
// Presenters invoke this logic by calling interactors' execute() methods.

@InjectViewState
class ProfileFragmentPresenter :
    MvpPresenter<ProfileFragmentView>(),
    SignOutInteractor.Callback,
    DeleteUserInteractor.Callback,
    SaveUsernameInteractor.Callback {

    @Inject lateinit var repo: Repo

    private val signOutInteractor = SignOutInteractor(this)
    private val deleteUserInteractor = DeleteUserInteractor(this)
    private val saveUsernameInteractor = SaveUsernameInteractor(this)

    // Keeps current text entered in username dialog
    private var tempUsername = ""

    init {
        HiveApp.appComponent.inject(this)
    }

    // === SignOutInteractor.Callback ===

    override fun onSignOutSuccess() = viewState.enableSignOutButton()

    override fun onSignOutError(errorMessage: String) {
        viewState.showToast(errorMessage)
        viewState.enableSignOutButton()
    }

    // === DeleteUserInteractor.Callback ===

    override fun onDeleteUserComplete(message: String) {
        viewState.showToast(message)
        viewState.enableDeleteUserButton()
    }

    // === SaveUsernameInteractor.Callback ===

    override fun onSaveUsernameError(errorMessage: String) = viewState.showToast(errorMessage)

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

    fun deleteUser() {
        viewState.dismissDeleteUserDialog()
        deleteUserInteractor.execute()
    }

    fun deleteUserCancel() {
        viewState.dismissDeleteUserDialog()
        viewState.enableDeleteUserButton()
    }

    fun showUsernameDialog() = viewState.showUsernameDialog()

    // Prefill dialog with currently entered text or current username
    fun getPrefill() = if (tempUsername != "") tempUsername else repo.currentUserUsername()

    fun updateTempUsername(newTempUsername: String) {
        tempUsername = newTempUsername
    }

    fun saveUsername() {
        saveUsernameInteractor.saveUsername(tempUsername)
        dismissUsernameDialog()
    }

    fun dismissUsernameDialog() {
        tempUsername = ""
        viewState.dismissUsernameDialog()
    }
}