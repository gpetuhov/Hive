package com.gpetuhov.android.hive.ui.epoxy.review.models

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatRatingBar
import androidx.core.view.marginBottom
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

    @EpoxyAttribute var controlsVisible = false
    @EpoxyAttribute lateinit var onEditClick: () -> Unit
    @EpoxyAttribute lateinit var onDeleteClick: () -> Unit

    override fun bind(holder: ReviewItemHolder) {
        updateUserPic(holder.userPic.context, userPicUrl, holder.userPic)
        holder.username.text = username
        holder.reviewText.text = reviewText
        holder.time.text = time

        holder.ratingBar.rating = rating
        holder.ratingBar.setVisible(ratingVisible)

        holder.separator.setVisible(!controlsVisible && ratingVisible)

        holder.controlsWrapper.setVisible(controlsVisible)
        holder.edit.setOnClickListener { onEditClick() }
        holder.delete.setOnClickListener { onDeleteClick() }
    }
}

class ReviewItemHolder : KotlinHolder() {
    val userPic by bind<ImageView>(R.id.review_item_user_pic)
    val username by bind<TextView>(R.id.review_item_user_name)
    val time by bind<TextView>(R.id.review_item_time)
    val reviewText by bind<TextView>(R.id.review_item_text)
    val ratingBar by bind<AppCompatRatingBar>(R.id.review_item_rating_bar)
    val separator by bind<View>(R.id.review_item_separator)
    val controlsWrapper by bind<View>(R.id.review_item_controls_wrapper)
    val edit by bind<TextView>(R.id.review_item_edit)
    val delete by bind<TextView>(R.id.review_item_delete)
}