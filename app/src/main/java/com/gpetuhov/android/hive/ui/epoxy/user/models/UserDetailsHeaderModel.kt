package com.gpetuhov.android.hive.ui.epoxy.user.models

import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.gpetuhov.android.hive.R
import com.gpetuhov.android.hive.ui.epoxy.holder.KotlinHolder
import com.gpetuhov.android.hive.util.updateUserPic

@EpoxyModelClass(layout = R.layout.user_details_header_view)
abstract class UserDetailsHeaderModel : EpoxyModelWithHolder<UserDetailsHeaderHolder>() {

    @EpoxyAttribute lateinit var onBackButtonClick: () -> Unit
    @EpoxyAttribute lateinit var userPicUrl: String
    @EpoxyAttribute lateinit var username: String

    override fun bind(holder: UserDetailsHeaderHolder) {
        holder.backButton.setOnClickListener { onBackButtonClick() }
        updateUserPic(holder.userPic.context, userPicUrl, holder.userPic)
        holder.username.text = username
    }
}

class UserDetailsHeaderHolder : KotlinHolder() {
    val backButton by bind<ImageButton>(R.id.user_details_back_button)
    val userPic by bind<ImageView>(R.id.user_details_user_pic)
    val username by bind<TextView>(R.id.user_details_username)
}