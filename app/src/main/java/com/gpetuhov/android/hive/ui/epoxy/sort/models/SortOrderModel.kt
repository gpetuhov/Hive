package com.gpetuhov.android.hive.ui.epoxy.sort.models

import android.widget.RadioButton
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.gpetuhov.android.hive.R
import com.gpetuhov.android.hive.ui.epoxy.base.KotlinHolder
import com.gpetuhov.android.hive.ui.epoxy.base.bind

@EpoxyModelClass(layout = R.layout.sort_order_view)
abstract class SortOrderModel : EpoxyModelWithHolder<SortOrderHolder>() {

    @EpoxyAttribute var ascending = false
    @EpoxyAttribute lateinit var onAscendingClick: () -> Unit

    @EpoxyAttribute var descending = false
    @EpoxyAttribute lateinit var onDescendingClick: () -> Unit

    override fun bind(holder: SortOrderHolder) {
        holder.ascending.bind(ascending) { onAscendingClick() }
        holder.descending.bind(descending) { onDescendingClick() }
    }
}

class SortOrderHolder : KotlinHolder() {
    val ascending by bind<RadioButton>(R.id.sort_order_ascending)
    val descending by bind<RadioButton>(R.id.sort_order_descending)
}