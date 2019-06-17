package com.gpetuhov.android.hive.ui.epoxy.offer.details.controller

import android.content.Context
import com.gpetuhov.android.hive.R
import com.gpetuhov.android.hive.application.HiveApp
import com.gpetuhov.android.hive.domain.model.Offer
import com.gpetuhov.android.hive.domain.model.User
import com.gpetuhov.android.hive.presentation.presenter.OfferDetailsFragmentPresenter
import com.gpetuhov.android.hive.ui.epoxy.base.controller.BaseController
import com.gpetuhov.android.hive.ui.epoxy.offer.details.models.*
import com.gpetuhov.android.hive.ui.epoxy.review.models.reviewsHeader
import com.gpetuhov.android.hive.ui.epoxy.review.models.reviewsSummary
import com.gpetuhov.android.hive.ui.epoxy.user.details.models.userDeleted
import com.gpetuhov.android.hive.util.Constants
import com.gpetuhov.android.hive.util.Settings
import com.gpetuhov.android.hive.util.getLastSeenText
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
        val isUserDeleted = user?.isDeleted ?: false

        if (!isUserDeleted) {
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

                    val isOnline = user?.isOnline ?: false
                    onlineVisible(isOnline && !isUserDeleted)

                    val lastSeen = user?.getLastSeenTime() ?: ""
                    lastSeen(getLastSeenText(context, lastSeen))
                    lastSeenVisible(!isOnline && !isUserDeleted && lastSeen != "")
                }

                offerDetailsDetails {
                    id("offer_details_details")
                    description(offer?.description ?: "")

                    val isFree = offer?.isFree ?: true
                    val price = offer?.price ?: Constants.Offer.DEFAULT_PRICE
                    free(isFree)
                    price(if (isFree) context.getString(R.string.free_caps) else "$price USD")
                }

                reviewsHeader {
                    id("offer_details_reviews_header")
                }

                lastOfferReview(offer, 0)

                reviewsSummary {
                    id("offer_details_reviews_summary")

                    val reviewCount = offer?.reviewCount ?: 0
                    val noReviews = reviewCount == 0
                    val allReviews = context.getString(R.string.all_reviews)
                    reviewsActionText(if (noReviews) context.getString(R.string.no_reviews) else "$allReviews ($reviewCount)")

                    val offerRating = offer?.rating ?: 0.0F
                    rating(offerRating)
                    ratingVisible(offerRating != 0.0F)

                    onClick { presenter.openReviews() }
                }

                mapModel.addTo(this)
            }

        } else {
            // If user account deleted show user deleted message
            userDeleted { id("user_account_deleted") }
        }
    }

    fun changeOffer(user: User, offer: Offer?) {
        this.user = user
        this.offer = offer

        mapModel.updateMap(user.location, user.location.toString())

        requestModelBuild()
    }
}