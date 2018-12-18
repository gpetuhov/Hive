package com.gpetuhov.android.hive.ui.epoxy.user.favorite.controller

import com.gpetuhov.android.hive.domain.model.User
import com.gpetuhov.android.hive.presentation.presenter.FavoriteUsersFragmentPresenter
import com.gpetuhov.android.hive.ui.epoxy.base.controller.BaseController
import com.gpetuhov.android.hive.ui.epoxy.user.item.model.userItem

class UserFavoriteListController(private val presenter: FavoriteUsersFragmentPresenter) : BaseController() {

    private var favoriteUsersList = mutableListOf<User>()

    override fun buildModels() {
        favoriteUsersList.forEach {
            userItem {
                id(it.uid)
                onClick { presenter.showUserDetails(it.uid) }
                username(it.getUsernameOrName())
            }
        }
    }

    fun changeFavoriteUsersList(favoriteUsersList: MutableList<User>) {
        this.favoriteUsersList = favoriteUsersList
        requestModelBuild()
    }
}