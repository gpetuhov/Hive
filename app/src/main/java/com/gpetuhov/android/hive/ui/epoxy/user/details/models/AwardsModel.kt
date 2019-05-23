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

    @EpoxyAttribute var textMasterVisible = false
    @EpoxyAttribute lateinit var onTextMasterClick: () -> Unit

    @EpoxyAttribute var textMasterTipVisible = false

    @EpoxyAttribute var offerProviderVisible = false
    @EpoxyAttribute lateinit var onOfferProviderClick: () -> Unit

    @EpoxyAttribute var offerProviderTipVisible = false

    @EpoxyAttribute var lineVisible = false

    override fun bind(holder: AwardsHolder) {
        holder.textMaster.setVisible(textMasterVisible)
        holder.textMaster.setOnClickListener { onTextMasterClick() }

        holder.textMasterTip.setVisible(textMasterTipVisible)
        holder.textMasterTip.setOnClickListener { onTextMasterClick() }

        holder.offerProvider.setVisible(offerProviderVisible)
        holder.offerProvider.setOnClickListener { onOfferProviderClick() }

        holder.offerProviderTip.setVisible(offerProviderTipVisible)
        holder.offerProviderTip.setOnClickListener { onOfferProviderClick() }

        holder.line.setVisible(lineVisible)
    }
}

class AwardsHolder : KotlinHolder() {
    val textMaster by bind<View>(R.id.user_details_awards_textmaster)
    val textMasterTip by bind<View>(R.id.user_details_awards_textmaster_tip)

    val offerProvider by bind<View>(R.id.user_details_awards_offerprovider)
    val offerProviderTip by bind<View>(R.id.user_details_awards_offerprovider_tip)

    val line by bind<View>(R.id.user_details_awards_line)
}