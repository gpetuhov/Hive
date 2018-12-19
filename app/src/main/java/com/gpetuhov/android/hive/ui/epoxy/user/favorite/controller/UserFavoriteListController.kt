package com.gpetuhov.android.hive.ui.epoxy.user.favorite.controller

import android.content.Context
import com.airbnb.epoxy.Carousel
import com.gpetuhov.android.hive.R
import com.gpetuhov.android.hive.application.HiveApp
import com.gpetuhov.android.hive.domain.model.User
import com.gpetuhov.android.hive.presentation.presenter.FavoriteUsersFragmentPresenter
import com.gpetuhov.android.hive.ui.epoxy.base.carousel
import com.gpetuhov.android.hive.ui.epoxy.base.controller.BaseController
import com.gpetuhov.android.hive.ui.epoxy.base.withModelsFrom
import com.gpetuhov.android.hive.ui.epoxy.offer.item.models.OfferItemOnePhotoModel_
import com.gpetuhov.android.hive.ui.epoxy.user.item.model.userItem
import com.gpetuhov.android.hive.util.Settings
import javax.inject.Inject

class UserFavoriteListController(private val presenter: FavoriteUsersFragmentPresenter) : BaseController() {

    @Inject lateinit var context: Context
    @Inject lateinit var settings: Settings

    private var favoriteUsersList = mutableListOf<User>()

    init {
        HiveApp.appComponent.inject(this)
    }

    override fun buildModels() {
        favoriteUsersList.forEach { user ->
            userItem {
                id(user.uid)
                userPicUrl(user.userPicUrl)
                onClick { presenter.showUserDetails(user.uid) }
                username(user.getUsernameOrName())
                onFavoriteButtonClick { presenter.removeUserFromFavorite(user.uid) }
            }

            val activeOfferList = user.offerList.filter { offer -> offer.isActive }

            if (!activeOfferList.isEmpty()) {
                carousel {
                    id("${user.uid}_offer_carousel")

                    withModelsFrom(activeOfferList) { index, offer ->
                        OfferItemOnePhotoModel_()
                            .id(offer.uid)
                            .photoUrl(offer.photoList.firstOrNull()?.downloadUrl ?: "")
                            .title(offer.title)
                            .free(offer.isFree)
                            .price(if (offer.isFree) context.getString(R.string.free_caps) else "${offer.price} USD")
                            .onClick { presenter.showOfferDetails(user.uid, offer.uid) }
                    }
                }
            }
        }
    }

    fun changeFavoriteUsersList(favoriteUsersList: MutableList<User>) {
        this.favoriteUsersList = favoriteUsersList
        requestModelBuild()
    }
}