package com.gpetuhov.android.hive.ui.epoxy.offer.item.models

import android.view.View
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.gpetuhov.android.hive.R
import com.gpetuhov.android.hive.ui.epoxy.base.KotlinHolder
import com.gpetuhov.android.hive.util.getPriceColorId
import com.gpetuhov.android.hive.util.getStarResourceId
import com.gpetuhov.android.hive.util.load
import com.gpetuhov.android.hive.util.setVisible

@EpoxyModelClass(layout = R.layout.offer_item_one_photo_view)
abstract class OfferItemOnePhotoModel : EpoxyModelWithHolder<OfferItemOnePhotoHolder>() {

    @EpoxyAttribute lateinit var photoUrl: String

    @EpoxyAttribute lateinit var title: String

    @EpoxyAttribute var free = true
    @EpoxyAttribute lateinit var price: String

    @EpoxyAttribute var favorite = false
    @EpoxyAttribute lateinit var onFavoriteButtonClick: () -> Unit

    @EpoxyAttribute var rating = 0.0F
    @EpoxyAttribute var reviewCount = 0

    @EpoxyAttribute lateinit var onClick: () -> Unit

    override fun bind(holder: OfferItemOnePhotoHolder) {
        holder.photo.load(photoUrl, true)

        holder.title.text = title
        holder.price.text = price

        holder.price.setTextColor(ContextCompat.getColor(holder.price.context, getPriceColorId(free)))

        holder.favoriteButton.setImageResource(getStarResourceId(favorite))
        holder.favoriteButton.setOnClickListener { onFavoriteButtonClick() }

        val ratingVisible = reviewCount != 0
        holder.ratingWrapper.setVisible(ratingVisible)
        holder.ratingBar.rating = rating
        holder.reviewCount.text = reviewCount.toString()

        holder.rootView.setOnClickListener { onClick() }
    }
}

class OfferItemOnePhotoHolder : KotlinHolder() {
    val rootView by bind<View>(R.id.offer_item_one_photo_root)
    val photo by bind<ImageView>(R.id.offer_item_one_photo_image)
    val title by bind<TextView>(R.id.offer_item_one_photo_title)
    val price by bind<TextView>(R.id.offer_item_one_photo_price)
    val favoriteButton by bind<ImageView>(R.id.offer_item_one_photo_favorite_button)
    val ratingWrapper by bind<View>(R.id.offer_item_one_photo_rating_wrapper)
    val ratingBar by bind<RatingBar>(R.id.offer_item_one_photo_rating_bar)
    val reviewCount by bind<TextView>(R.id.offer_item_one_photo_review_count)
}