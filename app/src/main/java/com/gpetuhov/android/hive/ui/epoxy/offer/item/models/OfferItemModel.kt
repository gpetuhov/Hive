package com.gpetuhov.android.hive.ui.epoxy.offer.item.models

import android.view.View
import android.widget.TextView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.gpetuhov.android.hive.R
import com.gpetuhov.android.hive.ui.epoxy.holder.KotlinHolder

@EpoxyModelClass(layout = R.layout.offer_item_view)
abstract class OfferItemModel : EpoxyModelWithHolder<OfferItemHolder>() {

    @EpoxyAttribute lateinit var onClick: () -> Unit
    @EpoxyAttribute lateinit var title: String

    override fun bind(holder: OfferItemHolder) {
        holder.rootView.setOnClickListener { onClick() }
        holder.title.text = title
    }
}

class OfferItemHolder : KotlinHolder() {
    val rootView by bind<View>(R.id.offer_item_root)
    val title by bind<TextView>(R.id.offer_item_title)
}