package com.gpetuhov.android.hive.ui.epoxy.user.details.models

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.gpetuhov.android.hive.R
import com.gpetuhov.android.hive.domain.model.Award
import com.gpetuhov.android.hive.ui.epoxy.base.KotlinHolder

@EpoxyModelClass(layout = R.layout.user_details_award_item_view)
abstract class AwardModel : EpoxyModelWithHolder<AwardHolder>() {

    @EpoxyAttribute lateinit var award: Award
    @EpoxyAttribute lateinit var onAwardClick: () -> Unit

    override fun bind(holder: AwardHolder) {
        holder.awardWrapper.setOnClickListener { onAwardClick() }
        holder.awardImage.setImageResource(award.smallImageId)
        holder.awardName.setText(award.nameId)
    }
}

class AwardHolder : KotlinHolder() {
    val awardWrapper by bind<View>(R.id.user_details_award_wrapper)
    val awardImage by bind<ImageView>(R.id.user_details_award_image)
    val awardName by bind<TextView>(R.id.user_details_award_name)
}