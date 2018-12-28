package com.gpetuhov.android.hive.ui.epoxy.user.details.models

import android.widget.TextView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.gpetuhov.android.hive.R
import com.gpetuhov.android.hive.ui.epoxy.base.KotlinHolder
import com.gpetuhov.android.hive.util.setVisible

@EpoxyModelClass(layout = R.layout.user_details_summary_view)
abstract class UserDetailsSummaryModel : EpoxyModelWithHolder<UserDetailsSummaryHolder>() {

    @EpoxyAttribute lateinit var creationDate: String
    @EpoxyAttribute var creationDateVisible = false

    @EpoxyAttribute lateinit var activeOffersCount: String
    @EpoxyAttribute lateinit var totalReviewsCount: String

    override fun bind(holder: UserDetailsSummaryHolder) {
        holder.creationDate.text = creationDate
        holder.creationDate.setVisible(creationDateVisible)

        holder.activeOffersCount.text = activeOffersCount
        holder.totalReviewsCount.text = totalReviewsCount
    }
}

class UserDetailsSummaryHolder : KotlinHolder() {
    val creationDate by bind<TextView>(R.id.user_details_creation_date)
    val activeOffersCount by bind<TextView>(R.id.user_details_active_offers_count)
    val totalReviewsCount by bind<TextView>(R.id.user_details_total_reviews_count)
}