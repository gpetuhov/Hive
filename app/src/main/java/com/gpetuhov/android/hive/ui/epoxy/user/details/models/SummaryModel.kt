package com.gpetuhov.android.hive.ui.epoxy.user.details.models

import android.widget.TextView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.gpetuhov.android.hive.R
import com.gpetuhov.android.hive.ui.epoxy.base.KotlinHolder
import com.gpetuhov.android.hive.util.setVisible

@EpoxyModelClass(layout = R.layout.user_details_summary_view)
abstract class SummaryModel : EpoxyModelWithHolder<SummaryHolder>() {

    @EpoxyAttribute lateinit var creationDate: String
    @EpoxyAttribute var creationDateVisible = false

    @EpoxyAttribute lateinit var firstOfferCreationDate: String
    @EpoxyAttribute var firstOfferCreationDateVisible = false

    @EpoxyAttribute lateinit var activeOffersCount: String
    @EpoxyAttribute lateinit var totalReviewsCount: String

    override fun bind(holder: SummaryHolder) {
        holder.creationDate.text = creationDate
        holder.creationDate.setVisible(creationDateVisible)

        holder.firstOfferCreationDate.text = firstOfferCreationDate
        holder.firstOfferCreationDate.setVisible(firstOfferCreationDateVisible)

        holder.activeOffersCount.text = activeOffersCount
        holder.totalReviewsCount.text = totalReviewsCount
    }
}

class SummaryHolder : KotlinHolder() {
    val creationDate by bind<TextView>(R.id.user_details_creation_date)
    val firstOfferCreationDate by bind<TextView>(R.id.user_details_first_offer_creation_date)
    val activeOffersCount by bind<TextView>(R.id.user_details_active_offers_count)
    val totalReviewsCount by bind<TextView>(R.id.user_details_total_reviews_count)
}