package com.gpetuhov.android.hive.ui.epoxy.photo.fullscreen.controller

import com.airbnb.epoxy.Carousel
import com.gpetuhov.android.hive.application.HiveApp
import com.gpetuhov.android.hive.ui.epoxy.base.controller.BaseController
import com.gpetuhov.android.hive.ui.epoxy.photo.fullscreen.models.PhotoFullscreenItemModel_
import com.gpetuhov.android.hive.util.Settings
import com.gpetuhov.android.hive.ui.epoxy.base.carousel
import com.gpetuhov.android.hive.ui.epoxy.base.withModelsIndexedFrom
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

                // Scroll to the selected photo
                // (onBind() is called, when models are rebuilt)
                onBind { model, view, position ->
                    view.addOnScrollListener(
                        buildScrollListener { lastScrollPosition -> settings.setSelectedPhotoPosition(lastScrollPosition) }
                    )
                    scrollToSavedSelectedPhotoPosition(settings, view, photoUrlList.size, false)
                }

                // This adds spacing between photos
                val padding = Carousel.Padding.dp(0, 16)
                padding(padding)

                val listSize = photoUrlList.size

                withModelsIndexedFrom(photoUrlList) { index, item ->
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