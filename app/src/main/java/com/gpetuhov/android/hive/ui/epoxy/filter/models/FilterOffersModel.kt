package com.gpetuhov.android.hive.ui.epoxy.filter.models

import android.widget.CheckBox
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.gpetuhov.android.hive.R
import com.gpetuhov.android.hive.ui.epoxy.base.KotlinHolder

@EpoxyModelClass(layout = R.layout.filter_offers_view)
abstract class FilterOffersModel : EpoxyModelWithHolder<FilterOffersHolder>() {

    @EpoxyAttribute var freeOffersOnly = false
    @EpoxyAttribute lateinit var onFreeOffersOnlyClick: (Boolean) -> Unit

    @EpoxyAttribute var offersWithReviewsOnly = false
    @EpoxyAttribute lateinit var onOffersWithReviewsClick: (Boolean) -> Unit

    override fun bind(holder: FilterOffersHolder) {
        holder.freeOffers.isChecked = freeOffersOnly
        holder.freeOffers.setOnClickListener { view -> onFreeOffersOnlyClick((view as CheckBox).isChecked) }

        holder.offersWithReviews.isChecked = offersWithReviewsOnly
        holder.offersWithReviews.setOnClickListener { view -> onOffersWithReviewsClick((view as CheckBox).isChecked) }
    }
}

class FilterOffersHolder : KotlinHolder() {
    val freeOffers by bind<CheckBox>(R.id.filter_free_offers)
    val offersWithReviews by bind<CheckBox>(R.id.filter_offers_with_reviews)
}