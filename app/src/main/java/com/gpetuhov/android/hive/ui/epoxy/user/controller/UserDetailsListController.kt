package com.gpetuhov.android.hive.ui.epoxy.user.controller

import com.airbnb.epoxy.EpoxyController
import com.gpetuhov.android.hive.domain.model.User
import com.gpetuhov.android.hive.presentation.presenter.UserDetailsFragmentPresenter
import com.gpetuhov.android.hive.ui.epoxy.user.models.userDetailsDescription
import com.gpetuhov.android.hive.ui.epoxy.user.models.userDetailsHeader
import com.gpetuhov.android.hive.ui.epoxy.user.models.userDetailsOfferHeader

class UserDetailsListController(private val presenter: UserDetailsFragmentPresenter) : EpoxyController() {

    private var user: User? = null

    override fun buildModels() {
        userDetailsHeader {
            id("user_details_header")
            onBackButtonClick { presenter.navigateUp() }
            userPicUrl(user?.userPicUrl ?: "")
            username(user?.getUsernameOrName() ?: "")
        }

        userDetailsDescription {
            id("user_details_description")
            description(user?.description ?: "")
        }

        userDetailsOfferHeader {
            id("user_details_offer_header")
        }
    }

    fun changeUser(user: User) {
        this.user = user
        requestModelBuild()
    }
}