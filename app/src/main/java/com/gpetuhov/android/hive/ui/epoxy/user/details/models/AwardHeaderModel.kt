package com.gpetuhov.android.hive.ui.epoxy.user.details.models

import android.view.View
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.gpetuhov.android.hive.R
import com.gpetuhov.android.hive.ui.epoxy.base.KotlinHolder
import com.gpetuhov.android.hive.util.setVisible

@EpoxyModelClass(layout = R.layout.user_details_awards_header_view)
abstract class AwardHeaderModel : EpoxyModelWithHolder<AwardHeaderHolder>() {

    @EpoxyAttribute var lineVisible = false

    override fun bind(holder: AwardHeaderHolder) = holder.line.setVisible(lineVisible)
}

class AwardHeaderHolder : KotlinHolder() {
    val line by bind<View>(R.id.user_details_awards_line)
}