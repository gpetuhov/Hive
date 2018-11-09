package com.gpetuhov.android.hive.domain.model

import com.gpetuhov.android.hive.util.getDateTimeFromTimestamp

data class Chatroom (
    var chatroomUid: String,
    var secondUserUid: String,
    var secondUserName: String,
    var lastMessageText: String,
    var lastMessageTimestamp: Long
) {
    fun getLastMessageTime() = getDateTimeFromTimestamp(lastMessageTimestamp)
}