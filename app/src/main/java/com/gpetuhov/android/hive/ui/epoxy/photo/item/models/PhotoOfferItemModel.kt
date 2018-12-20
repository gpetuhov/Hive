package com.gpetuhov.android.hive.ui.epoxy.photo.item.models

import android.widget.ImageView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.gpetuhov.android.hive.R
import com.gpetuhov.android.hive.ui.epoxy.base.KotlinHolder
import com.gpetuhov.android.hive.util.load

// This model is used in offer item photo carousels in user details and profile
@EpoxyModelClass(layout = R.layout.photo_offer_item_view)
abstract class PhotoOfferItemModel : EpoxyModelWithHolder<PhotoOfferItemHolder>() {

    @EpoxyAttribute lateinit var photoUrl: String
    @EpoxyAttribute lateinit var onClick: () -> Unit

    override fun bind(holder: PhotoOfferItemHolder) {
        holder.photo.load(photoUrl)
        holder.photo.setOnClickListener { onClick() }
    }
}

class PhotoOfferItemHolder : KotlinHolder() {
    val photo by bind<ImageView>(R.id.photo_offer_item_image)
}