package com.gpetuhov.android.hive.presentation.presenter

import android.net.Uri
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.gpetuhov.android.hive.application.HiveApp
import com.gpetuhov.android.hive.domain.interactor.*
import com.gpetuhov.android.hive.domain.repository.Repo
import com.gpetuhov.android.hive.domain.util.ResultMessages
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
    SaveUsernameInteractor.Callback,
    SaveDescriptionInteractor.Callback {

    @Inject lateinit var repo: Repo
    @Inject lateinit var resultMessages: ResultMessages

    private val signOutInteractor = SignOutInteractor(this)
    private val deleteUserInteractor = DeleteUserInteractor(this)
    private val saveUsernameInteractor = SaveUsernameInteractor(this)
    private val saveDescriptionInteractor = SaveDescriptionInteractor(this)

    // Keeps current text entered in username dialog
    private var tempUsername = ""

    // Keeps current text entered in description dialog
    private var tempDescription = ""

    init {
        HiveApp.appComponent.inject(this)
    }

    // === SignOutInteractor.Callback ===

    override fun onSignOutSuccess() = viewState.enableSignOutButton()

    override fun onSignOutError(errorMessage: String) {
        showToast(errorMessage)
        viewState.enableSignOutButton()
    }

    // === DeleteUserInteractor.Callback ===

    override fun onDeleteUserComplete(message: String) {
        showToast(message)
        viewState.enableDeleteUserButton()
    }

    // === SaveUsernameInteractor.Callback ===

    override fun onSaveUsernameError(errorMessage: String) = showToast(errorMessage)

    // === SaveDescriptionInteractor.Callback ===

    override fun onSaveDescriptionError(errorMessage: String) = showToast(errorMessage)

    // === Public methods ===
    // --- Sign out ---

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

    // --- Delete user ---

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

    // --- Change username ---

    fun showUsernameDialog() = viewState.showUsernameDialog()

    // Prefill username dialog with currently entered text or current username
    fun getUsernamePrefill() = if (tempUsername != "") tempUsername else repo.currentUserUsername()

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

    // --- Change description ---

    fun showDescriptionDialog() = viewState.showDescriptionDialog()

    // Prefill description dialog with currently entered text or current description
    fun getDescriptionPrefill() = if (tempDescription != "") tempDescription else repo.currentUserDescription()

    fun updateTempDescription(newTempDescription: String) {
        tempDescription = newTempDescription
    }

    fun saveDescription() {
        saveDescriptionInteractor.saveDescription(tempDescription)
        dismissDescriptionDialog()
    }

    fun dismissDescriptionDialog() {
        tempDescription = ""
        viewState.dismissDescriptionDialog()
    }

    // --- Change user pic ---

    fun chooseUserPic() = viewState.chooseUserPic()

    fun changeUserPic(selectedImageUri: Uri) = repo.changeUserPic(selectedImageUri) { showToast(resultMessages.getChangeUserPicErrorMessage()) }

    // --- Add offer ---

    fun addOffer() = viewState.addOffer()

    // --- Edit offer ---

    // TODO: implement this
    fun editOffer(offerUid: String) = showToast("Edit offer $offerUid")

    // === Private methods ===

    private fun showToast(message: String) {
        viewState.showToast(message)
    }
}