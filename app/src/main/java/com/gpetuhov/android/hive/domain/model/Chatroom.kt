package com.gpetuhov.android.hive.domain.model

import com.gpetuhov.android.hive.util.getDateTimeFromTimestamp

data class Chatroom (
    var secondUserUid: String,
    var secondUserName: String,
    var secondUserPicUrl: String,
    var lastMessageText: String,
    var lastMessageTimestamp: Long,
    var newMessageCount: Long
) {
    fun getLastMessageTime() = getDateTimeFromTimestamp(lastMessageTimestamp)
    fun getNewMessageCountString() = newMessageCount.toString()
}