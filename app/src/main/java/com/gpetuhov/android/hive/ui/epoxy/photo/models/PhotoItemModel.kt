package com.gpetuhov.android.hive.ui.epoxy.photo.models

import android.widget.ImageView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.gpetuhov.android.hive.R
import com.gpetuhov.android.hive.ui.epoxy.holder.KotlinHolder

@EpoxyModelClass(layout = R.layout.photo_item_view)
abstract class PhotoItemModel : EpoxyModelWithHolder<PhotoItemHolder>() {

    @EpoxyAttribute lateinit var photoUrl: String
    @EpoxyAttribute lateinit var onLongClick: () -> Unit

    override fun bind(holder: PhotoItemHolder) {
        holder.photo.setOnLongClickListener {
            onLongClick()
            true
        }

        if (photoUrl != "") {
            Glide.with(holder.photo.context)
                .load(photoUrl)
                .apply(RequestOptions.centerCropTransform())
                .into(holder.photo)
        }
    }
}

class PhotoItemHolder : KotlinHolder() {
    val photo by bind<ImageView>(R.id.photo_item_image)
}