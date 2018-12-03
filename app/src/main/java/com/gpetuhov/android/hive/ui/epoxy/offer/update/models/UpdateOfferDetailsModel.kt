package com.gpetuhov.android.hive.ui.epoxy.offer.update.models

import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.gpetuhov.android.hive.R
import com.gpetuhov.android.hive.ui.epoxy.holder.KotlinHolder

@EpoxyModelClass(layout = R.layout.update_offer_details_view)
abstract class UpdateOfferDetailsModel : EpoxyModelWithHolder<UpdateOfferDetailsHolder>() {

    @EpoxyAttribute var active = false
    @EpoxyAttribute var activeEnabled = false
    @EpoxyAttribute lateinit var onActiveClick: (Boolean) -> Unit

    @EpoxyAttribute lateinit var title: String
    @EpoxyAttribute lateinit var onTitleClick: () -> Unit

    @EpoxyAttribute lateinit var description: String
    @EpoxyAttribute lateinit var onDescriptionClick: () -> Unit

    override fun bind(holder: UpdateOfferDetailsHolder) {
        holder.active.isChecked = active
        holder.active.isEnabled = activeEnabled
        holder.active.setOnClickListener { view -> onActiveClick((view as SwitchCompat).isChecked) }

        holder.title.text = title
        holder.title.setOnClickListener { onTitleClick() }

        holder.description.text = description
        holder.description.setOnClickListener { onDescriptionClick() }
    }
}

class UpdateOfferDetailsHolder : KotlinHolder() {
    val active by bind<SwitchCompat>(R.id.update_offer_active)
    val title by bind<TextView>(R.id.update_offer_title)
    val description by bind<TextView>(R.id.update_offer_description)
}