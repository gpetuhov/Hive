package com.gpetuhov.android.hive.util.epoxy

import com.airbnb.epoxy.Carousel
import com.airbnb.epoxy.EpoxyController
import com.gpetuhov.android.hive.domain.model.Photo
import com.gpetuhov.android.hive.util.Settings

fun EpoxyController.getPhotoUrlList(photoList: MutableList<Photo>) = photoList.map { it.downloadUrl }.toMutableList()

fun EpoxyController.getSelectedPhotoPosition(settings: Settings, selectedPhotoUid: String, photoList: MutableList<Photo>): Int {
    var selectedPhotoPosition = photoList.indexOfFirst { item -> item.uid == selectedPhotoUid }
    if (selectedPhotoPosition < 0) selectedPhotoPosition = 0
    settings.setSelectedPhotoPosition(selectedPhotoPosition)
    return selectedPhotoPosition
}

fun EpoxyController.scrollToSavedSelectedPhotoPosition(settings: Settings, carousel: Carousel, photoList: MutableList<Photo>) {
    var selectedPhotoPosition = settings.getSelectedPhotoPosition()
    if (selectedPhotoPosition < 0 || selectedPhotoPosition >= photoList.size) selectedPhotoPosition = 0

    carousel.scrollToPosition(selectedPhotoPosition)
    settings.setSelectedPhotoPosition(0)
}