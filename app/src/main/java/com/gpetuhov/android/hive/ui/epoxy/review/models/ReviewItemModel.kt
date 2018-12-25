package com.gpetuhov.android.hive.ui.epoxy.review.models

import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.gpetuhov.android.hive.R
import com.gpetuhov.android.hive.ui.epoxy.base.KotlinHolder
import com.gpetuhov.android.hive.util.setVisible
import com.gpetuhov.android.hive.util.updateUserPic

@EpoxyModelClass(layout = R.layout.review_item_view)
abstract class ReviewItemModel : EpoxyModelWithHolder<ReviewItemHolder>() {

    @EpoxyAttribute lateinit var userPicUrl: String
    @EpoxyAttribute lateinit var username: String

    @EpoxyAttribute lateinit var time: String

    @EpoxyAttribute lateinit var reviewText: String

    @EpoxyAttribute var rating = 0.0F
    @EpoxyAttribute var ratingVisible = true

    override fun bind(holder: ReviewItemHolder) {
        updateUserPic(holder.userPic.context, userPicUrl, holder.userPic)
        holder.username.text = username
        holder.reviewText.text = reviewText
        holder.time.text = time

        holder.ratingBar.rating = rating
        holder.ratingBar.setVisible(ratingVisible)
    }
}

class ReviewItemHolder : KotlinHolder() {
    val userPic by bind<ImageView>(R.id.review_item_user_pic)
    val username by bind<TextView>(R.id.review_item_user_name)
    val time by bind<TextView>(R.id.review_item_time)
    val reviewText by bind<TextView>(R.id.review_item_text)
    val ratingBar by bind<RatingBar>(R.id.review_item_rating_bar)
}