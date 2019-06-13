package com.gpetuhov.android.hive.ui.epoxy.filter.models

import android.widget.CheckBox
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.gpetuhov.android.hive.R
import com.gpetuhov.android.hive.ui.epoxy.base.KotlinHolder
import com.gpetuhov.android.hive.ui.epoxy.base.bind

@EpoxyModelClass(layout = R.layout.filter_offers_view)
abstract class FilterOffersModel : EpoxyModelWithHolder<FilterOffersHolder>() {

    @EpoxyAttribute var freeOffersOnly = false
    @EpoxyAttribute lateinit var onFreeOffersOnlyClick: (Boolean) -> Unit

    @EpoxyAttribute var offersWithReviewsOnly = false
    @EpoxyAttribute lateinit var onOffersWithReviewsClick: (Boolean) -> Unit

    override fun bind(holder: FilterOffersHolder) {
        holder.freeOffers.bind(freeOffersOnly) { onFreeOffersOnlyClick(it) }
        holder.offersWithReviews.bind(offersWithReviewsOnly) { onOffersWithReviewsClick(it) }
    }
}

class FilterOffersHolder : KotlinHolder() {
    val freeOffers by bind<CheckBox>(R.id.filter_free_offers)
    val offersWithReviews by bind<CheckBox>(R.id.filter_offers_with_reviews)
}