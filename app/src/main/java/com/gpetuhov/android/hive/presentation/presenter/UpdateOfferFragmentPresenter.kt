package com.gpetuhov.android.hive.presentation.presenter

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.gpetuhov.android.hive.application.HiveApp
import com.gpetuhov.android.hive.domain.interactor.DeleteOfferInteractor
import com.gpetuhov.android.hive.domain.interactor.SaveOfferInteractor
import com.gpetuhov.android.hive.domain.model.Offer
import com.gpetuhov.android.hive.domain.repository.Repo
import com.gpetuhov.android.hive.presentation.view.UpdateOfferFragmentView
import javax.inject.Inject

@InjectViewState
class UpdateOfferFragmentPresenter :
    MvpPresenter<UpdateOfferFragmentView>(),
    SaveOfferInteractor.Callback,
    DeleteOfferInteractor.Callback {

    @Inject lateinit var repo: Repo

    var uid = ""
    var title = ""
    var description = ""

    private var tempTitle = ""
    private var tempDescription = ""
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
        // Init UI with data from offer, if offer exists (uid not empty)
        // and user has not started editing yet.
        if (offerUid != "" && !editStarted) {
            val offerList = repo.currentUserOfferList()
            val offer = offerList.firstOrNull { it.uid == offerUid }
            if (offer != null) {
                uid = offerUid
                title = offer.title
                description = offer.description
            }
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
        saveOfferInteractor.saveOffer(Offer(uid, title, description, 0.0, false, true))
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

    // --- Navigate up ---

    // TODO: show "Are you sure?" dialog here
    fun navigateUp() = viewState.navigateUp()

    // === Private methods ===

    private fun updateUI() = viewState.updateUI()

    private fun showToast(message: String) = viewState.showToast(message)

    private fun onOperationComplete() {
        viewState.enableButtons()
        viewState.hideProgress()
    }
}