package com.gpetuhov.android.hive.ui.epoxy.offer.details.models

import android.widget.ImageButton
import android.widget.ImageView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.gpetuhov.android.hive.R
import com.gpetuhov.android.hive.ui.epoxy.base.KotlinHolder

@EpoxyModelClass(layout = R.layout.offer_details_header_view)
abstract class OfferDetailsHeaderModel : EpoxyModelWithHolder<OfferDetailsHeaderHolder>() {

    @EpoxyAttribute lateinit var onBackButtonClick: () -> Unit

    @EpoxyAttribute var favorite = false
    @EpoxyAttribute lateinit var onFavoriteButtonClick: () -> Unit

    override fun bind(holder: OfferDetailsHeaderHolder) {
        holder.backButton.setOnClickListener { onBackButtonClick() }

        holder.favoriteButton.setImageResource(if (favorite) R.drawable.ic_star else R.drawable.ic_star_border)
        holder.favoriteButton.setOnClickListener { onFavoriteButtonClick() }
    }
}

class OfferDetailsHeaderHolder : KotlinHolder() {
    val backButton by bind<ImageView>(R.id.offer_details_back_button)
    val favoriteButton by bind<ImageButton>(R.id.offer_details_favorite_button)
}