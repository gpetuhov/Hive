package com.gpetuhov.android.hive.ui.epoxy.offer.details.controller

import android.content.Context
import com.gpetuhov.android.hive.R
import com.gpetuhov.android.hive.application.HiveApp
import com.gpetuhov.android.hive.domain.model.Offer
import com.gpetuhov.android.hive.domain.model.User
import com.gpetuhov.android.hive.presentation.presenter.OfferDetailsFragmentPresenter
import com.gpetuhov.android.hive.ui.epoxy.base.controller.BaseController
import com.gpetuhov.android.hive.ui.epoxy.offer.details.models.*
import com.gpetuhov.android.hive.util.Constants
import com.gpetuhov.android.hive.util.Settings
import javax.inject.Inject

class OfferDetailsListController(private val presenter: OfferDetailsFragmentPresenter) : BaseController() {

    @Inject lateinit var context: Context
    @Inject lateinit var settings: Settings

    private var user: User? = null
    private var offer: Offer? = null

    init {
        HiveApp.appComponent.inject(this)
        mapModel.onClick { presenter.openLocation() }
    }

    override fun buildModels() {
        offerDetailsHeader {
            id("offer_details_header")
            onBackButtonClick { presenter.navigateUp() }
            favorite(offer?.isFavorite ?: false)
            onFavoriteButtonClick { presenter.favorite() }
        }

        if (offer != null && offer?.isActive == true) {
            val photoList = offer?.photoList ?: mutableListOf()
            photoCarousel(
                settings,
                photoList,
                true,
                false,
                { photoUrlList -> presenter.openPhotos(photoUrlList) },
                { /* Do nothing */ }
            )
        }

        offerDetailsTitle {
            id("offer_details_title")
            val offerDeleted = context.getString(R.string.offer_deleted)
            val offerTitle = if (offer != null && offer?.isActive == true) offer?.title ?: offerDeleted else offerDeleted
            title(offerTitle)
        }

        // Offer can become null if deleted by offer provider,
        // when we are inside offer details.
        if (offer != null && offer?.isActive == true) {
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

                val isFree = offer?.isFree ?: true
                val price = offer?.price ?: Constants.Offer.DEFAULT_PRICE
                free(isFree)
                price(if (isFree) context.getString(R.string.free_caps) else "$price USD")
            }

            offerDetailsReviews {
                id("offer_details_reviews")

                val reviewCount = offer?.reviewCount ?: 0
                val noReviews = reviewCount == 0
                val allReviews = context.getString(R.string.all_reviews)
                reviewsActionText(if (noReviews) context.getString(R.string.no_reviews) else "$allReviews ($reviewCount)")

                rating(offer?.rating ?: 0.0F)

                onClick { if (reviewCount != 0) presenter.openReviews() else presenter.postReview() }
            }

            mapModel.addTo(this)
        }
    }

    fun changeOffer(user: User, offer: Offer?) {
        this.user = user
        this.offer = offer

        mapModel.updateMap(user.location)

        requestModelBuild()
    }
}