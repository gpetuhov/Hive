package com.gpetuhov.android.hive.ui.epoxy.offer.details.models

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.gpetuhov.android.hive.R
import com.gpetuhov.android.hive.ui.epoxy.base.KotlinHolder
import com.gpetuhov.android.hive.util.updateUserPic

@EpoxyModelClass(layout = R.layout.offer_details_user_view)
abstract class OfferDetailsUserModel : EpoxyModelWithHolder<OfferDetailsUserHolder>() {

    @EpoxyAttribute lateinit var onClick: () -> Unit
    @EpoxyAttribute lateinit var userPicUrl: String
    @EpoxyAttribute lateinit var username: String

    override fun bind(holder: OfferDetailsUserHolder) {
        holder.rootView.setOnClickListener { onClick() }
        updateUserPic(holder.userPic.context, userPicUrl, holder.userPic)
        holder.username.text = username
    }
}

class OfferDetailsUserHolder : KotlinHolder() {
    val rootView by bind<View>(R.id.offer_details_user_root_view)
    val userPic by bind<ImageView>(R.id.offer_details_user_pic)
    val username by bind<TextView>(R.id.offer_details_username)
}