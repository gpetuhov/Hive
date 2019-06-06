package com.gpetuhov.android.hive.ui.epoxy.search.models

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.gpetuhov.android.hive.R
import com.gpetuhov.android.hive.ui.epoxy.base.KotlinHolder
import com.gpetuhov.android.hive.util.updateUserPic

@EpoxyModelClass(layout = R.layout.user_item_no_offers_view)
abstract class UserItemNoOfferModel : EpoxyModelWithHolder<UserItemNoOfferHolder>() {

    @EpoxyAttribute lateinit var onClick: () -> Unit
    @EpoxyAttribute lateinit var userPicUrl: String
    @EpoxyAttribute lateinit var username: String
    @EpoxyAttribute lateinit var userStarCount: String
    @EpoxyAttribute lateinit var onFavoriteButtonClick: () -> Unit

    override fun bind(holder: UserItemNoOfferHolder) {
        holder.rootView.setOnClickListener { onClick() }
        updateUserPic(holder.userPic.context, userPicUrl, holder.userPic)
        holder.username.text = username
        holder.userStarCount.text = userStarCount
        holder.favoriteButton.setOnClickListener { onFavoriteButtonClick() }
    }
}

class UserItemNoOfferHolder : KotlinHolder() {
    val rootView by bind<View>(R.id.user_item_no_offer_root)
    val userPic by bind<ImageView>(R.id.user_item_no_offer_user_pic)
    val username by bind<TextView>(R.id.user_item_no_offer_name)
    val favoriteButton by bind<ImageView>(R.id.user_item_no_offer_favorite_button)
    val userStarCount by bind<TextView>(R.id.user_item_no_offer_star_count)
}