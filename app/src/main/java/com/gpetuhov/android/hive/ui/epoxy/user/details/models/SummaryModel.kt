package com.gpetuhov.android.hive.ui.epoxy.user.details.models

import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.AppCompatRatingBar
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
    @EpoxyAttribute lateinit var postedReviewsCount: String
    @EpoxyAttribute lateinit var postedFirstReviewsCount: String
    @EpoxyAttribute lateinit var totalReviewsCount: String

    @EpoxyAttribute var ratingVisible = false
    @EpoxyAttribute lateinit var ratingText: String
    @EpoxyAttribute var rating: Float = 0.0F

    @EpoxyAttribute lateinit var onReviewsClick: () -> Unit

    override fun bind(holder: SummaryHolder) {
        holder.creationDate.text = creationDate
        holder.creationDate.setVisible(creationDateVisible)

        holder.firstOfferCreationDate.text = firstOfferCreationDate
        holder.firstOfferCreationDate.setVisible(firstOfferCreationDateVisible)

        holder.activeOffersCount.text = activeOffersCount
        holder.postedReviewsCount.text = postedReviewsCount
        holder.postedFirstReviewsCount.text = postedFirstReviewsCount
        holder.totalReviewsCount.text = totalReviewsCount
        holder.totalReviewsCount.setOnClickListener { onReviewsClick() }

        holder.ratingWrapper.setVisible(ratingVisible)
        holder.ratingWrapper.setOnClickListener { onReviewsClick() }
        holder.ratingText.text = ratingText
        holder.ratingBar.rating = rating
    }
}

class SummaryHolder : KotlinHolder() {
    val creationDate by bind<TextView>(R.id.user_details_creation_date)
    val firstOfferCreationDate by bind<TextView>(R.id.user_details_first_offer_creation_date)
    val activeOffersCount by bind<TextView>(R.id.user_details_active_offers_count)
    val postedReviewsCount by bind<TextView>(R.id.user_details_posted_reviews_count)
    val postedFirstReviewsCount by bind<TextView>(R.id.user_details_posted_first_reviews_count)
    val totalReviewsCount by bind<TextView>(R.id.user_details_total_reviews_count)
    val ratingWrapper by bind<View>(R.id.user_details_average_rating_wrapper)
    val ratingText by bind<TextView>(R.id.user_details_average_rating_text)
    val ratingBar by bind<AppCompatRatingBar>(R.id.user_details_average_rating_bar)
}