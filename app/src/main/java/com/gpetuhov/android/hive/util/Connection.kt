package com.gpetuhov.android.hive.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo

fun isOnline(context: Context?): Boolean {
    context ?: return false

    val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
    return activeNetwork?.isConnected == true
}
