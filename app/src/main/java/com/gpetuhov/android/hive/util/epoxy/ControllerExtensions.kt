package com.gpetuhov.android.hive.util.epoxy

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.epoxy.Carousel
import com.airbnb.epoxy.EpoxyController
import com.gpetuhov.android.hive.domain.model.Photo
import com.gpetuhov.android.hive.util.Settings

fun EpoxyController.getPhotoUrlList(photoList: MutableList<Photo>) = photoList.map { it.downloadUrl }.toMutableList()

fun EpoxyController.scrollToSavedSelectedPhotoPosition(settings: Settings, carousel: Carousel, photoListSize: Int, resetSavedPosition: Boolean) {
    var selectedPhotoPosition = settings.getSelectedPhotoPosition()
    if (selectedPhotoPosition < 0 || selectedPhotoPosition >= photoListSize) selectedPhotoPosition = 0

    carousel.scrollToPosition(selectedPhotoPosition)

    if (resetSavedPosition) settings.setSelectedPhotoPosition(0)
}

fun EpoxyController.buildScrollListener(onScroll: (Int) -> Unit): RecyclerView.OnScrollListener {
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
