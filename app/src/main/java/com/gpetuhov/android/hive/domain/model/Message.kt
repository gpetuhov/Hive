package com.gpetuhov.android.hive.domain.model

import java.text.DateFormat
import java.util.*

data class Message(
    var senderUid: String,
    var timestamp: Long,
    var text: String,
    var isFromCurrentUser: Boolean
) {
    fun getMessageTime(): String {
        return try {
            val format = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT)
            val date = Date(timestamp * 1000)
            format.format(date)
        } catch (ex: Exception) {
            ""
        }
    }
}