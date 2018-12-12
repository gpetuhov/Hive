package com.gpetuhov.android.hive.ui.epoxy.photo.item.models

import android.widget.ImageView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.gpetuhov.android.hive.R
import com.gpetuhov.android.hive.ui.epoxy.holder.KotlinHolder

@EpoxyModelClass(layout = R.layout.photo_offer_item_view)
abstract class PhotoOfferItemModel : EpoxyModelWithHolder<PhotoOfferItemHolder>() {

    @EpoxyAttribute lateinit var photoUrl: String
    @EpoxyAttribute lateinit var onClick: () -> Unit

    override fun bind(holder: PhotoOfferItemHolder) {
        holder.photo.setOnClickListener { onClick() }

        if (photoUrl != "") {
            Glide.with(holder.photo.context)
                .load(photoUrl)
                .apply(RequestOptions.centerCropTransform())
                .into(holder.photo)
        }
    }
}

class PhotoOfferItemHolder : KotlinHolder() {
    val photo by bind<ImageView>(R.id.photo_offer_item_image)
}