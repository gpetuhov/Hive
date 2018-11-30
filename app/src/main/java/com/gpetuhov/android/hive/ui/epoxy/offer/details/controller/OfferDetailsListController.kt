package com.gpetuhov.android.hive.ui.epoxy.offer.details.controller

import com.airbnb.epoxy.EpoxyController
import com.gpetuhov.android.hive.domain.model.User
import com.gpetuhov.android.hive.presentation.presenter.OfferDetailsFragmentPresenter
import com.gpetuhov.android.hive.ui.epoxy.offer.details.models.offerDetailsHeader

class OfferDetailsListController(private val presenter: OfferDetailsFragmentPresenter) : EpoxyController() {

    private var user: User? = null
    private var offerUid = ""

    override fun buildModels() {
        offerDetailsHeader {
            id("offer_details_header")
            onBackButtonClick { presenter.navigateUp() }
        }
    }

    fun changeOffer(user: User, offerUid: String) {
        this.user = user
        this.offerUid = offerUid
        requestModelBuild()
    }
}