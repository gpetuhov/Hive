package com.gpetuhov.android.hive.util

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

fun ImageView.load(photoUrl: String, crop: Boolean) {
    if (photoUrl != "") {
        Glide.with(context)
            .load(photoUrl)
            .apply(
                if (crop) RequestOptions.centerCropTransform() else RequestOptions.centerInsideTransform()
            )
            .into(this)
    }
}