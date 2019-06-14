package com.gpetuhov.android.hive.ui.epoxy.sort.models

import android.widget.RadioButton
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.gpetuhov.android.hive.R
import com.gpetuhov.android.hive.ui.epoxy.base.KotlinHolder
import com.gpetuhov.android.hive.ui.epoxy.base.bind

@EpoxyModelClass(layout = R.layout.sort_type_view)
abstract class SortTypeModel : EpoxyModelWithHolder<SortTypeHolder>() {

    @EpoxyAttribute var offersFirst = false
    @EpoxyAttribute lateinit var onOffersFirstClick: () -> Unit

    @EpoxyAttribute var usersFirst = false
    @EpoxyAttribute lateinit var onUsersFirstClick: () -> Unit

    override fun bind(holder: SortTypeHolder) {
        holder.offersFirst.bind(offersFirst) { onOffersFirstClick() }
        holder.usersFirst.bind(usersFirst) { onUsersFirstClick() }
    }
}

class SortTypeHolder : KotlinHolder() {
    val offersFirst by bind<RadioButton>(R.id.sort_offers_first)
    val usersFirst by bind<RadioButton>(R.id.sort_users_first)
}