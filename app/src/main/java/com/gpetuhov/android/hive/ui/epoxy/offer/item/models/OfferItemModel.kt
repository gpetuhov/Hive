package com.gpetuhov.android.hive.ui.epoxy.offer.item.models

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.gpetuhov.android.hive.R
import com.gpetuhov.android.hive.ui.epoxy.holder.KotlinHolder

@EpoxyModelClass(layout = R.layout.offer_item_view)
abstract class OfferItemModel : EpoxyModelWithHolder<OfferItemHolder>() {

    @EpoxyAttribute var active = false
    @EpoxyAttribute var activeVisible = false

    @EpoxyAttribute lateinit var title: String
    @EpoxyAttribute lateinit var price: String
    @EpoxyAttribute lateinit var onClick: () -> Unit

    override fun bind(holder: OfferItemHolder) {
        holder.active.setImageResource(if (active) R.drawable.circle_green else R.drawable.circle_red)
        holder.active.visibility = if (activeVisible) View.VISIBLE else View.GONE

        holder.title.text = title
        holder.price.text = price
        holder.rootView.setOnClickListener { onClick() }
    }
}

class OfferItemHolder : KotlinHolder() {
    val rootView by bind<View>(R.id.offer_item_root)
    val active by bind<ImageView>(R.id.offer_item_active)
    val title by bind<TextView>(R.id.offer_item_title)
    val price by bind<TextView>(R.id.offer_item_price)
}