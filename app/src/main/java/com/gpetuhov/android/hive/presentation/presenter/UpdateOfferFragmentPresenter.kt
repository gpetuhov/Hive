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

    // TODO: implement this
    fun showDeleteOfferDialog() = showToast("Delete offer")

    // TODO: implement this
    fun saveOffer() = showToast("Save offer")

    // TODO: show "Are you sure?" dialog here
    fun navigateUp() = viewState.navigateUp()

    // === Private methods ===

    private fun showToast(message: String) {
        viewState.showToast(message)
    }
}