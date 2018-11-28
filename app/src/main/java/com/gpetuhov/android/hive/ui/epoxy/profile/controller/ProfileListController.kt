package com.gpetuhov.android.hive.ui.epoxy.profile.controller

import com.airbnb.epoxy.EpoxyController
import com.gpetuhov.android.hive.domain.model.User
import com.gpetuhov.android.hive.presentation.presenter.ProfileFragmentPresenter
import com.gpetuhov.android.hive.ui.epoxy.profile.models.details

class ProfileListController(private val presenter: ProfileFragmentPresenter) : EpoxyController() {

    private var user: User? = null

    override fun buildModels() {
        details {
            id("details")

            username(user?.username ?: "")
            onUsernameClick { presenter.showUsernameDialog() }

            userPicUrl(user?.userPicUrl ?: "")

            name(user?.name ?: "")
            email(user?.email ?: "")
        }
    }

    fun changeUser(user: User) {
        this.user = user
        requestModelBuild()
    }
}