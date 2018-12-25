package com.gpetuhov.android.hive.util

import android.content.Context
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import com.gpetuhov.android.hive.R
import com.gpetuhov.android.hive.domain.model.User

fun updateUserPic(fragment: Fragment, user: User, destinationView: ImageView) =
    update(Glide.with(fragment), user.userPicUrl, destinationView)

fun updateUserPic(context: Context, userPicUrl: String, destinationView: ImageView) =
    update(Glide.with(context), userPicUrl, destinationView)

private fun update(glideManager: RequestManager, userPicUrl: String, destinationView: ImageView) {
    val glideBuilder = if (userPicUrl != "") glideManager.load(userPicUrl) else glideManager.load(R.drawable.ic_account_circle)

    glideBuilder
        .apply(RequestOptions.circleCropTransform().placeholder(R.drawable.ic_account_circle))
        .into(destinationView)
}
