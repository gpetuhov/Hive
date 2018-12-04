package com.gpetuhov.android.hive.ui.epoxy.photo.models

import android.widget.ImageView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.gpetuhov.android.hive.R
import com.gpetuhov.android.hive.ui.epoxy.holder.KotlinHolder
import com.gpetuhov.android.hive.util.updateUserPic

@EpoxyModelClass(layout = R.layout.photo_item_view)
abstract class PhotoItemModel : EpoxyModelWithHolder<PhotoItemHolder>() {

    @EpoxyAttribute lateinit var photoUrl: String

    override fun bind(holder: PhotoItemHolder) {
        updateUserPic(holder.photo.context, photoUrl, holder.photo)
    }
}

class PhotoItemHolder : KotlinHolder() {
    val photo by bind<ImageView>(R.id.photo_item_image)
}