package com.gpetuhov.android.hive.domain.model

import java.text.SimpleDateFormat
import java.util.*

data class Message(
    var senderUid: String,
    var timestamp: Long,
    var text: String
) {

    fun isFromUser(userUid: String) = senderUid == userUid

    fun getMessageTime(): String {
        return try {
            val format = SimpleDateFormat("yyyy.MM.dd HH:mm:ss")
            val netDate = Date(timestamp * 1000)
            format.format(netDate)
        } catch (ex: Exception) {
            ""
        }
    }
}