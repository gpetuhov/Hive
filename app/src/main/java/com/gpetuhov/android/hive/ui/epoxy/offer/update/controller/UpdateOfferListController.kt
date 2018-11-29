package com.gpetuhov.android.hive.ui.epoxy.offer.update.controller

import com.airbnb.epoxy.EpoxyController
import com.gpetuhov.android.hive.presentation.presenter.UpdateOfferFragmentPresenter
import com.gpetuhov.android.hive.ui.epoxy.offer.update.models.updateOfferDetails

class UpdateOfferListController(private val presenter: UpdateOfferFragmentPresenter) : EpoxyController() {

    override fun buildModels() {
        updateOfferDetails {
            id("update_offer_details")

            // TODO: implement logic here
            title("Add title")
            description("Add description")
        }
    }
}