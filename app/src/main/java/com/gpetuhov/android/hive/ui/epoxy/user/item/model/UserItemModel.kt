package com.gpetuhov.android.hive.ui.epoxy.user.item.model

import android.widget.TextView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.gpetuhov.android.hive.R
import com.gpetuhov.android.hive.ui.epoxy.base.KotlinHolder

@EpoxyModelClass(layout = R.layout.user_item_view)
abstract class UserItemModel : EpoxyModelWithHolder<UserItemHolder>() {

    @EpoxyAttribute lateinit var username: String

    override fun bind(holder: UserItemHolder) {
        holder.username.text = username
    }
}

class UserItemHolder : KotlinHolder() {
    val username by bind<TextView>(R.id.user_item_username)
}