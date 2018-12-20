package com.gpetuhov.android.hive.util

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

fun ImageView.load(photoUrl: String) {
    if (photoUrl != "") {
        Glide.with(context)
            .load(photoUrl)
            .apply(RequestOptions.centerCropTransform())
            .into(this)
    }
}