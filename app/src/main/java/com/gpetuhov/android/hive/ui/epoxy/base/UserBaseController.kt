package com.gpetuhov.android.hive.ui.epoxy.base

import android.content.Context
import android.os.Bundle
import com.airbnb.epoxy.Carousel
import com.gpetuhov.android.hive.R
import com.gpetuhov.android.hive.domain.model.Offer
import com.gpetuhov.android.hive.domain.model.User
import com.gpetuhov.android.hive.ui.epoxy.offer.item.models.offerItem
import com.gpetuhov.android.hive.ui.epoxy.photo.item.models.PhotoOfferItemModel_
import com.gpetuhov.android.hive.util.Constants
import com.gpetuhov.android.hive.util.Settings

// Base controller for profile and user details
abstract class UserBaseController : BaseController() {

    companion object {
        private const val SELECTED_OFFER_PHOTO_MAP_KEY = "selectedOfferPhotoMap"
    }

    protected var user: User? = null

    private var selectedOfferPhotoMap = hashMapOf<String, Int>()

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putSerializable(SELECTED_OFFER_PHOTO_MAP_KEY, selectedOfferPhotoMap)
    }

    override fun onRestoreInstanceState(inState: Bundle?) {
        super.onRestoreInstanceState(inState)
        val restored = inState?.getSerializable(SELECTED_OFFER_PHOTO_MAP_KEY)
        if (restored != null) selectedOfferPhotoMap = restored as HashMap<String, Int>
    }

    // === Public methods ===

    fun changeUser(user: User) {
        this.user = user
        requestModelBuild()
    }

    // === Protected methods ===

    protected fun userOfferItem(context: Context, settings: Settings, offer: Offer, isProfile: Boolean, onClick: () -> Unit) {
        offerItemPhotoCarousel(settings, offer, !isProfile, onClick)
        offerItemDetails(context, settings, offer, isProfile, onClick)
    }

    // === Private methods ===

    private fun offerItemPhotoCarousel(settings: Settings, offer: Offer, limitVisible: Boolean, onClick: () -> Unit) {
        var offerPhotoList = offer.photoList

        if (limitVisible) {
            offerPhotoList = offerPhotoList
                .filterIndexed { index, item -> index < Constants.Offer.MAX_VISIBLE_PHOTO_COUNT }
                .toMutableList()
        }

        if (!offerPhotoList.isEmpty()) {
            carousel {
                id("${offer.uid}_photo_carousel")

                val padding = Carousel.Padding.dp(16, 0, 16, 0, 0)
                padding(padding)

                onBind { model, view, position ->
                    view.clipToPadding = true
                    view.addOnScrollListener(
                        buildScrollListener { lastScrollPosition -> selectedOfferPhotoMap[offer.uid] = lastScrollPosition}
                    )
                }

                withModelsIndexedFrom(offerPhotoList) { index, photo ->
                    PhotoOfferItemModel_()
                        .id(photo.uid)
                        .photoUrl(photo.downloadUrl)
                        .onClick {
                            settings.setSelectedPhotoPosition(index)
                            onClick()
                        }
                }
            }
        }
    }

    private fun offerItemDetails(context: Context, settings: Settings, offer: Offer, offerActiveVisible: Boolean, onClick: () -> Unit) {
        offerItem {
            id(offer.uid)
            active(offer.isActive)
            activeVisible(offerActiveVisible)
            title(offer.title)
            free(offer.isFree)
            price(if (offer.isFree) context.getString(R.string.free_caps) else "${offer.price} USD")
            onClick {
                settings.setSelectedPhotoPosition(selectedOfferPhotoMap[offer.uid] ?: 0)
                onClick()
            }
        }
    }
}