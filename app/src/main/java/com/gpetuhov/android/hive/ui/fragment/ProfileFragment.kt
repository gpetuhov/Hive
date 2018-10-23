package com.gpetuhov.android.hive.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.input.input
import com.gpetuhov.android.hive.R
import com.gpetuhov.android.hive.application.HiveApp
import com.gpetuhov.android.hive.databinding.FragmentProfileBinding
import com.gpetuhov.android.hive.managers.AuthManager
import com.gpetuhov.android.hive.repository.Repository
import com.gpetuhov.android.hive.util.isOnline
import com.pawegio.kandroid.toast
import timber.log.Timber
import javax.inject.Inject

class ProfileFragment : Fragment() {

    companion object {
        private const val TAG = "ProfileFragment"
    }

    @Inject lateinit var authManager: AuthManager
    @Inject lateinit var repo: Repository

    private var usernameDialog: MaterialDialog? = null
    private var signOutDialog: MaterialDialog? = null
    private var deleteUserDialog: MaterialDialog? = null
    private lateinit var binding: FragmentProfileBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        HiveApp.appComponent.inject(this)

        initUsernameDialog()
        initSignOutDialog()
        initDeleteUserDialog()

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false)
        binding.user = authManager.currentUser.value
        binding.handler = this

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        dismissUsernameDialog()
        dismissSignOutDialog()
        dismissDeleteUserDialog()
    }

    fun onSignOutButtonClick(view: View) {
        if (isOnline(context)) {
            showSignOutDialog()
        } else {
            toast(R.string.sign_out_no_network)
        }
    }

    fun onDeleteAccountButtonClick(view: View) {
        if (isOnline(context)) {
            showDeleteUserDialog()
        } else {
            toast(R.string.delete_account_no_network)
        }
    }

    fun onUsernameClick(view: View) {
        // We need to reinitialize username dialog every time,
        // so that it will be prefilled with current username.
        initUsernameDialog()
        showUsernameDialog()
    }

    private fun initUsernameDialog() {
        if (context != null) {
            usernameDialog = MaterialDialog(context!!)
                .title(R.string.username)
                .input(hintRes = R.string.enter_username, prefill = authManager.currentUser.value?.username) { dialog, text ->
                    saveUsername(text.toString())
                }
                .positiveButton { /* Do nothing */ }
                .negativeButton { /* Do nothing */ }
        }
    }

    private fun saveUsername(username: String) {
        Timber.tag(TAG).d("Username = $username")
        val oldUsername = authManager.currentUser.value?.username ?: ""
        val currentUser = authManager.currentUser.value
        currentUser?.username = username
        authManager.currentUser.value = currentUser
        repo.updateUserUsername({ /* Do nothing */ }, { onUsernameUpdateError(oldUsername) })
        updateUI()
    }

    private fun onUsernameUpdateError(oldUsername: String) {
        val currentUser = authManager.currentUser.value
        currentUser?.username = oldUsername
        authManager.currentUser.value = currentUser
        toast(R.string.username_save_error)
        updateUI()
    }

    private fun updateUI() {
        binding.user = authManager.currentUser.value
    }

    private fun showUsernameDialog() {
        usernameDialog?.show()
    }

    private fun dismissUsernameDialog() {
        usernameDialog?.dismiss()
    }

    private fun initSignOutDialog() {
        if (context != null) {
            signOutDialog = MaterialDialog(context!!)
                .title(R.string.sign_out)
                .message(R.string.prompt_sign_out)
                .positiveButton { startSignOut() }
                .negativeButton { /* Do nothing */ }
        }
    }

    private fun showSignOutDialog() {
        signOutDialog?.show()
    }

    private fun dismissSignOutDialog() {
        signOutDialog?.dismiss()
    }

    private fun initDeleteUserDialog() {
        if (context != null) {
            deleteUserDialog = MaterialDialog(context!!)
                .title(R.string.delete_account)
                .message(R.string.prompt_delete_account)
                .positiveButton { startDeleteAccount() }
                .negativeButton { /* Do nothing */ }
        }
    }

    private fun showDeleteUserDialog() {
        deleteUserDialog?.show()
    }

    private fun dismissDeleteUserDialog() {
        deleteUserDialog?.dismiss()
    }

    private fun startSignOut() {
        // When signing out, first set status to offline,
        // and only after that sign out (because after signing out,
        // updating backend will be impossible)
        val currentUser = authManager.currentUser.value
        currentUser?.isOnline = false
        authManager.currentUser.value = currentUser

        // We are signing out regardless of online status update success
        repo.updateUserOnlineStatus(this::signOut, this::signOut)
    }

    private fun signOut() {
        authManager.signOut(context, this::onSignOutSuccess, this::onSignOurError)
    }

    private fun onSignOutSuccess() {
        // Do nothing
    }

    private fun onSignOurError() {
        toast(R.string.sign_out_error)
    }

    private fun startDeleteAccount() {
        // When deleting account, delete user data from backend first
        // (because after deleting account, the user will be unauthorized,
        // and updating backend will be impossible)

        // Proceed with account deletion, only if user data has been successfully deleted
        repo.deleteUserData(this::deleteAccount, this::onDeleteAccountError)
    }

    private fun deleteAccount() {
        authManager.deleteAccount(context, this::onDeleteAccountSuccess, this::onDeleteAccountError)
    }

    private fun onDeleteAccountSuccess() {
        toast(R.string.delete_account_success)
    }

    private fun onDeleteAccountError() {
        toast(R.string.delete_account_error)
    }
}