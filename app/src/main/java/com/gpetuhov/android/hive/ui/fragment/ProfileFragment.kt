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
    private var languageDialog: MaterialDialog? = null
    private var educationDialog: MaterialDialog? = null
    private var workDialog: MaterialDialog? = null
    private var interestsDialog: MaterialDialog? = null
    private var statusDialog: MaterialDialog? = null
    private var deleteUserPicDialog: MaterialDialog? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Adjust_pan is needed to prevent activity from being pushed up by the keyboard
        setActivitySoftInputPan()

        hideMainHeader()
        showBottomNavigationView()

        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        controller = ProfileListController(presenter)
        controller?.onRestoreInstanceState(savedInstanceState)

        val profileRecyclerView = view.findViewById<EpoxyRecyclerView>(R.id.profile_recycler_view)
        profileRecyclerView.adapter = controller?.adapter

        val viewModel = ViewModelProviders.of(this).get(CurrentUserViewModel::class.java)
        viewModel.currentUser.observe(this, Observer<User> { user ->
            controller?.changeUser(user)

            // If there are new awards (for which congratulation have not been shown yet),
            // open CongratulationFragment.
            val newAwardsList = user.newAwardsList
            if (newAwardsList.isNotEmpty()) presenter.openCongratulation(newAwardsList)
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
        controller?.onSaveInstanceState(outState)
        super.onSaveInstanceState(outState)
    }

    // === ProfileFragmentView ===

    override fun showSignOutDialog() {
        initSignOutDialog()
        signOutDialog?.show()
    }

    override fun dismissSignOutDialog() = signOutDialog?.dismiss() ?: Unit

    override fun enableSignOutButton() = signOutButtonEnabled(true) ?: Unit

    override fun disableSignOutButton() = signOutButtonEnabled(false) ?: Unit

    override fun showDeleteUserDialog() {
        initDeleteUserDialog()
        deleteUserDialog?.show()
    }

    override fun dismissDeleteUserDialog() = deleteUserDialog?.dismiss() ?: Unit

    override fun enableDeleteUserButton() = deleteUserButtonEnabled(true) ?: Unit

    override fun disableDeleteUserButton() = deleteUserButtonEnabled(false) ?: Unit

    override fun showUsernameDialog() {
        initUsernameDialog()
        showDialog(usernameDialog, presenter.getUsernamePrefill())
    }

    override fun dismissUsernameDialog() = usernameDialog?.dismiss() ?: Unit

    override fun showDescriptionDialog() {
        initDescriptionDialog()
        showDialog(descriptionDialog, presenter.getDescriptionPrefill())
    }

    override fun dismissDescriptionDialog() = descriptionDialog?.dismiss() ?: Unit

    override fun chooseUserPic() = startPhotoPicker(RC_USERPIC_PICKER)

    override fun updateOffer(offerUid: String) {
        val action = ProfileFragmentDirections.actionNavigationProfileToUpdateOfferFragment(offerUid)
        findNavController().navigate(action)
    }

    override fun choosePhoto() = startPhotoPicker(RC_USER_PHOTO_PICKER)

    override fun showDeletePhotoDialog() {
        initDeletePhotoDialog()
        deletePhotoDialog?.show()
    }

    override fun dismissDeletePhotoDialog() = deletePhotoDialog?.dismiss() ?: Unit

    override fun openPhotos(photoUrlList: MutableList<String>) {
        val photoBundle = Bundle()
        photoBundle.putStringArrayList(PhotoFragment.PHOTO_URL_LIST_KEY, ArrayList(photoUrlList))

        val action = ProfileFragmentDirections.actionNavigationProfileToPhotoFragment(photoBundle)
        findNavController().navigate(action)
    }

    override fun showPhoneDialog() {
        initPhoneDialog()
        showDialog(phoneDialog, presenter.getPhonePrefill())
    }

    override fun dismissPhoneDialog() = phoneDialog?.dismiss() ?: Unit

    override fun showEmailDialog() {
        initEmailDialog()
        showDialog(emailDialog, presenter.getEmailPrefill())
    }

    override fun dismissEmailDialog() = emailDialog?.dismiss() ?: Unit

    override fun showSkypeDialog() {
        initSkypeDialog()
        showDialog(skypeDialog, presenter.getSkypePrefill())
    }

    override fun dismissSkypeDialog() = skypeDialog?.dismiss() ?: Unit

    override fun showFacebookDialog() {
        initFacebookDialog()
        showDialog(facebookDialog, presenter.getFacebookPrefill())
    }

    override fun dismissFacebookDialog() = facebookDialog?.dismiss() ?: Unit

    override fun showTwitterDialog() {
        initTwitterDialog()
        showDialog(twitterDialog, presenter.getTwitterPrefill())
    }

    override fun dismissTwitterDialog() = twitterDialog?.dismiss() ?: Unit

    override fun showInstagramDialog() {
        initInstagramDialog()
        showDialog(instagramDialog, presenter.getInstagramPrefill())
    }

    override fun dismissInstagramDialog() = instagramDialog?.dismiss() ?: Unit

    override fun showYouTubeDialog() {
        initYouTubeDialog()
        showDialog(youTubeDialog, presenter.getYouTubePrefill())
    }

    override fun dismissYouTubeDialog() = youTubeDialog?.dismiss() ?: Unit

    override fun showWebsiteDialog() {
        initWebsiteDialog()
        showDialog(websiteDialog, presenter.getWebsitePrefill())
    }

    override fun dismissWebsiteDialog() = websiteDialog?.dismiss() ?: Unit

    override fun showResidenceDialog() {
        initResidenceDialog()
        showDialog(residenceDialog, presenter.getResidencePrefill())
    }

    override fun dismissResidenceDialog() = residenceDialog?.dismiss() ?: Unit

    override fun showLanguageDialog() {
        initLanguageDialog()
        showDialog(languageDialog, presenter.getLanguagePrefill())
    }

    override fun dismissLanguageDialog() = languageDialog?.dismiss() ?: Unit

    override fun showEducationDialog() {
        initEducationDialog()
        showDialog(educationDialog, presenter.getEducationPrefill())
    }

    override fun dismissEducationDialog() = educationDialog?.dismiss() ?: Unit

    override fun showWorkDialog() {
        initWorkDialog()
        showDialog(workDialog, presenter.getWorkPrefill())
    }

    override fun dismissWorkDialog() = workDialog?.dismiss() ?: Unit

    override fun showInterestsDialog() {
        initInterestsDialog()
        showDialog(interestsDialog, presenter.getInterestsPrefill())
    }

    override fun dismissInterestsDialog() = interestsDialog?.dismiss() ?: Unit

    override fun showStatusDialog() {
        initStatusDialog()
        showDialog(statusDialog, presenter.getStatusPrefill())
    }

    override fun dismissStatusDialog() = statusDialog?.dismiss() ?: Unit

    override fun openPrivacyPolicy() {
        val action = ProfileFragmentDirections.actionNavigationProfileToPrivacyPolicyFragment()
        findNavController().navigate(action)
    }

    override fun openAllReviews() {
        val action = ProfileFragmentDirections.actionNavigationProfileToReviewsAllFragment(true)
        findNavController().navigate(action)
    }

    override fun openAward(awardType: Int) {
        val action = ProfileFragmentDirections.actionNavigationProfileToAwardFragment(awardType)
        findNavController().navigate(action)
    }

    override fun openCongratulation(newAwardsList: MutableList<Int>) {
        val navController = findNavController()

        // This check is required to prevent navigation destination is unknown crash
        // when user returns back from CongratulationFragment and there is another new award.
        if (navController.currentDestination?.id == R.id.navigation_profile) {
            val newAwardsBundle = Bundle()
            newAwardsBundle.putIntegerArrayList(CongratulationFragment.NEW_AWARD_LIST_KEY, ArrayList(newAwardsList))

            val action = ProfileFragmentDirections.actionNavigationProfileToCongratulationFragment(newAwardsBundle)
            navController.navigate(action)
        }
    }

    override fun openUserActivity(userActivityType: Int) {
        val action = ProfileFragmentDirections.actionNavigationProfileToUserActivityFragment(userActivityType)
        findNavController().navigate(action)
    }

    override fun openUserPic(userPicUrl: String) {
        val action = ProfileFragmentDirections.actionNavigationProfileToUserPicFragment(userPicUrl)
        findNavController().navigate(action)
    }

    override fun showDeleteUserPicDialog() {
        initDeleteUserPicDialog()
        deleteUserPicDialog?.show()
    }

    override fun dismissDeleteUserPicDialog() = deleteUserPicDialog?.dismiss() ?: Unit

    override fun showToast(message: String) {
        toast(message)
    }

    // === Private methods ===

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

    private fun initLanguageDialog() {
        languageDialog = getInputDialog(
            titleId = R.string.language,
            hintId = R.string.enter_language,
            errorMessageId = R.string.username_not_valid,
            onInputChange = { inputText -> presenter.updateTempLanguage(inputText) },
            isInputValid = { true },
            onPositive = { presenter.saveLanguage() },
            onNegative = { presenter.dismissLanguageDialog() }
        )
    }

    private fun initEducationDialog() {
        educationDialog = getInputDialog(
            titleId = R.string.education,
            hintId = R.string.enter_education,
            errorMessageId = R.string.username_not_valid,
            onInputChange = { inputText -> presenter.updateTempEducation(inputText) },
            isInputValid = { true },
            onPositive = { presenter.saveEducation() },
            onNegative = { presenter.dismissEducationDialog() }
        )
    }

    private fun initWorkDialog() {
        workDialog = getInputDialog(
            titleId = R.string.work,
            hintId = R.string.enter_work,
            errorMessageId = R.string.username_not_valid,
            onInputChange = { inputText -> presenter.updateTempWork(inputText) },
            isInputValid = { true },
            onPositive = { presenter.saveWork() },
            onNegative = { presenter.dismissWorkDialog() }
        )
    }

    private fun initInterestsDialog() {
        interestsDialog = getInputDialog(
            titleId = R.string.interests,
            hintId = R.string.enter_interests,
            errorMessageId = R.string.username_not_valid,
            onInputChange = { inputText -> presenter.updateTempInterests(inputText) },
            isInputValid = { true },
            onPositive = { presenter.saveInterests() },
            onNegative = { presenter.dismissInterestsDialog() }
        )
    }

    private fun initStatusDialog() {
        statusDialog = getInputDialog(
            titleId = R.string.status,
            hintId = R.string.enter_status,
            errorMessageId = R.string.username_not_valid,
            maxLength = Constants.User.MAX_STATUS_LENGTH,
            onInputChange = { inputText -> presenter.updateTempStatus(inputText) },
            isInputValid = { true },
            onPositive = { presenter.saveStatus() },
            onNegative = { presenter.dismissStatusDialog() }
        )
    }

    private fun initDeleteUserPicDialog() {
        if (context != null) {
            deleteUserPicDialog = MaterialDialog(context!!)
                .title(R.string.delete_user_pic)
                .message(R.string.prompt_delete_user_pic)
                .noAutoDismiss()
                .cancelable(false)
                .positiveButton { presenter.deleteUserPic() }
                .negativeButton { presenter.deleteUserPicCancel() }
        }
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
        dismissLanguageDialog()
        dismissEducationDialog()
        dismissWorkDialog()
        dismissInterestsDialog()
        dismissStatusDialog()
        dismissDeleteUserPicDialog()
    }

    private fun signOutButtonEnabled(isEnabled: Boolean) = controller?.signOutEnabled(isEnabled)

    private fun deleteUserButtonEnabled(isEnabled: Boolean) = controller?.deleteAccountEnabled(isEnabled)
}