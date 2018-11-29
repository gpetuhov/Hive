package com.gpetuhov.android.hive.ui.epoxy.user.models

import android.widget.ImageView
import android.widget.TextView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.gpetuhov.android.hive.R
import com.gpetuhov.android.hive.ui.epoxy.holder.KotlinHolder
import com.gpetuhov.android.hive.util.updateUserPic

@EpoxyModelClass(layout = R.layout.user_details_details_view)
abstract class UserDetailsModel : EpoxyModelWithHolder<UserDetailsHolder>() {

    @EpoxyAttribute lateinit var userPicUrl: String
    @EpoxyAttribute lateinit var username: String
    @EpoxyAttribute lateinit var description: String

    override fun bind(holder: UserDetailsHolder) {
        updateUserPic(holder.userPic.context, userPicUrl, holder.userPic)
        holder.username.text = username
        holder.description.text = description
    }
}

class UserDetailsHolder : KotlinHolder() {
    val userPic by bind<ImageView>(R.id.user_details_user_pic)
    val username by bind<TextView>(R.id.user_details_username)
    val description by bind<TextView>(R.id.user_details_description)
}