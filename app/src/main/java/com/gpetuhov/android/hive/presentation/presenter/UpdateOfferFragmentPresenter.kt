package com.gpetuhov.android.hive.presentation.presenter

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.gpetuhov.android.hive.domain.interactor.SaveOfferInteractor
import com.gpetuhov.android.hive.domain.model.Offer
import com.gpetuhov.android.hive.presentation.view.UpdateOfferFragmentView

@InjectViewState
class UpdateOfferFragmentPresenter :
    MvpPresenter<UpdateOfferFragmentView>(),
    SaveOfferInteractor.Callback {

    var title = ""
    var description = ""

    private var tempTitle = ""
    private var tempDescription = ""

    private var saveOfferInteractor = SaveOfferInteractor(this)

    // === SaveOfferInteractor.Callback ===

    override fun onSaveOfferSuccess() = viewState.navigateUp()

    override fun onSaveOfferError(errorMessage: String) = showToast(errorMessage)

    // === Public methods ===
    // --- Change username ---

    fun showTitleDialog() = viewState.showTitleDialog()

    fun getTitlePrefill() = if (tempTitle != "") tempTitle else title

    fun updateTempTitle(newTempTitle: String) {
        tempTitle = newTempTitle
    }

    fun saveTitle() {
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
        description = tempDescription
        updateUI()
        dismissDescriptionDialog()
    }

    fun dismissDescriptionDialog() {
        tempDescription = ""
        viewState.dismissDescriptionDialog()
    }

    // --- Delete offer ---

    // TODO: implement this
    fun showDeleteOfferDialog() = showToast("Delete offer")

    // --- Save offer ---

    // Init offer uid with empty string, because this is the new offer
    fun saveOffer() = saveOfferInteractor.saveOffer(Offer("", title, description, 0.0, false, true))

    // --- Navigate up ---

    // TODO: show "Are you sure?" dialog here
    fun navigateUp() = viewState.navigateUp()

    // === Private methods ===

    private fun updateUI() = viewState.updateUI()

    private fun showToast(message: String) {
        viewState.showToast(message)
    }
}