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

    override fun bind(holder: AddPhotoHolder) {
        holder.addPhotoButton.setOnClickListener { onClick() }
        holder.addPhotoTextView.setOnClickListener { onClick() }
    }
}

class AddPhotoHolder : KotlinHolder() {
    val addPhotoButton by bind<View>(R.id.profile_add_photo_button)
    val addPhotoTextView by bind<View>(R.id.profile_add_photo_textview)
}