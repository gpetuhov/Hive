package com.gpetuhov.android.hive.ui.epoxy.photo.fullscreen.controller

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.epoxy.Carousel
import com.airbnb.epoxy.EpoxyController
import com.gpetuhov.android.hive.application.HiveApp
import com.gpetuhov.android.hive.ui.epoxy.photo.fullscreen.models.PhotoFullscreenItemModel_
import com.gpetuhov.android.hive.util.Settings
import com.gpetuhov.android.hive.util.epoxy.carousel
import com.gpetuhov.android.hive.util.epoxy.scrollToSavedSelectedPhotoPosition
import com.gpetuhov.android.hive.util.epoxy.withModelsIndexedFrom
import javax.inject.Inject

class PhotoFullscreenListController : EpoxyController() {

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
                    view.addOnScrollListener(getScrollListener())
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

    // === Public methods ===

    fun setPhotos(photoUrlList: MutableList<String>) {
        this.photoUrlList = photoUrlList
        requestModelBuild()
    }

    // === Private methods ===

    private fun getScrollListener(): RecyclerView.OnScrollListener {
        return object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    val lastScrollPosition = (recyclerView.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
                    settings.setSelectedPhotoPosition(lastScrollPosition)
                }
            }
        }
    }
}