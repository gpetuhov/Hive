package com.gpetuhov.android.hive.domain.model

// This is what the user offers to the world
data class Offer(
    var uid: String,
    var userUid: String,
    var title: String,
    var description: String,
    var price: Double,
    var isFree: Boolean,
    var isActive: Boolean,
    var isFavorite: Boolean = false,
    var rating: Float = 0.0F,
    var reviewCount: Int = 0,
    var lastReviewAuthorName: String = "",
    var lastReviewAuthorUserPicUrl: String = "",
    var lastReviewText: String = "",
    var lastReviewTimestamp: Long = 0L,
    var starCount: Long = 0L,
    var distance: Double = 0.0
) {
    var photoList = mutableListOf<Photo>()
    val starCountString get() = starCount.toString()
    val hasReviews get() = reviewCount > 0
}