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
import com.gpetuhov.android.hive.ui.viewmodel.CurrentUserViewModel
import com.gpetuhov.android.hive.model.User
import com.gpetuhov.android.hive.repository.Repository
import com.gpetuhov.android.hive.util.isOnline
import com.pawegio.kandroid.toast
import timber.log.Timber
import javax.inject.Inject

class ProfileFragment : Fragment() {

    companion object {
        private const val TAG = "ProfileFragment"
        private const val USERNAME_DIALOG_SHOWING_KEY = "isUsernameDialogShowing"
        private const val SIGNOUT_DIALOG_SHOWING_KEY = "isSignOutDialogShowing"
        private const val DELETE_USER_DIALOG_SHOWING_KEY = "isDeleteUserDialogShowing"
    }

    @Inject lateinit var authManager: AuthManager
    @Inject lateinit var repo: Repository

    private var usernameDialog: MaterialDialog? = null
    private var signOutDialog: MaterialDialog? = null
    private var deleteUserDialog: MaterialDialog? = null
    private var binding: FragmentProfileBinding? = null

    private var isUsernameDialogShowing = false
    private var isSignOutDialogShowing = false
    private var isDeleteUserDialogShowing = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        HiveApp.appComponent.inject(this)

        initSignOutDialog()
        initDeleteUserDialog()

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false)
        binding?.handler = this

        val viewModel = ViewModelProviders.of(this).get(CurrentUserViewModel::class.java)

        // Every time current user data changes, update binding object with new data
        viewModel.currentUser.observe(this, Observer<User> { user ->
            binding?.user = user
        })

        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (savedInstanceState != null) {
            isUsernameDialogShowing = savedInstanceState.getBoolean(USERNAME_DIALOG_SHOWING_KEY, false)
            isSignOutDialogShowing = savedInstanceState.getBoolean(SIGNOUT_DIALOG_SHOWING_KEY, false)
            isDeleteUserDialogShowing = savedInstanceState.getBoolean(DELETE_USER_DIALOG_SHOWING_KEY, false)

            if (isUsernameDialogShowing) onUsernameClick()
            if (isSignOutDialogShowing) showSignOutDialog()
            if (isDeleteUserDialogShowing) showDeleteUserDialog()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putBoolean(USERNAME_DIALOG_SHOWING_KEY, isUsernameDialogShowing)
        outState.putBoolean(SIGNOUT_DIALOG_SHOWING_KEY, isSignOutDialogShowing)
        outState.putBoolean(DELETE_USER_DIALOG_SHOWING_KEY, isDeleteUserDialogShowing)
        super.onSaveInstanceState(outState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        dismissUsernameDialog()
        dismissSignOutDialog()
        dismissDeleteUserDialog()
    }

    fun onSignOutButtonClick() {
        if (isOnline(context)) {
            showSignOutDialog()
        } else {
            toast(R.string.sign_out_no_network)
        }
    }

    fun onDeleteAccountButtonClick() {
        if (isOnline(context)) {
            showDeleteUserDialog()
        } else {
            toast(R.string.delete_account_no_network)
        }
    }

    fun onUsernameClick() {
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
                .positiveButton { isUsernameDialogShowing = false }
                .negativeButton { isUsernameDialogShowing = false }
        }
    }

    private fun saveUsername(username: String) {
        Timber.tag(TAG).d("Username = $username")
        repo.updateUserUsername(username, { /* Do nothing */ }, { toast(R.string.username_save_error) })
    }

    private fun showUsernameDialog() {
        usernameDialog?.show()
        isUsernameDialogShowing = true
    }

    private fun dismissUsernameDialog() {
        usernameDialog?.dismiss()
        isUsernameDialogShowing = false
    }

    private fun initSignOutDialog() {
        if (context != null) {
            signOutDialog = MaterialDialog(context!!)
                .title(R.string.sign_out)
                .message(R.string.prompt_sign_out)
                .positiveButton {
                    isSignOutDialogShowing = false
                    signOut()
                }
                .negativeButton { isSignOutDialogShowing = false }
        }
    }

    private fun showSignOutDialog() {
        signOutDialog?.show()
        isSignOutDialogShowing = true
    }

    private fun dismissSignOutDialog() {
        signOutDialog?.dismiss()
        isSignOutDialogShowing = false
    }

    private fun initDeleteUserDialog() {
        if (context != null) {
            deleteUserDialog = MaterialDialog(context!!)
                .title(R.string.delete_account)
                .message(R.string.prompt_delete_account)
                .positiveButton {
                    isDeleteUserDialogShowing = false
                    deleteAccount()
                }
                .negativeButton { isDeleteUserDialogShowing = false }
        }
    }

    private fun showDeleteUserDialog() {
        deleteUserDialog?.show()
        isDeleteUserDialogShowing = true
    }

    private fun dismissDeleteUserDialog() {
        deleteUserDialog?.dismiss()
        isDeleteUserDialogShowing = false
    }

    private fun signOut() {
        authManager.signOut(context) { toast(R.string.sign_out_error) }
    }

    private fun deleteAccount() {
        authManager.deleteAccount(
            context,
            { toast(R.string.delete_account_success) },
            { toast(R.string.delete_account_error) }
        )
    }
}