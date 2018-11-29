package com.gpetuhov.android.hive.ui.epoxy.user.controller

import com.airbnb.epoxy.EpoxyController
import com.gpetuhov.android.hive.domain.model.User
import com.gpetuhov.android.hive.presentation.presenter.UserDetailsFragmentPresenter
import com.gpetuhov.android.hive.ui.epoxy.user.models.userDetails

class UserDetailsListController(private val presenter: UserDetailsFragmentPresenter) : EpoxyController() {

    private var user: User? = null

    override fun buildModels() {
        userDetails {
            id("user_details")
            onBackButtonClick { presenter.navigateUp() }
            userPicUrl(user?.userPicUrl ?: "")
            username(user?.getUsernameOrName() ?: "")
            description(user?.description ?: "")
        }
    }

    fun changeUser(user: User) {
        this.user = user
        requestModelBuild()
    }
}