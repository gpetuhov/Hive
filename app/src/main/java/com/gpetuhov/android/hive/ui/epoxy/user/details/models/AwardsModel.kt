package com.gpetuhov.android.hive.ui.epoxy.user.details.models

import android.view.View
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.gpetuhov.android.hive.R
import com.gpetuhov.android.hive.ui.epoxy.base.KotlinHolder
import com.gpetuhov.android.hive.util.setVisible

@EpoxyModelClass(layout = R.layout.user_details_awards_view)
abstract class AwardsModel : EpoxyModelWithHolder<AwardsHolder>() {

    @EpoxyAttribute var fieldFillerVisible = false
    @EpoxyAttribute lateinit var onFieldFillerClick: () -> Unit

    @EpoxyAttribute var lineVisible = false

    override fun bind(holder: AwardsHolder) {
        holder.fieldFiller.setVisible(fieldFillerVisible)
        holder.fieldFiller.setOnClickListener { onFieldFillerClick() }

        holder.line.setVisible(lineVisible)
    }
}

class AwardsHolder : KotlinHolder() {
    val fieldFiller by bind<View>(R.id.user_details_awards_fieldfiller)

    val line by bind<View>(R.id.user_details_awards_line)
}