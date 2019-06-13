package com.gpetuhov.android.hive.ui.epoxy.filter.models

import android.widget.RadioButton
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.gpetuhov.android.hive.R
import com.gpetuhov.android.hive.ui.epoxy.base.KotlinHolder
import com.gpetuhov.android.hive.ui.epoxy.base.bind

@EpoxyModelClass(layout = R.layout.filter_basics_view)
abstract class FilterBasicsModel : EpoxyModelWithHolder<FilterBasicsHolder>() {

    @EpoxyAttribute var showUsersOffersAll = false
    @EpoxyAttribute lateinit var onShowUsersOffersAllClick: () -> Unit

    @EpoxyAttribute var showUsersOnly = false
    @EpoxyAttribute lateinit var onShowUsersOnlyClick: () -> Unit

    @EpoxyAttribute var showOffersOnly = false
    @EpoxyAttribute lateinit var onShowOffersOnlyClick: () -> Unit

    override fun bind(holder: FilterBasicsHolder) {
        holder.showUsersOffersAll.bind(showUsersOffersAll) { onShowUsersOffersAllClick() }
        holder.showUsersOnly.bind(showUsersOnly) { onShowUsersOnlyClick() }
        holder.showOffersOnly.bind(showOffersOnly) { onShowOffersOnlyClick() }
    }
}

class FilterBasicsHolder : KotlinHolder() {
    val showUsersOffersAll by bind<RadioButton>(R.id.filter_show_users_offers_all)
    val showUsersOnly by bind<RadioButton>(R.id.filter_show_users)
    val showOffersOnly by bind<RadioButton>(R.id.filter_show_offers)
}