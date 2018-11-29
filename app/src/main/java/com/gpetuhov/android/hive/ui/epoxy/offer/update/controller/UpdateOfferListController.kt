package com.gpetuhov.android.hive.ui.epoxy.offer.update.controller

import com.airbnb.epoxy.EpoxyController
import com.gpetuhov.android.hive.presentation.presenter.UpdateOfferFragmentPresenter
import com.gpetuhov.android.hive.ui.epoxy.offer.update.models.updateOfferDetails
import com.gpetuhov.android.hive.ui.epoxy.offer.update.models.updateOfferHeader

class UpdateOfferListController(private val presenter: UpdateOfferFragmentPresenter) : EpoxyController() {

    override fun buildModels() {
        updateOfferHeader {
            id("update_offer_header")

            onBackButtonClick { presenter.navigateUp() }

            // TODO: show/hide depending on new offer or offer edit
            deleteButtonVisible(false)
            onDeleteButtonClick { presenter.showDeleteOfferDialog() }

            onSaveButtonClick { presenter.saveOffer() }
        }

        updateOfferDetails {
            id("update_offer_details")

            // TODO: implement logic here
            title("Add title")
            description("Add description")
        }
    }
}