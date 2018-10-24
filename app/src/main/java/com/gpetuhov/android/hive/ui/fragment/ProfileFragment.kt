package com.gpetuhov.android.hive.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.input.input
import com.gpetuhov.android.hive.R
import com.gpetuhov.android.hive.application.HiveApp
import com.gpetuhov.android.hive.databinding.FragmentProfileBinding
import com.gpetuhov.android.hive.managers.AuthManager
import com.gpetuhov.android.hive.model.CurrentUserViewModel
import com.gpetuhov.android.hive.model.User
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
        binding.handler = this

        val viewModel = ViewModelProviders.of(this).get(CurrentUserViewModel::class.java)

        // Every time current user data changes, update binding object with new data
        viewModel.currentUser.observe(this, Observer<User> { user ->
            binding.user = user
        })

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
                .input(hintRes = R.string.enter_username, prefill = repo.currentUser.value?.username) { dialog, text ->
                    saveUsername(text.toString())
                }
                .positiveButton { /* Do nothing */ }
                .negativeButton { /* Do nothing */ }
        }
    }

    private fun saveUsername(username: String) {
        Timber.tag(TAG).d("Username = $username")
        repo.updateUserUsername(username, { /* Do nothing */ }, { onUsernameUpdateError() })
    }

    private fun onUsernameUpdateError() {
        toast(R.string.username_save_error)
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
                .positiveButton { signOut() }
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

    private fun signOut() {
        authManager.signOut(context) { toast(R.string.sign_out_error) }
    }

    private fun startDeleteAccount() {
        // When deleting account, delete user data from backend first
        // (because after deleting account, the user will be unauthorized,
        // and updating backend will be impossible)

        // Proceed with account deletion, only if user data has been successfully deleted
        repo.deleteUserDataRemote(this::deleteAccount, this::onDeleteAccountError)
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