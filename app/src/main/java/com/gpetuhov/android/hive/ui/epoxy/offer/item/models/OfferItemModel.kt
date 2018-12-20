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
import com.gpetuhov.android.hive.util.setVisible

@EpoxyModelClass(layout = R.layout.offer_item_view)
abstract class OfferItemModel : EpoxyModelWithHolder<OfferItemHolder>() {

    @EpoxyAttribute var active = false
    @EpoxyAttribute var activeVisible = false

    @EpoxyAttribute lateinit var title: String

    @EpoxyAttribute var free = true
    @EpoxyAttribute lateinit var price: String

    @EpoxyAttribute lateinit var onClick: () -> Unit

    @EpoxyAttribute var favorite = false
    @EpoxyAttribute var favoriteButtonVisible = true
    @EpoxyAttribute lateinit var onFavoriteButtonClick: () -> Unit

    @EpoxyAttribute var rating = 0.0F
    @EpoxyAttribute var reviewCount = 0

    override fun bind(holder: OfferItemHolder) {
        holder.active.setImageResource(if (active) R.drawable.circle_green else R.drawable.circle_red)
        holder.active.setVisible(activeVisible)

        holder.title.text = title

        holder.price.text = price

        holder.price.setTextColor(ContextCompat.getColor(holder.price.context, getPriceColorId(free)))

        holder.favoriteButton.setImageResource(getStarResourceId(favorite))
        holder.favoriteButton.setVisible(favoriteButtonVisible)
        holder.favoriteButton.setOnClickListener { onFavoriteButtonClick() }

        val ratingVisible = reviewCount != 0
        holder.ratingWrapper.setVisible(ratingVisible)
        holder.ratingBar.rating = rating
        holder.reviewCount.text = reviewCount.toString()

        holder.rootView.setOnClickListener { onClick() }
    }
}

class OfferItemHolder : KotlinHolder() {
    val rootView by bind<View>(R.id.offer_item_root)
    val active by bind<ImageView>(R.id.offer_item_active)
    val title by bind<TextView>(R.id.offer_item_title)
    val price by bind<TextView>(R.id.offer_item_price)
    val favoriteButton by bind<ImageView>(R.id.offer_item_favorite_button)
    val ratingWrapper by bind<View>(R.id.offer_item_rating_wrapper)
    val ratingBar by bind<RatingBar>(R.id.offer_item_rating_bar)
    val reviewCount by bind<TextView>(R.id.offer_item_review_count)
}