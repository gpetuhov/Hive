package com.gpetuhov.android.hive.util

import android.content.Context
import com.gpetuhov.android.hive.R
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

fun getLastSeenTimeFromTimestamp(timestamp: Long): String {
    val timestampInMillis = timestamp * 1000
    val now = Calendar.getInstance()
    val timeToCheck = Calendar.getInstance()
    timeToCheck.timeInMillis = timestampInMillis

    return if (now.get(Calendar.YEAR) == timeToCheck.get(Calendar.YEAR)
        && (now.get(Calendar.DAY_OF_YEAR) == timeToCheck.get(Calendar.DAY_OF_YEAR))) {
        // If today, then display only time

        val hours = timeToCheck.get(Calendar.HOUR_OF_DAY)
        val minutes = timeToCheck.get(Calendar.MINUTE)

        "$hours:$minutes"

    } else {
        // Otherwise display full date
        getDateTimeFromTimestamp(timestamp)
    }
}

fun getLastSeenText(context: Context?, lastSeen: String): String {
    // For full date display "last seen", for only time display "last seen at"
    val lastSeenPrefix = (if (lastSeen.length > 5) context?.getString(R.string.last_seen) else context?.getString(R.string.last_seen_at)) ?: ""
    return "$lastSeenPrefix $lastSeen"
}

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