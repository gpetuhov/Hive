package com.gpetuhov.android.hive.ui.epoxy.photo.item.models

import android.widget.ImageView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.gpetuhov.android.hive.R
import com.gpetuhov.android.hive.ui.epoxy.base.KotlinHolder
import com.gpetuhov.android.hive.util.load

// This model is used in profile, user details, offer details and offer update photo carousels
@EpoxyModelClass(layout = R.layout.photo_item_view)
abstract class PhotoItemModel : EpoxyModelWithHolder<PhotoItemHolder>() {

    @EpoxyAttribute lateinit var photoUrl: String
    @EpoxyAttribute lateinit var onClick: () -> Unit
    @EpoxyAttribute lateinit var onLongClick: () -> Unit

    override fun bind(holder: PhotoItemHolder) {
        holder.photo.setOnClickListener { onClick() }

        holder.photo.setOnLongClickListener {
            onLongClick()
            true
        }

        holder.photo.load(photoUrl)
    }
}

class PhotoItemHolder : KotlinHolder() {
    val photo by bind<ImageView>(R.id.photo_item_image)
}