package com.gpetuhov.android.hive.ui.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.input.getInputField
import com.afollestad.materialdialogs.input.input
import com.arellomobile.mvp.presenter.InjectPresenter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.gpetuhov.android.hive.R
import com.gpetuhov.android.hive.databinding.FragmentProfileBinding
import com.gpetuhov.android.hive.ui.viewmodel.CurrentUserViewModel
import com.gpetuhov.android.hive.domain.model.User
import com.gpetuhov.android.hive.presentation.presenter.ProfileFragmentPresenter
import com.gpetuhov.android.hive.presentation.view.ProfileFragmentView
import com.gpetuhov.android.hive.util.Constants
import com.gpetuhov.android.hive.util.hideToolbar
import com.gpetuhov.android.hive.util.moxy.MvpAppCompatFragment
import com.gpetuhov.android.hive.util.setActivitySoftInputPan
import com.gpetuhov.android.hive.util.showBottomNavigationView
import com.pawegio.kandroid.toast
import kotlinx.android.synthetic.main.fragment_profile.*

class ProfileFragment : MvpAppCompatFragment(), ProfileFragmentView {

    companion object {
        private const val RC_PHOTO_PICKER = 1001
    }

    @InjectPresenter lateinit var presenter: ProfileFragmentPresenter

    private lateinit var userPic: ImageView

    private var usernameDialog: MaterialDialog? = null
    private var serviceDialog: MaterialDialog? = null
    private var signOutDialog: MaterialDialog? = null
    private var deleteUserDialog: MaterialDialog? = null
    private var binding: FragmentProfileBinding? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Adjust_pan is needed to prevent activity from being pushed up by the keyboard
        setActivitySoftInputPan()

        hideToolbar()
        showBottomNavigationView()

        initDialogs()

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false)
        binding?.presenter = presenter

        val viewModel = ViewModelProviders.of(this).get(CurrentUserViewModel::class.java)

        // Every time current user data changes, update binding object with new data
        viewModel.currentUser.observe(this, Observer<User> { user ->
            binding?.user = user
            updateUserPic(user.userPicUrl)
        })

        return binding?.root
    }

    override fun onDestroyView() {
        super.onDestroyView()

        // This is needed to prevent memory leaks.
        // Here we intentially dismiss dialogs directly, not via the presenter,
        // so that Moxy command queue doesn't change.
        dismissDialogs()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_PHOTO_PICKER && resultCode == Activity.RESULT_OK) {
            val selectedImageUri = data?.data
            if (selectedImageUri != null) presenter.changeUserPic(selectedImageUri)
        }
    }

    // === ProfileFragmentView ===

    override fun showSignOutDialog() = signOutDialog?.show() ?: Unit

    override fun dismissSignOutDialog() = signOutDialog?.dismiss() ?: Unit

    override fun enableSignOutButton() = signOutButtonEnabled(true)

    override fun disableSignOutButton() = signOutButtonEnabled(false)

    override fun showDeleteUserDialog() = deleteUserDialog?.show() ?: Unit

    override fun dismissDeleteUserDialog() = deleteUserDialog?.dismiss() ?: Unit

    override fun enableDeleteUserButton() = deleteUserButtonEnabled(true)

    override fun disableDeleteUserButton() = deleteUserButtonEnabled(false)

    override fun showUsernameDialog() {
        // Prefill dialog with text provided by presenter
        val editText = usernameDialog?.getInputField()
        editText?.setText(presenter.getUsernamePrefill())
        editText?.setSelection(editText.text.length)
        usernameDialog?.show()
    }

    override fun dismissUsernameDialog() = usernameDialog?.dismiss() ?: Unit

    override fun showServiceDialog() {
        // Prefill dialog with text provided by presenter
        val editText = serviceDialog?.getInputField()
        editText?.setText(presenter.getServicePrefill())
        editText?.setSelection(editText.text.length)
        serviceDialog?.show()
    }

    override fun dismissServiceDialog() = serviceDialog?.dismiss() ?: Unit

    override fun chooseUserPic() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = Constants.FileTypes.IMAGE
        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true)
        startActivityForResult(Intent.createChooser(intent, getString(R.string.complete_action_using)), RC_PHOTO_PICKER)
        // Result will be passed into onActivityResult()
    }

    override fun showToast(message: String) {
        toast(message)
    }

    // === Private methods ===

    private fun initDialogs() {
        initUsernameDialog()
        initServiceDialog()
        initSignOutDialog()
        initDeleteUserDialog()
    }

    private fun initUsernameDialog() {
        if (context != null) {
            usernameDialog = MaterialDialog(context!!)
                .title(R.string.username)
                .noAutoDismiss()
                .cancelable(false)
                .input(hintRes = R.string.enter_username, waitForPositiveButton = false) { dialog, text ->
                    presenter.updateTempUsername(text.toString())
                }
                .positiveButton { presenter.saveUsername() }
                .negativeButton { presenter.dismissUsernameDialog() }
        }
    }

    private fun initServiceDialog() {
        if (context != null) {
            serviceDialog = MaterialDialog(context!!)
                .title(R.string.service)
                .noAutoDismiss()
                .cancelable(false)
                .input(hintRes = R.string.enter_service, waitForPositiveButton = false) { dialog, text ->
                    presenter.updateTempService(text.toString())
                }
                .positiveButton { presenter.saveService() }
                .negativeButton { presenter.dismissServiceDialog() }
        }
    }

    private fun initSignOutDialog() {
        if (context != null) {
            signOutDialog = MaterialDialog(context!!)
                .title(R.string.sign_out)
                .message(R.string.prompt_sign_out)
                .noAutoDismiss()
                .cancelable(false)
                .positiveButton { presenter.signOut() }
                .negativeButton { presenter.signOutCancel() }
        }
    }

    private fun initDeleteUserDialog() {
        if (context != null) {
            deleteUserDialog = MaterialDialog(context!!)
                .title(R.string.delete_account)
                .message(R.string.prompt_delete_account)
                .noAutoDismiss()
                .cancelable(false)
                .positiveButton { presenter.deleteUser() }
                .negativeButton { presenter.deleteUserCancel() }
        }
    }

    private fun dismissDialogs() {
        dismissUsernameDialog()
        dismissServiceDialog()
        dismissSignOutDialog()
        dismissDeleteUserDialog()
    }

    private fun signOutButtonEnabled(isEnabled: Boolean) {
        signout_button.isEnabled = isEnabled
    }

    private fun deleteUserButtonEnabled(isEnabled: Boolean) {
        delete_user_button.isEnabled = isEnabled
    }

    private fun updateUserPic(userPicUrl: String) {
        userPic = binding?.root?.findViewById(R.id.user_pic) ?: ImageView(context)

        if (userPicUrl != "") {
            Glide.with(this).load(userPicUrl)
                .apply(RequestOptions.circleCropTransform())
                .into(userPic)

        } else {
            Glide.with(this).load(R.drawable.ic_account_circle)
                .apply(RequestOptions.circleCropTransform())
                .into(userPic)
        }
    }
}