package com.gpetuhov.android.hive.ui.epoxy.user.controller

import android.content.Context
import com.gpetuhov.android.hive.R
import com.gpetuhov.android.hive.application.HiveApp
import com.gpetuhov.android.hive.presentation.presenter.UserDetailsFragmentPresenter
import com.gpetuhov.android.hive.ui.epoxy.base.UserBaseController
import com.gpetuhov.android.hive.ui.epoxy.user.models.userDetailsDescription
import com.gpetuhov.android.hive.ui.epoxy.user.models.userDetailsHeader
import com.gpetuhov.android.hive.ui.epoxy.user.models.userDetailsName
import com.gpetuhov.android.hive.ui.epoxy.user.models.userDetailsOfferHeader
import com.gpetuhov.android.hive.util.Settings
import javax.inject.Inject

class UserDetailsListController(private val presenter: UserDetailsFragmentPresenter) : UserBaseController() {

    @Inject lateinit var context: Context
    @Inject lateinit var settings: Settings

    init {
        HiveApp.appComponent.inject(this)
    }

    override fun buildModels() {
        userDetailsHeader {
            id("user_details_header")
            onBackButtonClick { presenter.navigateUp() }
        }

        val photoList = user?.photoList ?: mutableListOf()
        photoCarousel(
            settings,
            photoList,
            true,
            { photoUrlList -> presenter.openPhotos(photoUrlList) },
            { /* Do nothing */ }
        )

        userDetailsName {
            id("user_details_name")
            userPicUrl(user?.userPicUrl ?: "")
            username(user?.getUsernameOrName() ?: "")
        }

        val hasDescription = user?.hasDescription ?: false
        if (hasDescription) {
            userDetailsDescription {
                id("user_details_description")
                description(user?.description ?: "")
            }
        }

        userDetailsOfferHeader {
            id("user_details_offer_header")

            val hasActiveOffer = user?.hasActiveOffer() ?: false
            offerHeader(if (hasActiveOffer) context.getString(R.string.offers) else context.getString(R.string.no_active_offer))
        }

        user?.offerList?.forEach { offer ->
            // In user details show only active offers
            if (offer.isActive) userOfferItem(context, settings, offer, false) { presenter.openOffer(offer.uid) }
        }
    }
}