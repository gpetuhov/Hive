package com.gpetuhov.android.hive.ui.epoxy.offer.item.models

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.gpetuhov.android.hive.R
import com.gpetuhov.android.hive.ui.epoxy.base.KotlinHolder

@EpoxyModelClass(layout = R.layout.offer_item_one_photo_view)
abstract class OfferItemOnePhotoModel : EpoxyModelWithHolder<OfferItemOnePhotoHolder>() {

    @EpoxyAttribute lateinit var photoUrl: String

    @EpoxyAttribute lateinit var title: String

    @EpoxyAttribute var free = true
    @EpoxyAttribute lateinit var price: String

    @EpoxyAttribute lateinit var onClick: () -> Unit

    override fun bind(holder: OfferItemOnePhotoHolder) {
        if (photoUrl != "") {
            Glide.with(holder.photo.context)
                .load(photoUrl)
                .apply(RequestOptions.centerCropTransform())
                .into(holder.photo)
        }

        holder.title.text = title
        holder.price.text = price

        val colorId = if (free) R.color.md_red_600 else R.color.md_grey_600
        holder.price.setTextColor(ContextCompat.getColor(holder.price.context, colorId))

        holder.rootView.setOnClickListener { onClick() }
    }
}

class OfferItemOnePhotoHolder : KotlinHolder() {
    val rootView by bind<View>(R.id.offer_item_one_photo_root)
    val photo by bind<ImageView>(R.id.offer_item_one_photo_image)
    val title by bind<TextView>(R.id.offer_item_one_photo_title)
    val price by bind<TextView>(R.id.offer_item_one_photo_price)
}