package com.gpetuhov.android.hive.domain.model

import com.gpetuhov.android.hive.util.getDateTimeFromTimestamp

data class Message(
    var uid: String,
    var senderUid: String,
    var timestamp: Long,
    var text: String,
    var isFromCurrentUser: Boolean,
    var isRead: Boolean
) {
    fun getMessageTime() = getDateTimeFromTimestamp(timestamp)
}