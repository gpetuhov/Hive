package com.gpetuhov.android.hive.domain.model

import com.gpetuhov.android.hive.util.getDateTimeFromTimestamp

data class Chatroom (
    var chatroomUid: String,
    var secondUserUid: String,    // Uid of the second user in the chat (not the current user)
    var lastMessageText: String,
    var lastMessageTimestamp: Long
) {
    fun getLastMessageTime() = getDateTimeFromTimestamp(lastMessageTimestamp)
}