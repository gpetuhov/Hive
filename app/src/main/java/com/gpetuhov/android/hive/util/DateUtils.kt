package com.gpetuhov.android.hive.util

import java.text.DateFormat
import java.util.*

// Get date and time from timestamp in seconds
fun getDateTimeFromTimestamp(timestamp: Long): String {
    return try {
        val format = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT)
        val date = Date(timestamp * 1000)
        format.format(date)
    } catch (ex: Exception) {
        ""
    }
}
