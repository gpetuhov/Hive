package com.gpetuhov.android.hive.domain.model

data class Message(
    var senderUid: String,
    var timestamp: Long,
    var text: String
) {
    fun isFromUser(userUid: String) = senderUid == userUid
}