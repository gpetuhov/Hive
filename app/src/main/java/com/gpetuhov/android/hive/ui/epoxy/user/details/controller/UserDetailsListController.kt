package com.gpetuhov.android.hive.ui.epoxy.user.details.controller

import android.content.Context
import com.gpetuhov.android.hive.R
import com.gpetuhov.android.hive.application.HiveApp
import com.gpetuhov.android.hive.domain.model.User
import com.gpetuhov.android.hive.presentation.presenter.UserDetailsFragmentPresenter
import com.gpetuhov.android.hive.ui.epoxy.base.controller.UserBaseController
import com.gpetuhov.android.hive.ui.epoxy.user.details.models.userDetailsDescription
import com.gpetuhov.android.hive.ui.epoxy.user.details.models.userDetailsName
import com.gpetuhov.android.hive.ui.epoxy.user.details.models.userDetailsOfferHeader
import com.gpetuhov.android.hive.ui.epoxy.user.details.models.userDetailsSummary
import com.gpetuhov.android.hive.util.Settings
import com.gpetuhov.android.hive.util.getDateFromTimestampInMilliseconds
import javax.inject.Inject

class UserDetailsListController(private val presenter: UserDetailsFragmentPresenter) : UserBaseController() {

    @Inject lateinit var context: Context
    @Inject lateinit var settings: Settings

    init {
        HiveApp.appComponent.inject(this)
        mapModel.onClick { presenter.openLocation() }
    }

    override fun buildModels() {
        val photoList = user?.photoList ?: mutableListOf()
        photoCarousel(
            settings,
            photoList,
            true,
            true,
            { photoUrlList -> presenter.openPhotos(photoUrlList) },
            { /* Do nothing */ }
        )

        userDetailsName {
            id("user_details_name")
            userPicUrl(user?.userPicUrl ?: "")
            username(user?.getUsernameOrName() ?: "")
        }

        userDetailsSummary {
            id("user_details_summary")

            val creationTimestamp = user?.creationTimestamp ?: 0
            val creationDate = getDateFromTimestampInMilliseconds(creationTimestamp)

            creationDate("${context.getString(R.string.user_creation_date)} $creationDate")
            creationDateVisible(creationTimestamp != 0L)

            // TODO: calculate these
            activeOffersCount("${context.getString(R.string.user_active_offers_count)}: 0")
            totalReviewsCount("${context.getString(R.string.user_total_reviews_count)}: 0")
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
            if (offer.isActive) {
                userOfferItem(
                    context,
                    settings,
                    offer,
                    false,
                    true,
                    { presenter.favoriteOffer(offer.isFavorite, offer.uid) },
                    { presenter.openOffer(offer.uid) }
                )
            }
        }

        // MapModel will be bind here only once (after fragment creation),
        // because location is not annotated as epoxy attribute.
        mapModel.addTo(this)
    }

    override fun changeUser(user: User) {
        // Manually update MapModel with new location.
        // If the model is already bind, map will be updated.
        // Otherwise map will be updated, when the model is bind.
        mapModel.updateMap(user.location)

        super.changeUser(user)
    }
}