package com.gpetuhov.android.hive.util

import android.content.Context
import android.util.DisplayMetrics
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.gpetuhov.android.hive.R

// === Extensions ===

fun View.setVisible(visible: Boolean) {
    visibility = if (visible) View.VISIBLE else View.GONE
}

fun ImageView.load(photoUrl: String, crop: Boolean) {
    if (photoUrl != "") {
        Glide.with(context)
            .load(photoUrl)
            .apply(
                (if (crop) RequestOptions.centerCropTransform() else RequestOptions.centerInsideTransform()).placeholder(R.drawable.ic_photo)
            )
            .into(this)
    }
}

// === Methods ===

fun getStarResourceId(favorite: Boolean) = if (favorite) R.drawable.ic_star else R.drawable.ic_star_border

fun getPriceColorId(free: Boolean) = if (free) R.color.md_red_600 else R.color.md_grey_600

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