package com.gpetuhov.android.hive.ui.epoxy.offer.item.models

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.gpetuhov.android.hive.R
import com.gpetuhov.android.hive.ui.epoxy.base.KotlinHolder

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

    override fun bind(holder: OfferItemHolder) {
        holder.active.setImageResource(if (active) R.drawable.circle_green else R.drawable.circle_red)
        holder.active.visibility = if (activeVisible) View.VISIBLE else View.GONE

        holder.title.text = title

        holder.price.text = price

        val colorId = if (free) R.color.md_red_600 else R.color.md_grey_600
        holder.price.setTextColor(ContextCompat.getColor(holder.price.context, colorId))

        holder.favoriteButton.setImageResource(if (favorite) R.drawable.ic_star else R.drawable.ic_star_border)
        holder.favoriteButton.visibility = if (favoriteButtonVisible) View.VISIBLE else View.GONE
        holder.favoriteButton.setOnClickListener { onFavoriteButtonClick() }

        holder.rootView.setOnClickListener { onClick() }
    }
}

class OfferItemHolder : KotlinHolder() {
    val rootView by bind<View>(R.id.offer_item_root)
    val active by bind<ImageView>(R.id.offer_item_active)
    val title by bind<TextView>(R.id.offer_item_title)
    val price by bind<TextView>(R.id.offer_item_price)
    val favoriteButton by bind<ImageView>(R.id.offer_item_favorite_button)
}