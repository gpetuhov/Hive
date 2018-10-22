package com.gpetuhov.android.hive.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.afollestad.materialdialogs.MaterialDialog
import com.gpetuhov.android.hive.R
import com.gpetuhov.android.hive.application.HiveApp
import com.gpetuhov.android.hive.databinding.FragmentProfileBinding
import com.gpetuhov.android.hive.managers.AuthManager
import com.gpetuhov.android.hive.repository.Repository
import com.gpetuhov.android.hive.util.isOnline
import com.pawegio.kandroid.toast
import javax.inject.Inject

class ProfileFragment : Fragment() {

    @Inject lateinit var authManager: AuthManager
    @Inject lateinit var repo: Repository

    private var signOutDialog: MaterialDialog? = null
    private var deleteUserDialog: MaterialDialog? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        HiveApp.appComponent.inject(this)

        initSignOutDialog()
        initDeleteUserDialog()

        val binding: FragmentProfileBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false)
        binding.user = authManager.user
        binding.handler = this

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
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
        authManager.user.isOnline = false

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