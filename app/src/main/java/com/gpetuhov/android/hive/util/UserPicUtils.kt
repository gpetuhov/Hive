package com.gpetuhov.android.hive.util

import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.gpetuhov.android.hive.R
import com.gpetuhov.android.hive.domain.model.User

fun updateUserPic(fragment: Fragment, user: User, destinationView: ImageView) {
    val userPicUrl = user.userPicUrl
    val glideManager = Glide.with(fragment)
    val glideBuilder = if (userPicUrl != "") glideManager.load(userPicUrl) else glideManager.load(R.drawable.ic_account_circle)

    glideBuilder
        .apply(RequestOptions.circleCropTransform())
        .into(destinationView)
}
