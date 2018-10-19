package com.gpetuhov.android.hive.model

import com.google.android.gms.maps.model.LatLng

data class User(
    var uid: String,
    var name: String,
    var email: String,
    var isOnline: Boolean,
    var location: LatLng
)