package com.gpetuhov.android.hive.util

import java.text.DateFormat
import java.util.*

// Get date and time from timestamp in seconds
fun getDateTimeFromTimestamp(timestamp: Long) = getDateTimeFromTimestamp(timestamp, false)

// Get date and time from timestamp in milliseconds
fun getDateTimeFromTimestampInMilliseconds(timestamp: Long) = getDateTimeFromTimestamp(timestamp, true)

// === Private ===

private fun getDateTimeFromTimestamp(timestamp: Long, isMillis: Boolean): String {
    return try {
        val format = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT)
        val date = Date(timestamp * (if (isMillis) 1 else 1000))
        format.format(date)
    } catch (ex: Exception) {
        ""
    }
}
