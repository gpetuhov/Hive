package com.gpetuhov.android.hive.presentation.presenter

import android.net.Uri
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.gpetuhov.android.hive.application.HiveApp
import com.gpetuhov.android.hive.domain.interactor.*
import com.gpetuhov.android.hive.domain.interactor.base.SaveUserPropertyInteractor
import com.gpetuhov.android.hive.domain.repository.Repo
import com.gpetuhov.android.hive.domain.util.ResultMessages
import com.gpetuhov.android.hive.presentation.view.ProfileFragmentView
import javax.inject.Inject

// This is the presenter for ProfileFragment
// ALL (!!!) user interactions must be performed through this presenter ONLY!
// Presenters only control views (in our case ProfileFragment).
// All business logic is implemented in the corresponding interactors at domain layer.
// Presenters invoke this logic by calling interactors' execute() methods.

@InjectViewState
class ProfileFragmentPresenter :
    MvpPresenter<ProfileFragmentView>(),
    SignOutInteractor.Callback,
    DeleteUserInteractor.Callback,
    SaveUserPropertyInteractor.Callback {

    @Inject lateinit var repo: Repo
    @Inject lateinit var resultMessages: ResultMessages

    private val signOutInteractor = SignOutInteractor(this)
    private val deleteUserInteractor = DeleteUserInteractor(this)
    private val saveUsernameInteractor = SaveUsernameInteractor(this)
    private val saveDescriptionInteractor = SaveDescriptionInteractor(this)
    private val deleteUserPhotoInteractor = DeleteUserPhotoInteractor(this)
    private val savePhoneInteractor = SavePhoneInteractor(this)
    private val saveEmailInteractor = SaveEmailInteractor(this)
    private val saveSkypeInteractor = SaveSkypeInteractor(this)
    private val saveFacebookInteractor = SaveFacebookInteractor(this)
    private val saveTwitterInteractor = SaveTwitterInteractor(this)
    private val saveInstagramInteractor = SaveInstagramInteractor(this)
    private val saveYouTubeInteractor = SaveYouTubeInteractor(this)
    private val saveWebsiteInteractor = SaveWebsiteInteractor(this)
    private val saveResidenceInteractor = SaveResidenceInteractor(this)
    private val saveLanguageInteractor = SaveLanguageInteractor(this)
    private val saveEducationInteractor = SaveEducationInteractor(this)
    private val saveWorkInteractor = SaveWorkInteractor(this)
    private val saveInterestsInteractor = SaveInterestsInteractor(this)

    // Keeps current text entered in username dialog
    private var tempUsername = ""

    // Keeps current text entered in description dialog
    private var tempDescription = ""

    // Uid of the photo to be deleted
    private var deletePhotoUid = ""

    // Keeps current text entered in phone dialog
    private var tempPhone = ""

    // Keeps current text entered in email dialog
    private var tempEmail = ""

    // Keeps current text entered in Skype dialog
    private var tempSkype = ""

    // Keeps current text entered in Facebook dialog
    private var tempFacebook = ""

    // Keeps current text entered in Twitter dialog
    private var tempTwitter = ""

    // Keeps current text entered in Instagram dialog
    private var tempInstagram = ""

    // Keeps current text entered in YouTube dialog
    private var tempYouTube = ""

    // Keeps current text entered in website dialog
    private var tempWebsite = ""

    // Keeps current text entered in residence dialog
    private var tempResidence = ""

    // Keeps current text entered in language dialog
    private var tempLanguage = ""

    // Keeps current text entered in education dialog
    private var tempEducation = ""

    // Keeps current text entered in work dialog
    private var tempWork = ""

    // Keeps current text entered in interests dialog
    private var tempInterests = ""

    init {
        HiveApp.appComponent.inject(this)
    }

    // === SignOutInteractor.Callback ===

    override fun onSignOutSuccess() = viewState.enableSignOutButton()

    override fun onSignOutError(errorMessage: String) {
        showToast(errorMessage)
        viewState.enableSignOutButton()
    }

    // === DeleteUserInteractor.Callback ===

    override fun onDeleteUserComplete(message: String) {
        showToast(message)
        viewState.enableDeleteUserButton()
    }

    // === SaveUserPropertyInteractor.Callback ===

    override fun onSaveError(errorMessage: String) = showToast(errorMessage)

    // === Public methods ===
    // --- Sign out ---

    // We must call ProfileFragmentView's methods not directly, but through ViewState only.
    // This way Moxy will remember current state of the view and will restore it,
    // when the view is recreated.
    fun showSignOutDialog() {
        viewState.disableSignOutButton()
        viewState.showSignOutDialog()
    }

    fun signOut() {
        viewState.dismissSignOutDialog()
        signOutInteractor.execute()
    }

    fun signOutCancel() {
        viewState.dismissSignOutDialog()
        viewState.enableSignOutButton()
    }

    // --- Delete user ---

    fun showDeleteUserDialog() {
        viewState.disableDeleteUserButton()
        viewState.showDeleteUserDialog()
    }

    fun deleteUser() {
        viewState.dismissDeleteUserDialog()
        deleteUserInteractor.execute()
    }

    fun deleteUserCancel() {
        viewState.dismissDeleteUserDialog()
        viewState.enableDeleteUserButton()
    }

    // --- Change username ---

    fun showUsernameDialog() = viewState.showUsernameDialog()

    // Prefill username dialog with currently entered text or current username
    fun getUsernamePrefill() = if (tempUsername != "") tempUsername else repo.currentUserUsername()

    fun updateTempUsername(newTempUsername: String) {
        tempUsername = newTempUsername
    }

    fun saveUsername() {
        saveUsernameInteractor.save(tempUsername)
        dismissUsernameDialog()
    }

    fun dismissUsernameDialog() {
        tempUsername = ""
        viewState.dismissUsernameDialog()
    }

    // --- Change description ---

    fun showDescriptionDialog() = viewState.showDescriptionDialog()

    // Prefill description dialog with currently entered text or current description
    fun getDescriptionPrefill() = if (tempDescription != "") tempDescription else repo.currentUserDescription()

    fun updateTempDescription(newTempDescription: String) {
        tempDescription = newTempDescription
    }

    fun saveDescription() {
        saveDescriptionInteractor.save(tempDescription)
        dismissDescriptionDialog()
    }

    fun dismissDescriptionDialog() {
        tempDescription = ""
        viewState.dismissDescriptionDialog()
    }

    // --- Change user pic ---

    fun chooseUserPic() = viewState.chooseUserPic()

    fun changeUserPic(selectedImageUri: Uri) = repo.changeUserPic(selectedImageUri) { showToast(resultMessages.getChangeUserPicErrorMessage()) }

    // --- Update (add, edit) offer ---

    fun updateOffer(offerUid: String) = viewState.updateOffer(offerUid)

    // --- Add photo ---

    fun choosePhoto() = viewState.choosePhoto()

    fun addPhoto(selectedImageUri: Uri) = repo.addUserPhoto(selectedImageUri) { showToast(resultMessages.getAddPhotoErrorMessage()) }

    // --- Delete photo ---

    fun showDeletePhotoDialog(photoUid: String) {
        deletePhotoUid = photoUid
        viewState.showDeletePhotoDialog()
    }

    fun deletePhoto() {
        viewState.dismissDeletePhotoDialog()
        deleteUserPhotoInteractor.deletePhoto(deletePhotoUid)
        deletePhotoUid = ""
    }

    fun deletePhotoCancel() {
        deletePhotoUid = ""
        viewState.dismissDeletePhotoDialog()
    }

    // --- Change phone ---

    fun showPhoneDialog() = viewState.showPhoneDialog()

    // Prefill phone dialog with currently entered text or current phone
    fun getPhonePrefill() = if (tempPhone != "") tempPhone else repo.currentUserPhone()

    fun updateTempPhone(newTempPhone: String) {
        tempPhone = newTempPhone
    }

    fun savePhone() {
        savePhoneInteractor.save(tempPhone)
        dismissPhoneDialog()
    }

    fun dismissPhoneDialog() {
        tempPhone = ""
        viewState.dismissPhoneDialog()
    }

    // --- Change email ---

    fun showEmailDialog() = viewState.showEmailDialog()

    // Prefill email dialog with currently entered text or current value
    fun getEmailPrefill() = if (tempEmail != "") tempEmail else repo.currentUserVisibleEmail()

    fun updateTempEmail(newTempEmail: String) {
        tempEmail = newTempEmail
    }

    fun saveEmail() {
        saveEmailInteractor.save(tempEmail)
        dismissEmailDialog()
    }

    fun dismissEmailDialog() {
        tempEmail = ""
        viewState.dismissEmailDialog()
    }

    // === Use registration email ===

    fun saveRegistrationEmailAsVisibleEmail() = saveEmailInteractor.save(repo.currentUserEmail())

    // --- Change Skype ---

    fun showSkypeDialog() = viewState.showSkypeDialog()

    // Prefill Skype dialog with currently entered text or current value
    fun getSkypePrefill() = if (tempSkype != "") tempSkype else repo.currentUserSkype()

    fun updateTempSkype(newTempSkype: String) {
        tempSkype = newTempSkype
    }

    fun saveSkype() {
        saveSkypeInteractor.save(tempSkype)
        dismissSkypeDialog()
    }

    fun dismissSkypeDialog() {
        tempSkype = ""
        viewState.dismissSkypeDialog()
    }

    // --- Change Facebook ---

    fun showFacebookDialog() = viewState.showFacebookDialog()

    // Prefill Facebook dialog with currently entered text or current value
    fun getFacebookPrefill() = if (tempFacebook != "") tempFacebook else repo.currentUserFacebook()

    fun updateTempFacebook(newTempFacebook: String) {
        tempFacebook = newTempFacebook
    }

    fun saveFacebook() {
        saveFacebookInteractor.save(tempFacebook)
        dismissFacebookDialog()
    }

    fun dismissFacebookDialog() {
        tempFacebook = ""
        viewState.dismissFacebookDialog()
    }

    // --- Change Twitter ---

    fun showTwitterDialog() = viewState.showTwitterDialog()

    // Prefill Twitter dialog with currently entered text or current value
    fun getTwitterPrefill() = if (tempTwitter != "") tempTwitter else repo.currentUserTwitter()

    fun updateTempTwitter(newTempTwitter: String) {
        tempTwitter = newTempTwitter
    }

    fun saveTwitter() {
        saveTwitterInteractor.save(tempTwitter)
        dismissTwitterDialog()
    }

    fun dismissTwitterDialog() {
        tempTwitter = ""
        viewState.dismissTwitterDialog()
    }

    // --- Change Instagram ---

    fun showInstagramDialog() = viewState.showInstagramDialog()

    // Prefill Instagram dialog with currently entered text or current value
    fun getInstagramPrefill() = if (tempInstagram != "") tempInstagram else repo.currentUserInstagram()

    fun updateTempInstagram(newTempInstagram: String) {
        tempInstagram = newTempInstagram
    }

    fun saveInstagram() {
        saveInstagramInteractor.save(tempInstagram)
        dismissInstagramDialog()
    }

    fun dismissInstagramDialog() {
        tempInstagram = ""
        viewState.dismissInstagramDialog()
    }

    // --- Change YouTube ---

    fun showYouTubeDialog() = viewState.showYouTubeDialog()

    // Prefill YouTube dialog with currently entered text or current value
    fun getYouTubePrefill() = if (tempYouTube != "") tempYouTube else repo.currentUserYouTube()

    fun updateTempYouTube(newTempYouTube: String) {
        tempYouTube = newTempYouTube
    }

    fun saveYouTube() {
        saveYouTubeInteractor.save(tempYouTube)
        dismissYouTubeDialog()
    }

    fun dismissYouTubeDialog() {
        tempYouTube = ""
        viewState.dismissYouTubeDialog()
    }

    // --- Change website ---

    fun showWebsiteDialog() = viewState.showWebsiteDialog()

    // Prefill website dialog with currently entered text or current value
    fun getWebsitePrefill() = if (tempWebsite != "") tempWebsite else repo.currentUserWebsite()

    fun updateTempWebsite(newTempWebsite: String) {
        tempWebsite = newTempWebsite
    }

    fun saveWebsite() {
        saveWebsiteInteractor.save(tempWebsite)
        dismissWebsiteDialog()
    }

    fun dismissWebsiteDialog() {
        tempWebsite = ""
        viewState.dismissWebsiteDialog()
    }

    // --- Change residence ---

    fun showResidenceDialog() = viewState.showResidenceDialog()

    // Prefill residence dialog with currently entered text or current value
    fun getResidencePrefill() = if (tempResidence != "") tempResidence else repo.currentUserResidence()

    fun updateTempResidence(newTempResidence: String) {
        tempResidence = newTempResidence
    }

    fun saveResidence() {
        saveResidenceInteractor.save(tempResidence)
        dismissResidenceDialog()
    }

    fun dismissResidenceDialog() {
        tempResidence = ""
        viewState.dismissResidenceDialog()
    }

    // --- Change language ---

    fun showLanguageDialog() = viewState.showLanguageDialog()

    // Prefill language dialog with currently entered text or current value
    fun getLanguagePrefill() = if (tempLanguage != "") tempLanguage else repo.currentUserLanguage()

    fun updateTempLanguage(newTempLanguage: String) {
        tempLanguage = newTempLanguage
    }

    fun saveLanguage() {
        saveLanguageInteractor.save(tempLanguage)
        dismissLanguageDialog()
    }

    fun dismissLanguageDialog() {
        tempLanguage = ""
        viewState.dismissLanguageDialog()
    }

    // --- Change education ---

    fun showEducationDialog() = viewState.showEducationDialog()

    // Prefill education dialog with currently entered text or current value
    fun getEducationPrefill() = if (tempEducation != "") tempEducation else repo.currentUserEducation()

    fun updateTempEducation(newTempEducation: String) {
        tempEducation = newTempEducation
    }

    fun saveEducation() {
        saveEducationInteractor.save(tempEducation)
        dismissEducationDialog()
    }

    fun dismissEducationDialog() {
        tempEducation = ""
        viewState.dismissEducationDialog()
    }

    // --- Change work ---

    fun showWorkDialog() = viewState.showWorkDialog()

    // Prefill work dialog with currently entered text or current value
    fun getWorkPrefill() = if (tempWork != "") tempWork else repo.currentUserWork()

    fun updateTempWork(newTempWork: String) {
        tempWork = newTempWork
    }

    fun saveWork() {
        saveWorkInteractor.save(tempWork)
        dismissWorkDialog()
    }

    fun dismissWorkDialog() {
        tempWork = ""
        viewState.dismissWorkDialog()
    }

    // --- Change interests ---

    fun showInterestsDialog() = viewState.showInterestsDialog()

    // Prefill interests dialog with currently entered text or current value
    fun getInterestsPrefill() = if (tempInterests != "") tempInterests else repo.currentUserInterests()

    fun updateTempInterests(newTempInterests: String) {
        tempInterests = newTempInterests
    }

    fun saveInterests() {
        saveInterestsInteractor.save(tempInterests)
        dismissInterestsDialog()
    }

    fun dismissInterestsDialog() {
        tempInterests = ""
        viewState.dismissInterestsDialog()
    }

    // --- Change status ---

    fun showStatusDialog() {
        // TODO: implement
    }

    // --- Open photos ---

    fun openPhotos(photoUrlList: MutableList<String>) = viewState.openPhotos(photoUrlList)

    // --- Open privacy policy ---

    fun openPrivacyPolicy() = viewState.openPrivacyPolicy()

    // --- Open all reviews ---

    fun openAllReviews() = viewState.openAllReviews()

    // === Private methods ===

    private fun showToast(message: String) {
        viewState.showToast(message)
    }
}