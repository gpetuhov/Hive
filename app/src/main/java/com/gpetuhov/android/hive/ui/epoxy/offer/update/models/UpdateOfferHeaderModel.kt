package com.gpetuhov.android.hive.ui.epoxy.offer.update.models

import android.widget.ImageView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.gpetuhov.android.hive.R
import com.gpetuhov.android.hive.ui.epoxy.holder.KotlinHolder

@EpoxyModelClass(layout = R.layout.update_offer_header_view)
abstract class UpdateOfferHeaderModel : EpoxyModelWithHolder<UpdateOfferHeaderHolder>() {

    @EpoxyAttribute lateinit var onBackButtonClick: () -> Unit
    @EpoxyAttribute lateinit var onDeleteButtonClick: () -> Unit
    @EpoxyAttribute lateinit var onSaveButtonClick: () -> Unit

    override fun bind(holder: UpdateOfferHeaderHolder) {
        holder.backButton.setOnClickListener { onBackButtonClick() }
        holder.deleteButton.setOnClickListener { onDeleteButtonClick }
        holder.saveButton.setOnClickListener { onSaveButtonClick }
    }
}

class UpdateOfferHeaderHolder : KotlinHolder() {
    val backButton by bind<ImageView>(R.id.update_offer_back_button)
    val deleteButton by bind<ImageView>(R.id.update_offer_delete_button)
    val saveButton by bind<ImageView>(R.id.update_offer_save_button)
}