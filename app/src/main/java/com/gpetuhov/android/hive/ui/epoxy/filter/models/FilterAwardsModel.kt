package com.gpetuhov.android.hive.ui.epoxy.filter.models

import android.widget.CheckBox
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.gpetuhov.android.hive.R
import com.gpetuhov.android.hive.ui.epoxy.base.KotlinHolder
import com.gpetuhov.android.hive.ui.epoxy.base.bind

@EpoxyModelClass(layout = R.layout.filter_awards_view)
abstract class FilterAwardsModel : EpoxyModelWithHolder<FilterAwardsHolder>() {

    @EpoxyAttribute var hasTextMaster = false
    @EpoxyAttribute lateinit var onHasTextMasterClick: (Boolean) -> Unit

    override fun bind(holder: FilterAwardsHolder) {
        holder.hasTextMaster.bind(hasTextMaster) { onHasTextMasterClick(it) }
    }
}

class FilterAwardsHolder : KotlinHolder() {
    val hasTextMaster by bind<CheckBox>(R.id.filter_has_textmaster)
}