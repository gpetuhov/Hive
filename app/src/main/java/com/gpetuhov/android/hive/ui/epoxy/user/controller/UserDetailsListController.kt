package com.gpetuhov.android.hive.ui.epoxy.user.controller

import android.content.Context
import com.airbnb.epoxy.EpoxyController
import com.gpetuhov.android.hive.R
import com.gpetuhov.android.hive.application.HiveApp
import com.gpetuhov.android.hive.domain.model.User
import com.gpetuhov.android.hive.presentation.presenter.UserDetailsFragmentPresenter
import com.gpetuhov.android.hive.ui.epoxy.offer.item.models.offerItem
import com.gpetuhov.android.hive.ui.epoxy.user.models.userDetailsDescription
import com.gpetuhov.android.hive.ui.epoxy.user.models.userDetailsHeader
import com.gpetuhov.android.hive.ui.epoxy.user.models.userDetailsOfferHeader
import java.util.*
import javax.inject.Inject

class UserDetailsListController(private val presenter: UserDetailsFragmentPresenter) : EpoxyController() {

    @Inject lateinit var context: Context

    private var user: User? = null

    init {
        HiveApp.appComponent.inject(this)
    }

    override fun buildModels() {
        userDetailsHeader {
            id("user_details_header")
            onBackButtonClick { presenter.navigateUp() }
            userPicUrl(user?.userPicUrl ?: "")
            username(user?.getUsernameOrName() ?: "")
        }

        val hasDescription = user?.hasDescription ?: false
        if (hasDescription) {
            userDetailsDescription {
                id("user_details_description")
                description(user?.description ?: "")
            }
        }

        userDetailsOfferHeader {
            id("user_details_offer_header")

            val hasActiveOffer = user?.hasActiveOffer() ?: false
            offerHeader(if (hasActiveOffer) context.getString(R.string.offers) else context.getString(R.string.no_active_offer))
        }

        user?.offerList?.forEach {
            offerItem {
                id(it.uid)
                onClick { presenter.openOffer(it.uid) }
                title(it.title)
            }
        }
    }

    fun changeUser(user: User) {
        this.user = user
        requestModelBuild()
    }
}