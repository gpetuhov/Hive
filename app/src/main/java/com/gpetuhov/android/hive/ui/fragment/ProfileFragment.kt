package com.gpetuhov.android.hive.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.afollestad.materialdialogs.MaterialDialog
import com.gpetuhov.android.hive.R
import com.gpetuhov.android.hive.application.HiveApp
import com.gpetuhov.android.hive.managers.AuthManager
import com.pawegio.kandroid.toast
import kotlinx.android.synthetic.main.fragment_profile.*
import javax.inject.Inject

class ProfileFragment : Fragment() {

    @Inject lateinit var authManager: AuthManager

    private var signOutDialog: MaterialDialog? = null
    private var deleteUserDialog: MaterialDialog? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        HiveApp.appComponent.inject(this)

        initSignOutDialog()
        initDeleteUserDialog()

        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        user_name_textview.text = authManager.user.name
        user_email_textview.text = authManager.user.email

        signout_button.setOnClickListener { showSignOutDialog() }
        delete_user_button.setOnClickListener { showDeleteUserDialog() }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        dismissSignOutDialog()
        dismissDeleteUserDialog()
    }

    private fun initSignOutDialog() {
        if (context != null) {
            signOutDialog = MaterialDialog(context!!)
                .title(R.string.sign_out)
                .message(R.string.prompt_sign_out)
                .positiveButton { authManager.signOut(context, this::onSignOutSuccess, this::onSignOurError) }
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
                .positiveButton { authManager.deleteAccount(context, this::onDeleteAccountSuccess, this::onDeleteAccountError) }
                .negativeButton { /* Do nothing */ }
        }
    }

    private fun showDeleteUserDialog() {
        deleteUserDialog?.show()
    }

    private fun dismissDeleteUserDialog() {
        deleteUserDialog?.dismiss()
    }

    private fun onSignOutSuccess() {
        // Do nothing
    }

    private fun onSignOurError() {
        toast(R.string.sign_out_error)
    }

    private fun onDeleteAccountSuccess() {
        toast(R.string.delete_account_success)
    }

    private fun onDeleteAccountError() {
        toast(R.string.delete_account_error)
    }
}