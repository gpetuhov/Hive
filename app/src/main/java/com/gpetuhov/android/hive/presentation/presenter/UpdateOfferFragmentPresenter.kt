package com.gpetuhov.android.hive.presentation.presenter

import android.net.Uri
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.gpetuhov.android.hive.application.HiveApp
import com.gpetuhov.android.hive.domain.interactor.DeleteOfferInteractor
import com.gpetuhov.android.hive.domain.interactor.SaveOfferInteractor
import com.gpetuhov.android.hive.domain.model.Photo
import com.gpetuhov.android.hive.domain.model.Offer
import com.gpetuhov.android.hive.domain.model.User
import com.gpetuhov.android.hive.domain.repository.Repo
import com.gpetuhov.android.hive.domain.util.ResultMessages
import com.gpetuhov.android.hive.presentation.view.UpdateOfferFragmentView
import com.gpetuhov.android.hive.util.Constants
import javax.inject.Inject

@InjectViewState
class UpdateOfferFragmentPresenter :
    MvpPresenter<UpdateOfferFragmentView>(),
    SaveOfferInteractor.Callback,
    DeleteOfferInteractor.Callback {

    @Inject lateinit var repo: Repo
    @Inject lateinit var resultMessages: ResultMessages

    var uid = ""
    var userUid = ""
    var title = ""
    var description = ""
    var active = false
    var activeEnabled = false
    var free = true
    var price = Constants.Offer.DEFAULT_PRICE
    var photoList= mutableListOf<Photo>()
    var rating = 0.0F
    var reviewCount = 0

    var isDeleteButtonVisible = false

    private var tempTitle = ""
    private var tempDescription = ""
    private var tempPrice = Constants.Offer.DEFAULT_PRICE
    private var editStarted = false

    private var saveOfferInteractor = SaveOfferInteractor(this)
    private var deleteOfferInteractor = DeleteOfferInteractor(this)

    private var deletePhotoUid = ""

    // True if offer editing is stopped (user has chosen quit, save or delete)
    private var isOfferEditStopped = false

    init {
        HiveApp.appComponent.inject(this)
    }

    // === SaveOfferInteractor.Callback ===

    override fun onSaveOfferSuccess() = onSuccess()

    override fun onSaveOfferError(errorMessage: String) = onError(errorMessage)

    // === DeleteOfferInteractor.Callback ===

    override fun onDeleteOfferSuccess() = onSuccess()

    override fun onDeleteOfferError(errorMessage: String) = onError(errorMessage)

    // === Public methods ===
    // --- Init ---

    fun initOffer(offerUid: String) {
        userUid = repo.currentUserUid()

        val offerList = repo.currentUserOfferList()
        val activeOfferCount = offerList.filter { it.isActive }.size
        val activeOfferCountLessThanMax = activeOfferCount < Constants.Offer.MAX_ACTIVE_OFFER_COUNT

        // Init UI with data from offer, if offer exists (uid not empty)
        // and user has not started editing yet.
        if (offerUid != "") {
            if (!editStarted) {
                val offer = offerList.firstOrNull { it.uid == offerUid }
                if (offer != null) {
                    uid = offerUid
                    title = offer.title
                    description = offer.description
                    active = offer.isActive
                    free = offer.isFree
                    price = offer.price
                    photoList = copyPhotoList(offer.photoList)
                    rating = offer.rating
                    reviewCount = offer.reviewCount

                    // For existing offer enable active switch if active offer count is less than max
                    // or if offer is already active (to be able to turn it off)
                    activeEnabled = activeOfferCountLessThanMax || offer.isActive
                }
            }

        } else {
            // For new offer set active and enable active switch only if active offer count is less than max
            active = activeOfferCountLessThanMax
            activeEnabled = activeOfferCountLessThanMax
        }

        isDeleteButtonVisible = uid != ""
    }

    // --- Change title ---

    fun showTitleDialog() = viewState.showTitleDialog()

    fun getTitlePrefill() = if (tempTitle != "") tempTitle else title

    fun updateTempTitle(newTempTitle: String) {
        tempTitle = newTempTitle
    }

    fun saveTitle() {
        editStarted = true
        title = tempTitle
        updateUI()
        dismissTitleDialog()
    }

    fun dismissTitleDialog() {
        tempTitle = ""
        viewState.dismissTitleDialog()
    }

    // --- Change description ---

    fun showDescriptionDialog() = viewState.showDescriptionDialog()

    fun getDescriptionPrefill() = if (tempDescription != "") tempDescription else description

    fun updateTempDescription(newTempDescription: String) {
        tempDescription = newTempDescription
    }

    fun saveDescription() {
        editStarted = true
        description = tempDescription
        updateUI()
        dismissDescriptionDialog()
    }

    fun dismissDescriptionDialog() {
        tempDescription = ""
        viewState.dismissDescriptionDialog()
    }

    // --- Save offer ---

    fun saveOffer() {
        if (shouldQuit()) {
            // If title, description and photo list are not empty and edit not started, just quit
            navigateUp()

        } else {
            // Otherwise try to save offer
            viewState.disableButtons()
            viewState.showProgress()

            val offer = Offer(uid, userUid, title, description, price, free, active)
            offer.photoList.clear()
            offer.photoList.addAll(photoList)

            saveOfferInteractor.saveOffer(offer)
        }
    }

    // --- Delete offer ---

    fun showDeleteOfferDialog() {
        viewState.disableButtons()
        viewState.showDeleteOfferDialog()
    }

    fun deleteOffer() {
        viewState.showProgress()
        viewState.dismissDeleteOfferDialog()
        deleteNewPhotosFromStorage()
        deleteOfferInteractor.deleteOffer(uid)
    }

    fun deleteOfferCancel() {
        onOperationComplete()
        viewState.dismissDeleteOfferDialog()
    }

    // --- Quit offer update

    fun showQuitOfferUpdateDialog() {
        if (editStarted) {
            viewState.showQuitOfferUpdateDialog()
        } else {
            navigateUp()
        }
    }

    fun quitOfferUpdate() {
        viewState.dismissQuitOfferUpdateDialog()
        cancelPhotoUploads()
        deleteNewPhotosFromStorage()
        navigateUp()
    }

    fun saveOfferFromQuitDialog() {
        viewState.dismissQuitOfferUpdateDialog()
        saveOffer()
    }

    // --- Offer is active ---

    fun onActiveClick(isActive: Boolean) {
        if (activeEnabled) {
            active = isActive
            editStarted = true
        }
    }

    // --- Offer is free ---

    fun onFreeClick(isFree: Boolean) {
        free = isFree
        editStarted = true
        updateUI()
    }

    // --- Change price ---

    fun showPriceDialog() = viewState.showPriceDialog()

    fun getPricePrefill(): String {
        return when {
            tempPrice != Constants.Offer.DEFAULT_PRICE -> tempPrice.toString()
            price != Constants.Offer.DEFAULT_PRICE -> price.toString()
            else -> ""
        }
    }

    fun updateTempPrice(newTempPrice: Double) {
        tempPrice = newTempPrice
    }

    fun savePrice() {
        editStarted = true
        price = tempPrice
        updateUI()
        dismissPriceDialog()
    }

    fun dismissPriceDialog() {
        tempPrice = Constants.Offer.DEFAULT_PRICE
        viewState.dismissPriceDialog()
    }

    // --- Add photo ---

    fun choosePhoto() = viewState.choosePhoto()

    fun addPhoto(selectedImageUri: Uri) {
        editStarted = true
        repo.addOfferPhoto(
            selectedImageUri,
            { photo -> onAddPhotoSuccess(photo) },
            { showToast(resultMessages.getAddPhotoErrorMessage()) }
        )
    }

    // --- Delete photo ---

    fun showDeletePhotoDialog(photoUid: String) {
        deletePhotoUid = photoUid
        viewState.showDeletePhotoDialog()
    }

    fun deletePhoto() {
        editStarted = true
        viewState.dismissDeletePhotoDialog()
        deletePhotoOrMarkAsDeleted()
        deletePhotoUid = ""
        updateUI()
    }

    fun deletePhotoCancel() {
        deletePhotoUid = ""
        viewState.dismissDeletePhotoDialog()
    }

    // --- Open photos ---

    fun openPhotos(photoUrlList: MutableList<String>) = viewState.openPhotos(photoUrlList)

    // --- Open reviews ---

    fun openReviews() {
        if (uid != "") {
            repo.clearReviews()
            viewState.openReviews(uid)
        }
    }

    // --- Update reviews ---

    fun updateReviews(user: User) {
        val offerList = user.offerList
        val offer = offerList.firstOrNull { it.uid == uid }

        if (offer != null) {
            rating = offer.rating
            reviewCount = offer.reviewCount
            updateUI()
        }
    }

    // === Private methods ===

    private fun updateUI() = viewState.updateUI()

    private fun showToast(message: String) = viewState.showToast(message)

    private fun onOperationComplete() {
        viewState.enableButtons()
        viewState.hideProgress()
    }

    private fun onSuccess() {
        onOperationComplete()
        cancelPhotoUploads()
        viewState.navigateUp()
    }

    private fun onError(errorMessage: String) {
        onOperationComplete()
        showToast(errorMessage)
    }

    private fun navigateUp() = viewState.navigateUp()

    private fun onAddPhotoSuccess(photo: Photo) {
        if (!isOfferEditStopped) {
            // If offer editing has not been stopped, mark photo as new, add it to list and update UI
            photo.markAsNew()
            photoList.add(photo)
            updateUI()

        } else {
            // If offer editing has been stopped, delete uploaded photo from Cloud Storage,
            // because photo has not been added to the offer
            // We get here if the user has stopped editing offer after the photo has already been uploaded,
            // but before it has been added to offer photo list.
            // If we don't delete it here, it will remain in Cloud Storage forever without user knowing about it.
            repo.deleteOfferPhotoFromStorage(photo.uid)
        }
    }

    private fun deletePhotoOrMarkAsDeleted() {
        val index = photoList.indexOfFirst { it.uid == deletePhotoUid }

        if (index >=0 && index < photoList.size) {
            val photo = photoList[index]
            if (photo.isNew) {
                // If photo is new, delete it from photo list and Cloud Storage immediately
                photoList.removeAt(index)
                repo.deleteOfferPhotoFromStorage(deletePhotoUid)

            } else {
                // Otherwise just mark photo as deleted,
                // so that it will be deleted if the user confirms offer changes
                photoList[index].markAsDeleted()
            }
        }
    }

    // This is needed so that offer photos won't be updated inside repo directly
    private fun copyPhotoList(photoList: MutableList<Photo>): MutableList<Photo> {
        val result = mutableListOf<Photo>()

        for (photo in photoList) {
            val photoCopy = Photo(photo.uid, photo.downloadUrl)
            result.add(photoCopy)
        }

        return result
    }

    private fun deleteNewPhotosFromStorage() = photoList.filter { it.isNew }.forEach { repo.deleteOfferPhotoFromStorage(it.uid) }

    private fun cancelPhotoUploads() {
        isOfferEditStopped = true
        repo.cancelPhotoUploadTasks()
    }

    private fun shouldQuit(): Boolean {
        // Count photos that are NOT marked as deleted
        val photoCount = photoList.filter { !it.isDeleted }.size

        return title != "" && description != "" && photoCount != 0 && !editStarted
    }
}