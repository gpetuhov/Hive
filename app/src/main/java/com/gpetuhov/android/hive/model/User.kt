package com.gpetuhov.android.hive.model

import com.google.android.gms.maps.model.LatLng

// Represents data both for the current user and search results
data class User(
    var uid: String,
    var name: String,
    var username: String,
    var email: String,
    var isOnline: Boolean,
    var location: LatLng
) {
    val hasUsername get() = username != ""
}