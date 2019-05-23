package com.gpetuhov.android.hive.ui.epoxy.profile.models

import android.view.View
import android.widget.TextView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.gpetuhov.android.hive.R
import com.gpetuhov.android.hive.domain.model.Award
import com.gpetuhov.android.hive.ui.epoxy.base.KotlinHolder

@EpoxyModelClass(layout = R.layout.profile_award_tip_view)
abstract class AwardTipModel : EpoxyModelWithHolder<AwardTipHolder>() {

    @EpoxyAttribute lateinit var award: Award
    @EpoxyAttribute lateinit var onAwardTipClick: () -> Unit

    override fun bind(holder: AwardTipHolder) {
        holder.awardTipWrapper.setOnClickListener { onAwardTipClick() }
        holder.awardTipText.setText(award.tipId)
    }
}

class AwardTipHolder : KotlinHolder() {
    val awardTipWrapper by bind<View>(R.id.profile_award_tip_wrapper)
    val awardTipText by bind<TextView>(R.id.profile_award_tip_text)
}