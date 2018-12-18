package com.gpetuhov.android.hive.domain.model

import com.google.android.gms.maps.model.LatLng

// Represents data both for the current user and search results.
// Models at domain layer are just POJOs for keeping data.
data class User(
    var uid: String,
    var name: String,
    var username: String,
    var email: String,
    var userPicUrl: String,
    var description: String,
    var isOnline: Boolean,
    var location: LatLng,
    var offerSearchResultIndex: Int = -1,    // index of the offer that corresponds to search query text
    var isFavorite: Boolean = false
) {
    var offerList = mutableListOf<Offer>()
    var photoList = mutableListOf<Photo>()
    val hasUsername get() = username != ""
    val hasDescription get() = description != ""

    fun hasActiveOffer() = offerList.any { it.isActive }

    fun getUsernameOrName() = if (hasUsername) username else name

    fun getOffer(offerUid: String) = offerList.firstOrNull { it.uid == offerUid }
}