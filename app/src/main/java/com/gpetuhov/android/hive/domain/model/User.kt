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
    var isOnline: Boolean,
    var location: LatLng
) {
    var offerList = mutableListOf<Offer>()
    val hasUsername get() = username != ""

    fun hasActiveOffer() = offerList.any { it.isActive }

    fun getUsernameOrName() = if (hasUsername) username else name
}