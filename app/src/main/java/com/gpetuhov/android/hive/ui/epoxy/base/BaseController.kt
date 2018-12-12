package com.gpetuhov.android.hive.ui.epoxy.base

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.epoxy.Carousel
import com.airbnb.epoxy.EpoxyController
import com.gpetuhov.android.hive.domain.model.Photo
import com.gpetuhov.android.hive.util.Settings

abstract class BaseController : EpoxyController() {

    // === Protected methods ===

    protected fun getPhotoUrlList(photoList: MutableList<Photo>) = photoList.map { it.downloadUrl }.toMutableList()

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
}