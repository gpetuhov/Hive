package com.gpetuhov.android.hive.ui.epoxy.profile.models

import android.widget.ImageView
import android.widget.TextView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.gpetuhov.android.hive.R
import com.gpetuhov.android.hive.ui.epoxy.base.KotlinHolder
import com.gpetuhov.android.hive.util.updateUserPic

@EpoxyModelClass(layout = R.layout.profile_username_view)
abstract class ProfileUsernameModel : EpoxyModelWithHolder<ProfileUsernameHolder>() {

    @EpoxyAttribute lateinit var username: String
    @EpoxyAttribute lateinit var onUsernameClick: () -> Unit

    @EpoxyAttribute lateinit var userPicUrl: String
    @EpoxyAttribute lateinit var onUserPicClick: () -> Unit

    @EpoxyAttribute lateinit var name: String
    @EpoxyAttribute lateinit var email: String

    override fun bind(holder: ProfileUsernameHolder) {
        holder.username.text = username
        holder.username.setOnClickListener { onUsernameClick() }

        updateUserPic(holder.userPic.context, userPicUrl, holder.userPic)
        holder.userPic.setOnClickListener { onUserPicClick() }

        holder.name.text = name
        holder.email.text = email
    }
}

class ProfileUsernameHolder : KotlinHolder() {
    val username by bind<TextView>(R.id.username_textview)
    val userPic by bind<ImageView>(R.id.user_pic)
    val name by bind<TextView>(R.id.user_name_textview)
    val email by bind<TextView>(R.id.user_email_textview)
}