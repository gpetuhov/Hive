package com.gpetuhov.android.hive.presentation.presenter

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.gpetuhov.android.hive.presentation.view.UpdateOfferFragmentView

@InjectViewState
class UpdateOfferFragmentPresenter : MvpPresenter<UpdateOfferFragmentView>() {

    var title = ""
    var description = ""

    private var tempTitle = ""
    private var tempDescription = ""

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

    // TODO: implement this
    fun saveOffer() = showToast("Save offer")

    // --- Navigate up ---

    // TODO: show "Are you sure?" dialog here
    fun navigateUp() = viewState.navigateUp()

    // === Private methods ===

    private fun updateUI() = viewState.updateUI()

    private fun showToast(message: String) {
        viewState.showToast(message)
    }
}