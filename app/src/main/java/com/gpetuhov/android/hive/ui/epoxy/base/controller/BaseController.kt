package com.gpetuhov.android.hive.ui.epoxy.base.controller

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.epoxy.Carousel
import com.airbnb.epoxy.EpoxyController
import com.gpetuhov.android.hive.domain.model.Offer
import com.gpetuhov.android.hive.domain.model.Photo
import com.gpetuhov.android.hive.ui.epoxy.base.carousel
import com.gpetuhov.android.hive.ui.epoxy.base.withModelsFrom
import com.gpetuhov.android.hive.ui.epoxy.map.models.MapModel_
import com.gpetuhov.android.hive.ui.epoxy.photo.item.models.PhotoItemModel_
import com.gpetuhov.android.hive.ui.epoxy.review.models.reviewItem
import com.gpetuhov.android.hive.util.Constants
import com.gpetuhov.android.hive.util.Settings
import com.gpetuhov.android.hive.util.getDateTimeFromTimestamp

abstract class BaseController : EpoxyController() {

    protected var mapModel: MapModel_ = MapModel_().id("map")
    private var scrollToSelectedPhoto = true

    // === Protected methods ===

    protected fun scrollToSavedSelectedPhotoPosition(settings: Settings, carousel: Carousel, photoListSize: Int, resetSavedPosition: Boolean) {
        var selectedPhotoPosition = settings.getSelectedPhotoPosition()
        if (selectedPhotoPosition < 0 || selectedPhotoPosition >= photoListSize) selectedPhotoPosition = 0

        carousel.scrollToPosition(selectedPhotoPosition)

        if (resetSavedPosition) settings.setSelectedPhotoPosition(0)
    }

    protected fun buildScrollListener(onScroll: (Int) -> Unit): RecyclerView.OnScrollListener {
        return object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    val lastScrollPosition = (recyclerView.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
                    onScroll(lastScrollPosition)
                }
            }
        }
    }

    protected fun photoCarousel(
        settings: Settings,
        photoListSource: MutableList<Photo>,
        limitVisible: Boolean,
        isUserPhotos: Boolean,
        onClick: (MutableList<String>) -> Unit,
        onLongClick: (String) -> Unit
    ) {
        var photoList = mutableListOf<Photo>()
        photoList.addAll(photoListSource)

        if (limitVisible) {
            val maxPhotoCount = if (isUserPhotos) Constants.User.MAX_VISIBLE_PHOTO_COUNT else Constants.Offer.MAX_VISIBLE_PHOTO_COUNT
            photoList = photoList.filterIndexed { index, item -> index < maxPhotoCount }.toMutableList()
        }

        if (!photoList.isEmpty()) {
            carousel {
                id("photo_carousel")

                paddingDp(0)

                onBind { model, view, position ->
                    if (scrollToSelectedPhoto) {
                        scrollToSelectedPhoto = false
                        scrollToSavedSelectedPhotoPosition(settings, view, photoList.size, true)
                    }
                }

                withModelsFrom(photoList) { index, item ->
                    PhotoItemModel_()
                        .id(item.uid)
                        .photoUrl(item.downloadUrl)
                        .onClick {
                            settings.setSelectedPhotoPosition(index)
                            onClick(getPhotoUrlList(photoList))
                        }
                        .onLongClick { onLongClick(item.uid) }
                }
            }
        }
    }

    // Add ReviewItemModel for the last review of the offer
    protected fun lastOfferReview(offer: Offer?, offerIndex: Int) {
        val lastReviewAuthorName = offer?.lastReviewAuthorName ?: ""
        val lastReviewAuthorPicUrl = offer?.lastReviewAuthorUserPicUrl ?: ""
        val lastReviewText = offer?.lastReviewText ?: ""
        val lastReviewTimestamp = offer?.lastReviewTimestamp ?: 0

        if (
            lastReviewAuthorName != ""
            && lastReviewAuthorPicUrl != ""
            && lastReviewText != ""
            && lastReviewTimestamp != 0L
        ) {
            reviewItem {
                id("offer_last_review_$offerIndex")
                userPicUrl(lastReviewAuthorPicUrl)
                username(lastReviewAuthorName)
                time(getDateTimeFromTimestamp(lastReviewTimestamp))
                reviewText(lastReviewText)
                rating(0.0F)
                ratingVisible(false)
                controlsVisible(false)
                onEditClick { /* Do nothing */ }
                onDeleteClick { /* Do nothing */ }
                commentVisible(false)
                onCommentClick { /* Do nothing */ }
                commentTextVisible(false)
                commentText("")
                commentControlsVisible(false)
                onCommentEditClick { /* Do nothing */ }
                onCommentDeleteClick { /* Do nothing */ }
            }
        }
    }

    // === Private methods ===

    private fun getPhotoUrlList(photoList: MutableList<Photo>) = photoList.map { it.downloadUrl }.toMutableList()
}