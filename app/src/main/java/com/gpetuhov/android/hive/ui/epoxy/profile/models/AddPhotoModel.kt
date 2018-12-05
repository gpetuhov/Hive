package com.gpetuhov.android.hive.ui.epoxy.profile.models

import android.view.View
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.gpetuhov.android.hive.R
import com.gpetuhov.android.hive.ui.epoxy.holder.KotlinHolder

@EpoxyModelClass(layout = R.layout.profile_add_photo_view)
abstract class AddPhotoModel : EpoxyModelWithHolder<AddPhotoHolder>() {

    @EpoxyAttribute lateinit var onClick: () -> Unit
    @EpoxyAttribute var maxPhotoWarningVisible = false

    override fun bind(holder: AddPhotoHolder) {
        holder.addPhotoButton.setOnClickListener { onClick() }
        holder.addPhotoTextView.setOnClickListener { onClick() }
        holder.maxPhotoWarning.visibility = if (maxPhotoWarningVisible) View.VISIBLE else View.GONE
    }
}

class AddPhotoHolder : KotlinHolder() {
    val addPhotoButton by bind<View>(R.id.profile_add_photo_button)
    val addPhotoTextView by bind<View>(R.id.profile_add_photo_textview)
    val maxPhotoWarning by bind<View>(R.id.profile_add_photo_warning)
}