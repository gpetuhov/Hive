package com.gpetuhov.android.hive.domain.model

data class Review(
    var uid: String,
    var providerUserUid: String,
    var offerUid: String,
    var authorUid: String,
    var authorName: String,
    var authorUserPicUrl: String,
    var text: String,
    var rating: Float,
    var timestamp: Long,
    var isFromCurrentUser: Boolean,
    var comment: String
) {
}