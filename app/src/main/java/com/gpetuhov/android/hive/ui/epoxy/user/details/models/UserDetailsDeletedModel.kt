package com.gpetuhov.android.hive.ui.epoxy.user.details.models

import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.gpetuhov.android.hive.R
import com.gpetuhov.android.hive.ui.epoxy.base.KotlinHolder

@EpoxyModelClass(layout = R.layout.user_details_deleted_view)
abstract class UserDetailsDeletedModel : EpoxyModelWithHolder<UserDetailsDeletedHolder>()

class UserDetailsDeletedHolder : KotlinHolder()