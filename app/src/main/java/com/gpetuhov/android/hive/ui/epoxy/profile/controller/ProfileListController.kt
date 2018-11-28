package com.gpetuhov.android.hive.ui.epoxy.profile.controller

import android.content.Context
import com.airbnb.epoxy.EpoxyController
import com.gpetuhov.android.hive.R
import com.gpetuhov.android.hive.application.HiveApp
import com.gpetuhov.android.hive.domain.model.User
import com.gpetuhov.android.hive.presentation.presenter.ProfileFragmentPresenter
import com.gpetuhov.android.hive.ui.epoxy.profile.models.details
import javax.inject.Inject

class ProfileListController(private val presenter: ProfileFragmentPresenter) : EpoxyController() {

    @Inject lateinit var context: Context

    private lateinit var user: User

    init {
        HiveApp.appComponent.inject(this)
    }

    override fun buildModels() {
        details {
            id("details")

            username(if (user.hasUsername) user.username else context.getString(R.string.enter_username))
            onUsernameClick { presenter.showUsernameDialog() }

            userPicUrl(user.userPicUrl)
            onUserPicClick { presenter.chooseUserPic() }

            name(user.name)
            email(user.email)
        }
    }

    fun changeUser(user: User) {
        this.user = user
        requestModelBuild()
    }
}