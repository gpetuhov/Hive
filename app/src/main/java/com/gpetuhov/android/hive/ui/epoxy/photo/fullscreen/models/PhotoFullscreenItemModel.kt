package com.gpetuhov.android.hive.ui.epoxy.photo.fullscreen.models

import android.widget.ImageView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.gpetuhov.android.hive.R
import com.gpetuhov.android.hive.ui.epoxy.holder.KotlinHolder

@EpoxyModelClass(layout = R.layout.photo_fullscreen_item_view)
abstract class PhotoFullscreenItemModel : EpoxyModelWithHolder<PhotoFullscreenItemHolder>() {

    @EpoxyAttribute lateinit var photoUrl: String

    override fun bind(holder: PhotoFullscreenItemHolder) {
        if (photoUrl != "") {
            Glide.with(holder.photo.context)
                .load(photoUrl)
                .apply(RequestOptions.centerInsideTransform())
                .into(holder.photo)
        }
    }
}

class PhotoFullscreenItemHolder : KotlinHolder() {
    val photo by bind<ImageView>(R.id.photo_fullscreen_item_image)
}