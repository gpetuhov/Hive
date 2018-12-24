package com.gpetuhov.android.hive.domain.model

data class Rating (
    var offerUid: String,
    var rating: Float,
    var reviewCount: Int
) {
}