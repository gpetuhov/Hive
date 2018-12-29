package com.gpetuhov.android.hive.ui.epoxy.profile.models

import android.widget.ImageView
import android.widget.TextView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.gpetuhov.android.hive.R
import com.gpetuhov.android.hive.ui.epoxy.base.KotlinHolder
import com.gpetuhov.android.hive.util.setVisible
import com.gpetuhov.android.hive.util.updateUserPic

@EpoxyModelClass(layout = R.layout.profile_details_view)
abstract class DetailsModel : EpoxyModelWithHolder<DetailsHolder>() {

    @EpoxyAttribute lateinit var username: String
    @EpoxyAttribute lateinit var onUsernameClick: () -> Unit

    @EpoxyAttribute lateinit var userPicUrl: String
    @EpoxyAttribute lateinit var onUserPicClick: () -> Unit

    @EpoxyAttribute lateinit var name: String
    @EpoxyAttribute lateinit var email: String

    @EpoxyAttribute lateinit var creationDate: String
    @EpoxyAttribute var creationDateVisible = false

    @EpoxyAttribute lateinit var firstOfferCreationDate: String
    @EpoxyAttribute var firstOfferCreationDateVisible = false

    @EpoxyAttribute lateinit var activeOffersCount: String
    @EpoxyAttribute lateinit var totalReviewsCount: String

    @EpoxyAttribute lateinit var description: String
    @EpoxyAttribute lateinit var onDescriptionClick: () -> Unit

    @EpoxyAttribute var noActiveOffersWarningVisible = false

    override fun bind(holder: DetailsHolder) {
        holder.username.text = username
        holder.username.setOnClickListener { onUsernameClick() }

        updateUserPic(holder.userPic.context, userPicUrl, holder.userPic)
        holder.userPic.setOnClickListener { onUserPicClick() }

        holder.name.text = name
        holder.email.text = email

        holder.creationDate.text = creationDate
        holder.creationDate.setVisible(creationDateVisible)

        holder.firstOfferCreationDate.text = firstOfferCreationDate
        holder.firstOfferCreationDate.setVisible(firstOfferCreationDateVisible)

        holder.activeOffersCount.text = activeOffersCount
        holder.totalReviewsCount.text = totalReviewsCount

        holder.description.text = description
        holder.description.setOnClickListener { onDescriptionClick() }

        holder.noActiveOffersWarning.setVisible(noActiveOffersWarningVisible)
    }
}

class DetailsHolder : KotlinHolder() {
    val username by bind<TextView>(R.id.username_textview)
    val userPic by bind<ImageView>(R.id.user_pic)
    val name by bind<TextView>(R.id.user_name_textview)
    val email by bind<TextView>(R.id.user_email_textview)
    val creationDate by bind<TextView>(R.id.user_creation_date)
    val firstOfferCreationDate by bind<TextView>(R.id.user_first_offer_creation_date)
    val activeOffersCount by bind<TextView>(R.id.user_active_offers_count)
    val totalReviewsCount by bind<TextView>(R.id.user_total_reviews_count)
    val description by bind<TextView>(R.id.user_description_textview)
    val noActiveOffersWarning by bind<TextView>(R.id.user_no_active_offers_warning)
}