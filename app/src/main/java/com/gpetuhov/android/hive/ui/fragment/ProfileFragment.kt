package com.gpetuhov.android.hive.ui.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.input.getInputField
import com.afollestad.materialdialogs.input.input
import com.airbnb.epoxy.EpoxyRecyclerView
import com.arellomobile.mvp.presenter.InjectPresenter
import com.gpetuhov.android.hive.R
import com.gpetuhov.android.hive.ui.viewmodel.CurrentUserViewModel
import com.gpetuhov.android.hive.domain.model.User
import com.gpetuhov.android.hive.presentation.presenter.ProfileFragmentPresenter
import com.gpetuhov.android.hive.presentation.view.ProfileFragmentView
import com.gpetuhov.android.hive.ui.epoxy.profile.controller.ProfileListController
import com.gpetuhov.android.hive.util.*
import com.gpetuhov.android.hive.util.moxy.MvpAppCompatFragment
import com.pawegio.kandroid.toast

class ProfileFragment : MvpAppCompatFragment(), ProfileFragmentView {

    companion object {
        private const val RC_PHOTO_PICKER = 1001
    }

    @InjectPresenter lateinit var presenter: ProfileFragmentPresenter

    private lateinit var controller: ProfileListController

    private var usernameDialog: MaterialDialog? = null
    private var offerDialog: MaterialDialog? = null
    private var signOutDialog: MaterialDialog? = null
    private var deleteUserDialog: MaterialDialog? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Adjust_pan is needed to prevent activity from being pushed up by the keyboard
        setActivitySoftInputPan()

        hideToolbar()
        showBottomNavigationView()

        initDialogs()

        val view = inflater.inflate(R.layout.fragment_profile_2, container, false)

        controller = ProfileListController(presenter)

        val profileRecyclerView = view.findViewById<EpoxyRecyclerView>(R.id.profile_recycler_view)
        profileRecyclerView.adapter = controller.adapter

        val viewModel = ViewModelProviders.of(this).get(CurrentUserViewModel::class.java)
        viewModel.currentUser.observe(this, Observer<User> { user ->
            controller.changeUser(user)
        })

        return view
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

    override fun showOfferDialog() {
        // Prefill dialog with text provided by presenter
        val editText = offerDialog?.getInputField()
        editText?.setText(presenter.getOfferPrefill())
        editText?.setSelection(editText.text.length)
        offerDialog?.show()
    }

    override fun dismissOfferDialog() = offerDialog?.dismiss() ?: Unit

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
            offerDialog = MaterialDialog(context!!)
                .title(R.string.offer)
                .noAutoDismiss()
                .cancelable(false)
                .input(hintRes = R.string.enter_offer, waitForPositiveButton = false) { dialog, text ->
                    presenter.updateTempOffer(text.toString())
                }
                .positiveButton { presenter.saveOffer() }
                .negativeButton { presenter.dismissOfferDialog() }
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
        dismissOfferDialog()
        dismissSignOutDialog()
        dismissDeleteUserDialog()
    }

    // TODO: fix this
    private fun signOutButtonEnabled(isEnabled: Boolean) {
//        signout_button.isEnabled = isEnabled
    }

    private fun deleteUserButtonEnabled(isEnabled: Boolean) {
//        delete_user_button.isEnabled = isEnabled
    }
}