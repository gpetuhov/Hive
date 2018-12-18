package com.gpetuhov.android.hive.ui.epoxy.user.details.models

import android.widget.ImageView
import android.widget.TextView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.gpetuhov.android.hive.R
import com.gpetuhov.android.hive.ui.epoxy.base.KotlinHolder
import com.gpetuhov.android.hive.util.updateUserPic

@EpoxyModelClass(layout = R.layout.user_details_name_view)
abstract class UserDetailsNameModel : EpoxyModelWithHolder<UserDetailsNameHolder>() {

    @EpoxyAttribute lateinit var userPicUrl: String
    @EpoxyAttribute lateinit var username: String

    override fun bind(holder: UserDetailsNameHolder) {
        updateUserPic(holder.userPic.context, userPicUrl, holder.userPic)
        holder.username.text = username
    }
}

class UserDetailsNameHolder : KotlinHolder() {
    val userPic by bind<ImageView>(R.id.user_details_user_pic)
    val username by bind<TextView>(R.id.user_details_username)
}