package com.gpetuhov.android.hive.util

import java.text.DateFormat
import java.util.*

// Get date from timestamp in seconds
fun getDateFromTimestamp(timestamp: Long) = getDateTimeFromTimestamp(
    timestamp = timestamp,
    isMillis = false,
    isWithTime = false
)

// Get date from timestamp in milliseconds
fun getDateFromTimestampInMilliseconds(timestamp: Long) = getDateTimeFromTimestamp(
    timestamp = timestamp,
    isMillis = true,
    isWithTime = false
)

// Get date and time from timestamp in seconds
fun getDateTimeFromTimestamp(timestamp: Long) = getDateTimeFromTimestamp(
    timestamp = timestamp,
    isMillis = false,
    isWithTime = true
)

// Get date and time from timestamp in milliseconds
fun getDateTimeFromTimestampInMilliseconds(timestamp: Long) = getDateTimeFromTimestamp(
    timestamp = timestamp,
    isMillis = true,
    isWithTime = true
)

// === Private ===

private fun getDateTimeFromTimestamp(timestamp: Long, isMillis: Boolean, isWithTime: Boolean): String {
    return try {
        val format =
            if (isWithTime) DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT)
            else DateFormat.getDateInstance(DateFormat.SHORT)

        val date = Date(timestamp * (if (isMillis) 1 else 1000))
        format.format(date)

    } catch (ex: Exception) {
        ""
    }
}
