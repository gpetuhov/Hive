package com.gpetuhov.android.hive.ui.epoxy.photo.fullscreen.controller

import com.airbnb.epoxy.Carousel
import com.airbnb.epoxy.EpoxyController
import com.gpetuhov.android.hive.domain.model.User
import com.gpetuhov.android.hive.presentation.presenter.PhotoFragmentPresenter
import com.gpetuhov.android.hive.ui.epoxy.photo.fullscreen.models.PhotoFullscreenItemModel_
import com.gpetuhov.android.hive.util.Constants
import com.gpetuhov.android.hive.util.epoxy.carousel
import com.gpetuhov.android.hive.util.epoxy.withModelsFrom

class PhotoFullscreenListController(private val presenter: PhotoFragmentPresenter) : EpoxyController() {

    private var carousel: Carousel? = null
    private var user: User? = null

    override fun buildModels() {
        val offerUid = presenter.offerUid

        val photoList = if (offerUid != "") {
            // Show offer photos
            user?.offerList?.firstOrNull { it.uid == offerUid }?.photoList ?: mutableListOf()

        } else {
            // Otherwise show user photos
            user?.photoList ?: mutableListOf()
        }

        if (!photoList.isEmpty()) {
            val maxVisiblePhotoCount = if (offerUid != "") Constants.Offer.MAX_VISIBLE_PHOTO_COUNT else Constants.User.MAX_VISIBLE_PHOTO_COUNT
            val visiblePhotos = photoList.filterIndexed { index, item -> index < maxVisiblePhotoCount }

            var initialPhotoPosition = visiblePhotos.indexOfFirst { it.uid == presenter.photoUid }
            if (initialPhotoPosition < 0) initialPhotoPosition = 0

            carousel {
                id("photo_carousel")

                onBind { model, view, position ->
                    carousel = view
                    carousel?.scrollToPosition(initialPhotoPosition)
                }

                onUnbind { model, view ->
                    carousel = null
                }

                // This adds spacing between photos
                val padding = Carousel.Padding.dp(0, 16)
                padding(padding)

                withModelsFrom(visiblePhotos) {
                    PhotoFullscreenItemModel_()
                        .id(it.uid)
                        .photoUrl(it.downloadUrl)
                }
            }
        }
    }

    fun changeUser(user: User) {
        this.user = user
        requestModelBuild()
    }
}