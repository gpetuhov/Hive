package com.gpetuhov.android.hive.ui.epoxy.offer.details.controller

import android.content.Context
import com.airbnb.epoxy.EpoxyController
import com.gpetuhov.android.hive.R
import com.gpetuhov.android.hive.application.HiveApp
import com.gpetuhov.android.hive.domain.model.Offer
import com.gpetuhov.android.hive.domain.model.User
import com.gpetuhov.android.hive.presentation.presenter.OfferDetailsFragmentPresenter
import com.gpetuhov.android.hive.ui.epoxy.offer.details.models.offerDetailsDetails
import com.gpetuhov.android.hive.ui.epoxy.offer.details.models.offerDetailsHeader
import com.gpetuhov.android.hive.ui.epoxy.offer.details.models.offerDetailsTitle
import com.gpetuhov.android.hive.ui.epoxy.offer.details.models.offerDetailsUser
import javax.inject.Inject

class OfferDetailsListController(private val presenter: OfferDetailsFragmentPresenter) : EpoxyController() {

    @Inject lateinit var context: Context

    private var user: User? = null
    private var offer: Offer? = null

    init {
        HiveApp.appComponent.inject(this)
    }

    override fun buildModels() {
        offerDetailsHeader {
            id("offer_details_header")
            onBackButtonClick { presenter.navigateUp() }
        }

        offerDetailsTitle {
            id("offer_details_title")
            title(offer?.title ?: context.getString(R.string.offer_deleted))
        }

        // Offer can become null if deleted by offer provider,
        // when we are inside offer details.
        if (offer != null) {
            offerDetailsUser {
                id("offer_details_user")
                onClick { presenter.openUserDetails() }
                userPicUrl(user?.userPicUrl ?: "")

                val providedBy = context.getString(R.string.provided_by)
                val username = user?.getUsernameOrName() ?: ""
                username("$providedBy $username")
            }

            offerDetailsDetails {
                id("offer_details_details")
                description(offer?.description ?: "")
            }
        }
    }

    fun changeOffer(user: User, offerUid: String) {
        this.user = user
        val offerList = user.offerList
        offer = offerList.firstOrNull { it.uid == offerUid }
        requestModelBuild()
    }
}