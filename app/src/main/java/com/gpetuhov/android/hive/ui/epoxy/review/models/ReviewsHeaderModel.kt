package com.gpetuhov.android.hive.ui.epoxy.review.models

import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.gpetuhov.android.hive.R
import com.gpetuhov.android.hive.ui.epoxy.base.KotlinHolder

@EpoxyModelClass(layout = R.layout.reviews_header_view)
abstract class ReviewsHeaderModel : EpoxyModelWithHolder<ReviewsHeaderHolder>() {

    override fun bind(holder: ReviewsHeaderHolder) {
    }
}

class ReviewsHeaderHolder : KotlinHolder()