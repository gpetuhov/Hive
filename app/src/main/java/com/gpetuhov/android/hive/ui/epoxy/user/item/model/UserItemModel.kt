package com.gpetuhov.android.hive.ui.epoxy.user.item.model

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.gpetuhov.android.hive.R
import com.gpetuhov.android.hive.ui.epoxy.base.KotlinHolder
import com.gpetuhov.android.hive.util.updateUserPic

@EpoxyModelClass(layout = R.layout.user_item_view)
abstract class UserItemModel : EpoxyModelWithHolder<UserItemHolder>() {

    @EpoxyAttribute lateinit var onClick: () -> Unit
    @EpoxyAttribute lateinit var userPicUrl: String
    @EpoxyAttribute lateinit var username: String
    @EpoxyAttribute lateinit var onFavoriteButtonClick: () -> Unit

    override fun bind(holder: UserItemHolder) {
        holder.rootView.setOnClickListener { onClick() }
        updateUserPic(holder.userPic.context, userPicUrl, holder.userPic)
        holder.username.text = username
        holder.favoriteButton.setOnClickListener { onFavoriteButtonClick() }
    }
}

class UserItemHolder : KotlinHolder() {
    val rootView by bind<View>(R.id.user_item_root)
    val userPic by bind<ImageView>(R.id.user_item_user_pic)
    val username by bind<TextView>(R.id.user_item_username)
    val favoriteButton by bind<ImageView>(R.id.user_item_favorite_button)
}