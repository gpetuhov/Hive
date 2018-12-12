package com.gpetuhov.android.hive.ui.epoxy.photo.fullscreen.models

import android.widget.ImageView
import android.widget.TextView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.gpetuhov.android.hive.R
import com.gpetuhov.android.hive.ui.epoxy.holder.KotlinHolder
import com.gpetuhov.android.hive.util.getScreenHeight
import com.gpetuhov.android.hive.util.getStatusBarHeight

@EpoxyModelClass(layout = R.layout.photo_fullscreen_item_view)
abstract class PhotoFullscreenItemModel : EpoxyModelWithHolder<PhotoFullscreenItemHolder>() {

    @EpoxyAttribute lateinit var photoUrl: String
    @EpoxyAttribute lateinit var position: String

    override fun bind(holder: PhotoFullscreenItemHolder) {
        setImageHeight(holder.photo)

        if (photoUrl != "") {
            Glide.with(holder.photo.context)
                .load(photoUrl)
                .apply(RequestOptions.centerInsideTransform())
                .into(holder.photo)
        }

        holder.position.text = position
    }

    private fun setImageHeight(imageView: ImageView) {
        val imageHeight = getScreenHeight(imageView.context) - getStatusBarHeight(imageView.context)
        imageView.layoutParams.height = imageHeight
    }
}

class PhotoFullscreenItemHolder : KotlinHolder() {
    val photo by bind<ImageView>(R.id.photo_fullscreen_item_image)
    val position by bind<TextView>(R.id.photo_fullscreen_item_position)
}