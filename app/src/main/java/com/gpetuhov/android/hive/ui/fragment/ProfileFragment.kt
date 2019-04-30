package com.gpetuhov.android.hive.ui.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.util.Patterns
import android.util.Patterns.PHONE
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.util.PatternsCompat.EMAIL_ADDRESS
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
    private var phoneDialog: MaterialDialog? = null
    private var emailDialog: MaterialDialog? = null
    private var skypeDialog: MaterialDialog? = null
    private var facebookDialog: MaterialDialog? = null
    private var twitterDialog: MaterialDialog? = null
    private var instagramDialog: MaterialDialog? = null
    private var youTubeDialog: MaterialDialog? = null
    private var websiteDialog: MaterialDialog? = null
    private var residenceDialog: MaterialDialog? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Adjust_pan is needed to prevent activity from being pushed up by the keyboard
        setActivitySoftInputPan()

        hideMainHeader()
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

    override fun showPhoneDialog() {
        // Prefill dialog with text provided by presenter
        val editText = phoneDialog?.getInputField()
        editText?.setText(presenter.getPhonePrefill())
        editText?.setSelection(editText.text.length)
        phoneDialog?.show()
    }

    override fun dismissPhoneDialog() = phoneDialog?.dismiss() ?: Unit

    override fun showEmailDialog() {
        // Prefill dialog with text provided by presenter
        val editText = emailDialog?.getInputField()
        editText?.setText(presenter.getEmailPrefill())
        editText?.setSelection(editText.text.length)
        emailDialog?.show()
    }

    override fun dismissEmailDialog() = emailDialog?.dismiss() ?: Unit

    override fun showSkypeDialog() {
        // Prefill dialog with text provided by presenter
        val editText = skypeDialog?.getInputField()
        editText?.setText(presenter.getSkypePrefill())
        editText?.setSelection(editText.text.length)
        skypeDialog?.show()
    }

    override fun dismissSkypeDialog() = skypeDialog?.dismiss() ?: Unit

    override fun showFacebookDialog() {
        // Prefill dialog with text provided by presenter
        val editText = facebookDialog?.getInputField()
        editText?.setText(presenter.getFacebookPrefill())
        editText?.setSelection(editText.text.length)
        facebookDialog?.show()
    }

    override fun dismissFacebookDialog() = facebookDialog?.dismiss() ?: Unit

    override fun showTwitterDialog() {
        // Prefill dialog with text provided by presenter
        val editText = twitterDialog?.getInputField()
        editText?.setText(presenter.getTwitterPrefill())
        editText?.setSelection(editText.text.length)
        twitterDialog?.show()
    }

    override fun dismissTwitterDialog() = twitterDialog?.dismiss() ?: Unit

    override fun showInstagramDialog() {
        // Prefill dialog with text provided by presenter
        val editText = instagramDialog?.getInputField()
        editText?.setText(presenter.getInstagramPrefill())
        editText?.setSelection(editText.text.length)
        instagramDialog?.show()
    }

    override fun dismissInstagramDialog() = instagramDialog?.dismiss() ?: Unit

    override fun showYouTubeDialog() {
        // Prefill dialog with text provided by presenter
        val editText = youTubeDialog?.getInputField()
        editText?.setText(presenter.getYouTubePrefill())
        editText?.setSelection(editText.text.length)
        youTubeDialog?.show()
    }

    override fun dismissYouTubeDialog() = youTubeDialog?.dismiss() ?: Unit

    override fun showWebsiteDialog() {
        // Prefill dialog with text provided by presenter
        val editText = websiteDialog?.getInputField()
        editText?.setText(presenter.getWebsitePrefill())
        editText?.setSelection(editText.text.length)
        websiteDialog?.show()
    }

    override fun dismissWebsiteDialog() = websiteDialog?.dismiss() ?: Unit

    override fun showResidenceDialog() {
        // Prefill dialog with text provided by presenter
        val editText = residenceDialog?.getInputField()
        editText?.setText(presenter.getResidencePrefill())
        editText?.setSelection(editText.text.length)
        residenceDialog?.show()
    }

    override fun dismissResidenceDialog() = residenceDialog?.dismiss() ?: Unit

    override fun showLanguageDialog() {
        // TODO
    }

    override fun dismissLanguageDialog() {
        // TODO
    }

    override fun openPrivacyPolicy() {
        val action = ProfileFragmentDirections.actionNavigationProfileToPrivacyPolicyFragment()
        findNavController().navigate(action)
    }

    override fun openAllReviews() {
        val action = ProfileFragmentDirections.actionNavigationProfileToReviewsAllFragment(true)
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
        initPhoneDialog()
        initEmailDialog()
        initSkypeDialog()
        initFacebookDialog()
        initTwitterDialog()
        initInstagramDialog()
        initYouTubeDialog()
        initWebsiteDialog()
        initResidenceDialog()
    }

    private fun getInputDialog(
        titleId: Int,
        hintId: Int,
        errorMessageId: Int = R.string.username_not_valid,
        maxLength: Int? = null,
        inputType: Int = InputType.TYPE_CLASS_TEXT,
        onInputChange: (String) -> Unit,
        isInputValid: (String) -> Boolean,
        onPositive: () -> Unit,
        onNegative: () -> Unit
    ): MaterialDialog? {

        if (context != null) {
            val errorMessage = context?.getString(errorMessageId)

            return MaterialDialog(context!!)
                .title(titleId)
                .noAutoDismiss()
                .cancelable(false)
                .input(
                    maxLength = maxLength,
                    inputType = inputType,
                    hintRes = hintId,
                    waitForPositiveButton = false
                ) { dialog, text ->
                    val inputText = text.toString()
                    onInputChange(inputText)

                    val inputValid = isInputValid(inputText)
                    dialog.getInputField()?.error = if (!inputValid) errorMessage else null

                    val lengthValid = if (maxLength != null) inputText.length <= maxLength else true

                    // Enable positive button if input text and length is valid
                    // (for invalid length we do not show any error message).
                    dialog.setActionButtonEnabled(WhichButton.POSITIVE, inputValid && lengthValid)
                }
                .positiveButton { onPositive() }
                .negativeButton { onNegative() }

        } else {
            return null
        }
    }

    private fun initUsernameDialog() {
        usernameDialog = getInputDialog(
            titleId = R.string.username,
            hintId = R.string.enter_username,
            errorMessageId = R.string.username_not_valid,
            maxLength = Constants.User.MAX_USERNAME_LENGTH,
            onInputChange = { inputText -> presenter.updateTempUsername(inputText) },
            isInputValid = { inputText -> isHiveUsernameValid(inputText) },
            onPositive = { presenter.saveUsername() },
            onNegative = { presenter.dismissUsernameDialog() }
        )
    }

    private fun isHiveUsernameValid(username: String) = username == "" || !username.contains(" ")

    private fun initDescriptionDialog() {
        descriptionDialog = getInputDialog(
            titleId = R.string.about_me,
            hintId = R.string.enter_description,
            maxLength = Constants.User.MAX_DESCRIPTION_LENGTH,
            inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_MULTI_LINE,
            onInputChange = { inputText -> presenter.updateTempDescription(inputText) },
            isInputValid = { true },
            onPositive = { presenter.saveDescription() },
            onNegative = { presenter.dismissDescriptionDialog() }
        )
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

    private fun initPhoneDialog() {
        phoneDialog = getInputDialog(
            titleId = R.string.phone_number,
            hintId = R.string.enter_phone,
            errorMessageId = R.string.phone_not_valid,
            inputType = InputType.TYPE_CLASS_PHONE,
            onInputChange = { inputText -> presenter.updateTempPhone(inputText) },
            isInputValid = { inputText -> isPhoneValid(inputText) },
            onPositive = { presenter.savePhone() },
            onNegative = { presenter.dismissPhoneDialog() }
        )
    }

    private fun isPhoneValid(phone: String) = phone == "" || PHONE.matcher(phone).matches()

    private fun initEmailDialog() {
        emailDialog = getInputDialog(
            titleId = R.string.email,
            hintId = R.string.enter_email,
            errorMessageId = R.string.email_not_valid,
            inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS,
            onInputChange = { inputText -> presenter.updateTempEmail(inputText) },
            isInputValid = { inputText -> isEmailValid(inputText) },
            onPositive = { presenter.saveEmail() },
            onNegative = { presenter.dismissEmailDialog() }
        )
    }

    private fun isEmailValid(email: String) = email == "" || EMAIL_ADDRESS.matcher(email).matches()

    private fun isSocialUsernameValid(username: String) = username == "" || !username.contains(" ")

    private fun initSkypeDialog() {
        skypeDialog = getInputDialog(
            titleId = R.string.skype,
            hintId = R.string.enter_skype,
            errorMessageId = R.string.username_not_valid,
            onInputChange = { inputText -> presenter.updateTempSkype(inputText) },
            isInputValid = { inputText -> isSocialUsernameValid(inputText) },
            onPositive = { presenter.saveSkype() },
            onNegative = { presenter.dismissSkypeDialog() }
        )
    }

    private fun initFacebookDialog() {
        facebookDialog = getInputDialog(
            titleId = R.string.facebook,
            hintId = R.string.enter_facebook,
            errorMessageId = R.string.username_not_valid,
            onInputChange = { inputText -> presenter.updateTempFacebook(inputText) },
            isInputValid = { inputText -> isSocialUsernameValid(inputText) },
            onPositive = { presenter.saveFacebook() },
            onNegative = { presenter.dismissFacebookDialog() }
        )
    }

    private fun initTwitterDialog() {
        twitterDialog = getInputDialog(
            titleId = R.string.twitter,
            hintId = R.string.enter_twitter,
            errorMessageId = R.string.username_not_valid,
            onInputChange = { inputText -> presenter.updateTempTwitter(inputText) },
            isInputValid = { inputText -> isSocialUsernameValid(inputText) },
            onPositive = { presenter.saveTwitter() },
            onNegative = { presenter.dismissTwitterDialog() }
        )
    }

    private fun initInstagramDialog() {
        instagramDialog = getInputDialog(
            titleId = R.string.instagram,
            hintId = R.string.enter_instagram,
            errorMessageId = R.string.username_not_valid,
            onInputChange = { inputText -> presenter.updateTempInstagram(inputText) },
            isInputValid = { inputText -> isSocialUsernameValid(inputText) },
            onPositive = { presenter.saveInstagram() },
            onNegative = { presenter.dismissInstagramDialog() }
        )
    }

    private fun initYouTubeDialog() {
        youTubeDialog = getInputDialog(
            titleId = R.string.youtube,
            hintId = R.string.enter_youtube,
            errorMessageId = R.string.username_not_valid,
            onInputChange = { inputText -> presenter.updateTempYouTube(inputText) },
            isInputValid = { inputText -> isSocialUsernameValid(inputText) },
            onPositive = { presenter.saveYouTube() },
            onNegative = { presenter.dismissYouTubeDialog() }
        )
    }

    private fun initWebsiteDialog() {
        websiteDialog = getInputDialog(
            titleId = R.string.website,
            hintId = R.string.enter_website,
            errorMessageId = R.string.website_not_valid,
            onInputChange = { inputText -> presenter.updateTempWebsite(inputText) },
            isInputValid = { inputText -> isWebsiteValid(inputText) },
            onPositive = { presenter.saveWebsite() },
            onNegative = { presenter.dismissWebsiteDialog() }
        )
    }

    private fun isWebsiteValid(website: String): Boolean {
        val startsWithHttp = website.startsWith("http://") || website.startsWith("https://")
        return website == "" || (startsWithHttp && Patterns.WEB_URL.matcher(website).matches())
    }

    private fun initResidenceDialog() {
        residenceDialog = getInputDialog(
            titleId = R.string.residence,
            hintId = R.string.enter_residence,
            errorMessageId = R.string.username_not_valid,
            onInputChange = { inputText -> presenter.updateTempResidence(inputText) },
            isInputValid = { true },
            onPositive = { presenter.saveResidence() },
            onNegative = { presenter.dismissResidenceDialog() }
        )
    }

    private fun dismissDialogs() {
        dismissUsernameDialog()
        dismissDescriptionDialog()
        dismissSignOutDialog()
        dismissDeleteUserDialog()
        dismissDeletePhotoDialog()
        dismissPhoneDialog()
        dismissEmailDialog()
        dismissSkypeDialog()
        dismissFacebookDialog()
        dismissTwitterDialog()
        dismissInstagramDialog()
        dismissYouTubeDialog()
        dismissWebsiteDialog()
        dismissResidenceDialog()
    }

    private fun signOutButtonEnabled(isEnabled: Boolean) = controller?.signOutEnabled(isEnabled)

    private fun deleteUserButtonEnabled(isEnabled: Boolean) = controller?.deleteAccountEnabled(isEnabled)
}