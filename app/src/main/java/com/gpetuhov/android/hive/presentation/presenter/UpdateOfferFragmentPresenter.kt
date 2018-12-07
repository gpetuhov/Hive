package com.gpetuhov.android.hive.presentation.presenter

import android.net.Uri
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.gpetuhov.android.hive.application.HiveApp
import com.gpetuhov.android.hive.domain.interactor.DeleteOfferInteractor
import com.gpetuhov.android.hive.domain.interactor.SaveOfferInteractor
import com.gpetuhov.android.hive.domain.model.Image
import com.gpetuhov.android.hive.domain.model.Offer
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
    var title = ""
    var description = ""
    var active = false
    var activeEnabled = false
    var free = true
    var price = Constants.Offer.DEFAULT_PRICE
    var photoList= mutableListOf<Image>()

    private var tempTitle = ""
    private var tempDescription = ""
    private var tempPrice = Constants.Offer.DEFAULT_PRICE
    private var editStarted = false

    private var saveOfferInteractor = SaveOfferInteractor(this)
    private var deleteOfferInteractor = DeleteOfferInteractor(this)

    init {
        HiveApp.appComponent.inject(this)
    }

    // === SaveOfferInteractor.Callback ===

    override fun onSaveOfferSuccess() {
        onOperationComplete()
        viewState.navigateUp()
    }

    override fun onSaveOfferError(errorMessage: String) {
        onOperationComplete()
        showToast(errorMessage)
    }

    // === DeleteOfferInteractor.Callback ===

    override fun onDeleteOfferSuccess() {
        onOperationComplete()
        viewState.navigateUp()
    }

    override fun onDeleteOfferError(errorMessage: String) {
        onOperationComplete()
        showToast(errorMessage)
    }

    // === Public methods ===
    // --- Init ---

    fun initOffer(offerUid: String) {
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
                    photoList.clear()
                    photoList.addAll(offer.photoList)

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
        viewState.disableButtons()
        viewState.showProgress()

        val offer = Offer(uid, title, description, price, free, active)
        offer.photoList.clear()
        offer.photoList.addAll(photoList)

        saveOfferInteractor.saveOffer(offer)
    }

    // --- Delete offer ---

    fun showDeleteOfferDialog() {
        viewState.disableButtons()
        viewState.showDeleteOfferDialog()
    }

    fun deleteOffer() {
        viewState.showProgress()
        viewState.dismissDeleteOfferDialog()
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
        navigateUp()
    }

    fun quitOfferUpdateCancel() = viewState.dismissQuitOfferUpdateDialog()

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
        repo.addOfferPhoto(
            selectedImageUri,
            { photo -> onAddPhotoSuccess(photo) },
            { showToast(resultMessages.getAddPhotoErrorMessage()) }
        )
    }

    // --- Delete photo ---

    // TODO: implement
    fun showDeletePhotoDialog(photoUid: String) {
        showToast("Delete photo $photoUid")
    }

    // === Private methods ===

    private fun updateUI() = viewState.updateUI()

    private fun showToast(message: String) = viewState.showToast(message)

    private fun onOperationComplete() {
        viewState.enableButtons()
        viewState.hideProgress()
    }

    private fun navigateUp() = viewState.navigateUp()

    private fun onAddPhotoSuccess(photo: Image) {
        editStarted = true
        photoList.add(photo)
        updateUI()
    }
}