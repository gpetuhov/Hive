package com.gpetuhov.android.hive.ui.epoxy.user.details.models

import android.widget.ImageButton
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.gpetuhov.android.hive.R
import com.gpetuhov.android.hive.ui.epoxy.base.KotlinHolder

@EpoxyModelClass(layout = R.layout.user_details_header_view)
abstract class UserDetailsHeaderModel : EpoxyModelWithHolder<UserDetailsHeaderHolder>() {

    @EpoxyAttribute lateinit var onBackButtonClick: () -> Unit

    @EpoxyAttribute var favorite = false
    @EpoxyAttribute lateinit var onFavoriteButtonClick: () -> Unit

    override fun bind(holder: UserDetailsHeaderHolder) {
        holder.backButton.setOnClickListener { onBackButtonClick() }

        holder.favoriteButton.setImageResource(if (favorite) R.drawable.ic_star else R.drawable.ic_star_border)
        holder.favoriteButton.setOnClickListener { onFavoriteButtonClick() }
    }
}

class UserDetailsHeaderHolder : KotlinHolder() {
    val backButton by bind<ImageButton>(R.id.user_details_back_button)
    val favoriteButton by bind<ImageButton>(R.id.user_details_favorite_button)
}