package com.gpetuhov.android.hive.ui.epoxy.offer.details.models

import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.gpetuhov.android.hive.R
import com.gpetuhov.android.hive.ui.epoxy.base.KotlinHolder

@EpoxyModelClass(layout = R.layout.offer_details_reviews_header_view)
abstract class OfferDetailsReviewsHeaderModel : EpoxyModelWithHolder<OfferDetailsReviewsHeaderHolder>() {

    override fun bind(holder: OfferDetailsReviewsHeaderHolder) {
    }
}

class OfferDetailsReviewsHeaderHolder : KotlinHolder()