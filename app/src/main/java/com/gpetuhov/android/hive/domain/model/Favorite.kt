package com.gpetuhov.android.hive.domain.model

data class Favorite(
    var userUid: String,
    var offerUid: String
) {
    fun isOffer() = offerUid != ""
}