package com.gpetuhov.android.hive.util

import android.content.Context
import android.util.DisplayMetrics
import android.view.WindowManager

fun getScreenHeight(context: Context): Int {
    val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager?
    val display = wm?.defaultDisplay
    val metrics = DisplayMetrics()
    display?.getMetrics(metrics)
    return metrics.heightPixels
}

fun getStatusBarHeight(context: Context): Int {
    var statusBarHeight = 0

    val resourceId = context.resources.getIdentifier("status_bar_height", "dimen", "android")
    if (resourceId > 0) {
        statusBarHeight = context.resources.getDimensionPixelSize(resourceId)
    }

    return statusBarHeight
}
