package com.gpetuhov.android.hive.domain.model

data class Rating (
    var offerUid: String,
    var rating: Float,
    var reviewCount: Int,
    var lastReviewAuthorName: String,
    var lastReviewAuthorUserPicUrl: String,
    var lastReviewText: String,
    var lastReviewTimestamp: Long
) {
}