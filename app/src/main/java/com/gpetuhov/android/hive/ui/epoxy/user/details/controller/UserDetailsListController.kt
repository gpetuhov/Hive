package com.gpetuhov.android.hive.ui.epoxy.user.details.controller

import android.content.Context
import com.gpetuhov.android.hive.R
import com.gpetuhov.android.hive.application.HiveApp
import com.gpetuhov.android.hive.domain.model.User
import com.gpetuhov.android.hive.presentation.presenter.UserDetailsFragmentPresenter
import com.gpetuhov.android.hive.ui.epoxy.base.controller.UserBaseController
import com.gpetuhov.android.hive.ui.epoxy.user.details.models.*
import com.gpetuhov.android.hive.util.Settings
import org.jetbrains.anko.collections.forEachWithIndex
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

        summary(context)

        val hasPhone = user?.hasPhone ?: false
        val hasVisibleEmail = user?.hasVisibleEmail ?: false
        val hasSkype = user?.hasSkype ?: false
        val hasContacts = hasPhone || hasVisibleEmail || hasSkype
        if (hasContacts) {
            userDetailsContacts {
                id("user_details_contacts")

                val phone = user?.phone ?: ""
                phone(phone)
                phoneVisible(hasPhone)
                onPhoneClick { presenter.dialPhone(phone) }

                val email = user?.visibleEmail ?: ""
                email(email)
                emailVisible(hasVisibleEmail)
                emailSeparatorVisible(hasVisibleEmail && hasPhone)
                onEmailClick { presenter.sendEmail(email) }

                val skype = user?.skype ?: ""
                skype(skype)
                skypeVisible(hasSkype)
                skypeSeparatorVisible(hasSkype && (hasPhone || hasVisibleEmail))
                onSkypeClick { presenter.callSkype(skype) }
            }
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

        userDetailsReviewsHeader {
            id("user_details_reviews_header")
        }

        user?.offerList?.sortedByDescending { it.lastReviewTimestamp }?.forEachWithIndex { index, offer ->
            if (offer.isActive) lastOfferReview(offer, index)
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