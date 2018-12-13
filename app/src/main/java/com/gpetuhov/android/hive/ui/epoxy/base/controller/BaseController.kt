package com.gpetuhov.android.hive.ui.epoxy.base.controller

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.epoxy.Carousel
import com.airbnb.epoxy.EpoxyController
import com.gpetuhov.android.hive.domain.model.Photo
import com.gpetuhov.android.hive.ui.epoxy.base.carousel
import com.gpetuhov.android.hive.ui.epoxy.base.withModelsFrom
import com.gpetuhov.android.hive.ui.epoxy.photo.item.models.PhotoItemModel_
import com.gpetuhov.android.hive.util.Constants
import com.gpetuhov.android.hive.util.Settings

abstract class BaseController : EpoxyController() {

    companion object {
        private const val SCROLL_TO_SELECTED_PHOTO_KEY = "scrollToSelectedPhoto"
    }

    private var scrollToSelectedPhoto = true

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(SCROLL_TO_SELECTED_PHOTO_KEY, scrollToSelectedPhoto)
    }

    override fun onRestoreInstanceState(inState: Bundle?) {
        super.onRestoreInstanceState(inState)
        if (inState != null) scrollToSelectedPhoto = inState.getBoolean(SCROLL_TO_SELECTED_PHOTO_KEY, true)
    }

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

    // === Private methods ===

    private fun getPhotoUrlList(photoList: MutableList<Photo>) = photoList.map { it.downloadUrl }.toMutableList()
}