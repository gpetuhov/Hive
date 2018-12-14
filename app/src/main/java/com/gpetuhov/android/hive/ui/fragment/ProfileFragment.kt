package com.gpetuhov.android.hive.ui.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.WhichButton
import com.afollestad.materialdialogs.actions.setActionButtonEnabled
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
import com.gpetuhov.android.hive.ui.fragment.base.BaseFragment
import com.gpetuhov.android.hive.util.*
import com.pawegio.kandroid.toast

class ProfileFragment : BaseFragment(), ProfileFragmentView {

    companion object {
        private const val RC_USERPIC_PICKER = 1001
        private const val RC_USER_PHOTO_PICKER = 1002
    }

    @InjectPresenter lateinit var presenter: ProfileFragmentPresenter

    private var controller: ProfileListController? = null

    private var usernameDialog: MaterialDialog? = null
    private var descriptionDialog: MaterialDialog? = null
    private var signOutDialog: MaterialDialog? = null
    private var deleteUserDialog: MaterialDialog? = null
    private var deletePhotoDialog: MaterialDialog? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Adjust_pan is needed to prevent activity from being pushed up by the keyboard
        setActivitySoftInputPan()

        hideToolbar()
        showBottomNavigationView()

        initDialogs()

        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        controller = ProfileListController(presenter)
        controller?.onRestoreInstanceState(savedInstanceState)

        val profileRecyclerView = view.findViewById<EpoxyRecyclerView>(R.id.profile_recycler_view)
        profileRecyclerView.adapter = controller?.adapter

        val viewModel = ViewModelProviders.of(this).get(CurrentUserViewModel::class.java)
        viewModel.currentUser.observe(this, Observer<User> { user ->
            controller?.changeUser(user)
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

        if (requestCode == RC_USERPIC_PICKER && resultCode == Activity.RESULT_OK) {
            val selectedImageUri = data?.data
            if (selectedImageUri != null) presenter.changeUserPic(selectedImageUri)
        }

        if (requestCode == RC_USER_PHOTO_PICKER && resultCode == Activity.RESULT_OK) {
            val selectedImageUri = data?.data
            if (selectedImageUri != null) presenter.addPhoto(selectedImageUri)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        controller?.onSaveInstanceState(outState)
    }

    // === ProfileFragmentView ===

    override fun showSignOutDialog() = signOutDialog?.show() ?: Unit

    override fun dismissSignOutDialog() = signOutDialog?.dismiss() ?: Unit

    override fun enableSignOutButton() = signOutButtonEnabled(true) ?: Unit

    override fun disableSignOutButton() = signOutButtonEnabled(false) ?: Unit

    override fun showDeleteUserDialog() = deleteUserDialog?.show() ?: Unit

    override fun dismissDeleteUserDialog() = deleteUserDialog?.dismiss() ?: Unit

    override fun enableDeleteUserButton() = deleteUserButtonEnabled(true) ?: Unit

    override fun disableDeleteUserButton() = deleteUserButtonEnabled(false) ?: Unit

    override fun showUsernameDialog() {
        // Prefill dialog with text provided by presenter
        val editText = usernameDialog?.getInputField()
        editText?.setText(presenter.getUsernamePrefill())
        editText?.setSelection(editText.text.length)
        usernameDialog?.show()
    }

    override fun dismissUsernameDialog() = usernameDialog?.dismiss() ?: Unit

    override fun showDescriptionDialog() {
        // Prefill dialog with text provided by presenter
        val editText = descriptionDialog?.getInputField()
        editText?.setText(presenter.getDescriptionPrefill())
        editText?.setSelection(editText.text.length)
        descriptionDialog?.show()
    }

    override fun dismissDescriptionDialog() = descriptionDialog?.dismiss() ?: Unit

    override fun chooseUserPic() = startPhotoPicker(RC_USERPIC_PICKER)

    override fun updateOffer(offerUid: String) {
        val action = ProfileFragmentDirections.actionNavigationProfileToUpdateOfferFragment(offerUid)
        findNavController().navigate(action)
    }

    override fun choosePhoto() = startPhotoPicker(RC_USER_PHOTO_PICKER)

    override fun showDeletePhotoDialog() = deletePhotoDialog?.show() ?: Unit

    override fun dismissDeletePhotoDialog() = deletePhotoDialog?.dismiss() ?: Unit

    override fun openPhotos(photoUrlList: MutableList<String>) {
        val photoBundle = Bundle()
        photoBundle.putStringArrayList(PhotoFragment.PHOTO_URL_LIST_KEY, ArrayList(photoUrlList))

        val action = ProfileFragmentDirections.actionNavigationProfileToPhotoFragment(photoBundle)
        findNavController().navigate(action)
    }

    override fun showToast(message: String) {
        toast(message)
    }

    // === Private methods ===

    private fun initDialogs() {
        initUsernameDialog()
        initDescriptionDialog()
        initSignOutDialog()
        initDeleteUserDialog()
        initDeletePhotoDialog()
    }

    private fun initUsernameDialog() {
        if (context != null) {
            usernameDialog = MaterialDialog(context!!)
                .title(R.string.username)
                .noAutoDismiss()
                .cancelable(false)
                .input(
                    maxLength = Constants.User.MAX_USERNAME_LENGTH,
                    hintRes = R.string.enter_username,
                    waitForPositiveButton = false
                ) { dialog, text ->
                    val inputText = text.toString()
                    var positiveButtonEnabled = inputText.length <= Constants.User.MAX_USERNAME_LENGTH
                    presenter.updateTempUsername(inputText)

                    if (inputText.contains(" ")) {
                        dialog.getInputField()?.error = "Must not contain spaces"
                        positiveButtonEnabled = false
                    }

                    dialog.setActionButtonEnabled(WhichButton.POSITIVE, positiveButtonEnabled)
                }
                .positiveButton { presenter.saveUsername() }
                .negativeButton { presenter.dismissUsernameDialog() }
        }
    }

    private fun initDescriptionDialog() {
        if (context != null) {
            descriptionDialog = MaterialDialog(context!!)
                .title(R.string.about_me)
                .noAutoDismiss()
                .cancelable(false)
                .input(
                    maxLength = Constants.User.MAX_DESCRIPTION_LENGTH,
                    inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_MULTI_LINE,
                    hintRes = R.string.enter_description,
                    waitForPositiveButton = false
                ) { dialog, text ->
                    val inputText = text.toString()
                    val positiveButtonEnabled = inputText.length <= Constants.User.MAX_DESCRIPTION_LENGTH
                    presenter.updateTempDescription(inputText)
                    dialog.setActionButtonEnabled(WhichButton.POSITIVE, positiveButtonEnabled)
                }
                .positiveButton { presenter.saveDescription() }
                .negativeButton { presenter.dismissDescriptionDialog() }
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

    private fun initDeletePhotoDialog() {
        if (context != null) {
            deletePhotoDialog = MaterialDialog(context!!)
                .title(R.string.delete_photo)
                .message(R.string.prompt_delete_photo)
                .noAutoDismiss()
                .cancelable(false)
                .positiveButton { presenter.deletePhoto() }
                .negativeButton { presenter.deletePhotoCancel() }
        }
    }

    private fun dismissDialogs() {
        dismissUsernameDialog()
        dismissDescriptionDialog()
        dismissSignOutDialog()
        dismissDeleteUserDialog()
        dismissDeletePhotoDialog()
    }

    private fun signOutButtonEnabled(isEnabled: Boolean) = controller?.signOutEnabled(isEnabled)

    private fun deleteUserButtonEnabled(isEnabled: Boolean) = controller?.deleteAccountEnabled(isEnabled)
}