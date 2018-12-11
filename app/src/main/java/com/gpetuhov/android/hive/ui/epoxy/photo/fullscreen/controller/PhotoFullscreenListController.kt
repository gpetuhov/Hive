package com.gpetuhov.android.hive.ui.epoxy.photo.fullscreen.controller

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.epoxy.Carousel
import com.airbnb.epoxy.EpoxyController
import com.gpetuhov.android.hive.application.HiveApp
import com.gpetuhov.android.hive.presentation.presenter.PhotoFragmentPresenter
import com.gpetuhov.android.hive.ui.epoxy.photo.fullscreen.models.PhotoFullscreenItemModel_
import com.gpetuhov.android.hive.util.Settings
import com.gpetuhov.android.hive.util.epoxy.carousel
import com.gpetuhov.android.hive.util.epoxy.withModelsFrom
import javax.inject.Inject

class PhotoFullscreenListController(private val presenter: PhotoFragmentPresenter) : EpoxyController() {

    @Inject lateinit var settings: Settings

    private var photoUrlList = mutableListOf<String>()
    private var selectedPhotoPosition = 0

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
                    view.scrollToPosition(selectedPhotoPosition)
                    settings.setSelectedPhotoPosition(selectedPhotoPosition)
                }

                // This adds spacing between photos
                val padding = Carousel.Padding.dp(0, 16)
                padding(padding)

                withModelsFrom(photoUrlList) {
                    PhotoFullscreenItemModel_()
                        .id(it)
                        .photoUrl(it)
                }
            }
        }
    }

    // === Public methods ===

    fun setPhotos(selectedPhotoPosition: Int, photoUrlList: MutableList<String>) {
        this.selectedPhotoPosition = selectedPhotoPosition
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