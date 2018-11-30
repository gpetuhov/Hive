package com.gpetuhov.android.hive.ui.epoxy.offer.details.controller

import com.airbnb.epoxy.EpoxyController
import com.gpetuhov.android.hive.domain.model.Offer
import com.gpetuhov.android.hive.domain.model.User
import com.gpetuhov.android.hive.presentation.presenter.OfferDetailsFragmentPresenter
import com.gpetuhov.android.hive.ui.epoxy.offer.details.models.offerDetailsDetails
import com.gpetuhov.android.hive.ui.epoxy.offer.details.models.offerDetailsHeader
import com.gpetuhov.android.hive.ui.epoxy.offer.details.models.offerDetailsTitle

class OfferDetailsListController(private val presenter: OfferDetailsFragmentPresenter) : EpoxyController() {

    private var offer: Offer? = null

    override fun buildModels() {
        offerDetailsHeader {
            id("offer_details_header")
            onBackButtonClick { presenter.navigateUp() }
        }

        offerDetailsTitle {
            id("offer_details_title")
            title(offer?.title ?: "")
        }

        offerDetailsDetails {
            id("offer_details_details")
            description(offer?.description ?: "")
        }
    }

    fun changeOffer(user: User, offerUid: String) {
        val offerList = user.offerList
        offer = offerList.firstOrNull { it.uid == offerUid }
        requestModelBuild()
    }
}