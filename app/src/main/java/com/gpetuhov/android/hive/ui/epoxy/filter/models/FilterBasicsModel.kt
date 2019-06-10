package com.gpetuhov.android.hive.ui.epoxy.filter.models

import android.widget.CheckBox
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.gpetuhov.android.hive.R
import com.gpetuhov.android.hive.ui.epoxy.base.KotlinHolder

@EpoxyModelClass(layout = R.layout.filter_basics_view)
abstract class FilterBasicsModel : EpoxyModelWithHolder<FilterBasicsHolder>() {

    @EpoxyAttribute var showUsers = false
    @EpoxyAttribute lateinit var onShowUsersClick: (Boolean) -> Unit

    @EpoxyAttribute var showOffers = false
    @EpoxyAttribute lateinit var onShowOffersClick: (Boolean) -> Unit

    @EpoxyAttribute var showOnline = false
    @EpoxyAttribute lateinit var onShowOnlineClick: (Boolean) -> Unit

    override fun bind(holder: FilterBasicsHolder) {
        holder.showUsers.isChecked = showUsers
        holder.showUsers.setOnClickListener { view -> onShowUsersClick((view as CheckBox).isChecked) }

        holder.showOffers.isChecked = showOffers
        holder.showOffers.setOnClickListener { view -> onShowOffersClick((view as CheckBox).isChecked) }

        holder.showOnline.isChecked = showOnline
        holder.showOnline.setOnClickListener { view -> onShowOnlineClick((view as CheckBox).isChecked) }
    }
}

class FilterBasicsHolder : KotlinHolder() {
    val showUsers by bind<CheckBox>(R.id.filter_show_users)
    val showOffers by bind<CheckBox>(R.id.filter_show_offers)
    val showOnline by bind<CheckBox>(R.id.filter_show_online)
}