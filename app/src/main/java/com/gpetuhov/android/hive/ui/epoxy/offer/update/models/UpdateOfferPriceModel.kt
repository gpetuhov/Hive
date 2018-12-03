package com.gpetuhov.android.hive.ui.epoxy.offer.update.models

import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.gpetuhov.android.hive.R
import com.gpetuhov.android.hive.ui.epoxy.holder.KotlinHolder

@EpoxyModelClass(layout = R.layout.update_offer_price_view)
abstract class UpdateOfferPriceModel : EpoxyModelWithHolder<UpdateOfferPriceHolder>() {

    @EpoxyAttribute var free = false
    @EpoxyAttribute lateinit var onFreeClick: (Boolean) -> Unit

    @EpoxyAttribute lateinit var price: String
    @EpoxyAttribute lateinit var onPriceClick: () -> Unit

    override fun bind(holder: UpdateOfferPriceHolder) {
        holder.free.isChecked = free
        holder.free.setOnClickListener { view -> onFreeClick((view as SwitchCompat).isChecked) }

        holder.priceWrapper.visibility = if (free) View.GONE else View.VISIBLE
        holder.priceWrapper.setOnClickListener { onPriceClick() }
        holder.price.text = price
    }
}

class UpdateOfferPriceHolder : KotlinHolder() {
    val free by bind<SwitchCompat>(R.id.update_offer_free)
    val priceWrapper by bind<View>(R.id.update_offer_price_wrapper)
    val price by bind<TextView>(R.id.update_offer_price)
}