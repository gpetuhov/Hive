package com.gpetuhov.android.hive.ui.epoxy.offer.update.models

import android.view.View
import android.widget.ImageView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.gpetuhov.android.hive.R
import com.gpetuhov.android.hive.ui.epoxy.base.KotlinHolder

@EpoxyModelClass(layout = R.layout.update_offer_header_view)
abstract class UpdateOfferHeaderModel : EpoxyModelWithHolder<UpdateOfferHeaderHolder>() {

    @EpoxyAttribute lateinit var onBackButtonClick: () -> Unit

    @EpoxyAttribute var deleteButtonVisible = false
    @EpoxyAttribute var deleteButtonEnabled = true
    @EpoxyAttribute lateinit var onDeleteButtonClick: () -> Unit

    @EpoxyAttribute var saveButtonEnabled = true
    @EpoxyAttribute lateinit var onSaveButtonClick: () -> Unit

    override fun bind(holder: UpdateOfferHeaderHolder) {
        holder.backButton.setOnClickListener { onBackButtonClick() }

        holder.deleteButton.visibility = if (deleteButtonVisible) View.VISIBLE else View.GONE
        holder.deleteButton.isEnabled = deleteButtonEnabled
        holder.deleteButton.setOnClickListener { onDeleteButtonClick() }

        holder.saveButton.isEnabled = saveButtonEnabled
        holder.saveButton.setOnClickListener { onSaveButtonClick() }
    }
}

class UpdateOfferHeaderHolder : KotlinHolder() {
    val backButton by bind<ImageView>(R.id.update_offer_back_button)
    val deleteButton by bind<ImageView>(R.id.update_offer_delete_button)
    val saveButton by bind<ImageView>(R.id.update_offer_save_button)
}