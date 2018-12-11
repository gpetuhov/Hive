package com.gpetuhov.android.hive.util.epoxy

import com.airbnb.epoxy.EpoxyController
import com.gpetuhov.android.hive.domain.model.Photo

fun EpoxyController.getPhotoUrlList(photoList: MutableList<Photo>) = photoList.map { it.downloadUrl }.toMutableList()

fun EpoxyController.getSelectedPhotoPosition(selectedPhotoUid: String, photoList: MutableList<Photo>): Int {
    var selectedPhotoPosition = photoList.indexOfFirst { item -> item.uid == selectedPhotoUid }
    if (selectedPhotoPosition < 0) selectedPhotoPosition = 0
    return selectedPhotoPosition
}