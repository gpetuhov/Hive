package com.gpetuhov.android.hive.ui.epoxy.photo.fullscreen.controller

import com.airbnb.epoxy.Carousel
import com.gpetuhov.android.hive.application.HiveApp
import com.gpetuhov.android.hive.ui.epoxy.base.controller.BaseController
import com.gpetuhov.android.hive.ui.epoxy.photo.fullscreen.models.PhotoFullscreenItemModel_
import com.gpetuhov.android.hive.util.Settings
import com.gpetuhov.android.hive.ui.epoxy.base.carousel
import com.gpetuhov.android.hive.ui.epoxy.base.withModelsFrom
import timber.log.Timber
import javax.inject.Inject

class PhotoFullscreenListController : BaseController() {

    @Inject lateinit var settings: Settings

    private var photoUrlList = mutableListOf<String>()

    init {
        HiveApp.appComponent.inject(this)
    }

    override fun buildModels() {
        if (!photoUrlList.isEmpty()) {
            carousel {
                id("photo_carousel")

                val scrollListener = buildScrollListener { lastScrollPosition ->
                    Timber.tag("PhotoCarousel").d("Full screen - Save photo position $lastScrollPosition")
                    settings.setSelectedPhotoPosition(lastScrollPosition)
                }

                // Scroll to the selected photo
                // (onBind() is called, when models are rebuilt)
                onBind { model, view, position ->
                    Timber.tag("PhotoCarousel").d("Full screen - onBind")
                    view.addOnScrollListener(scrollListener)
                    scrollToSavedSelectedPhotoPosition(settings, view, photoUrlList.size, false)
                }

                onUnbind { model, view ->
                    Timber.tag("PhotoCarousel").d("Full screen - onUnbind")
                    view.removeOnScrollListener(scrollListener)
                }

                // This adds spacing between photos
                val padding = Carousel.Padding.dp(0, 16)
                padding(padding)

                val listSize = photoUrlList.size

                withModelsFrom(photoUrlList) { index, item ->
                    PhotoFullscreenItemModel_()
                        .id(item)
                        .photoUrl(item)
                        .position("${index + 1}/$listSize")
                }
            }
        }
    }

    fun setPhotos(photoUrlList: MutableList<String>) {
        this.photoUrlList = photoUrlList
        requestModelBuild()
    }
}