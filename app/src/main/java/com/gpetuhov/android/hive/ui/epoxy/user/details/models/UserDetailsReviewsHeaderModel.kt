package com.gpetuhov.android.hive.ui.epoxy.user.details.models

import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.gpetuhov.android.hive.R
import com.gpetuhov.android.hive.ui.epoxy.base.KotlinHolder

@EpoxyModelClass(layout = R.layout.user_details_reviews_header_view)
abstract class UserDetailsReviewsHeaderModel : EpoxyModelWithHolder<UserDetailsReviewsHeaderHolder>() {

    override fun bind(holder: UserDetailsReviewsHeaderHolder) {
    }
}

class UserDetailsReviewsHeaderHolder : KotlinHolder()