package com.gpetuhov.android.hive.ui.epoxy.profile.controller

import android.content.Context
import com.airbnb.epoxy.EpoxyController
import com.gpetuhov.android.hive.R
import com.gpetuhov.android.hive.application.HiveApp
import com.gpetuhov.android.hive.domain.model.User
import com.gpetuhov.android.hive.presentation.presenter.ProfileFragmentPresenter
import com.gpetuhov.android.hive.ui.epoxy.offer.item.models.offerItem
import com.gpetuhov.android.hive.ui.epoxy.photo.item.models.PhotoItemModel_
import com.gpetuhov.android.hive.ui.epoxy.profile.models.addOffer
import com.gpetuhov.android.hive.ui.epoxy.profile.models.addPhoto
import com.gpetuhov.android.hive.ui.epoxy.profile.models.details
import com.gpetuhov.android.hive.ui.epoxy.profile.models.settings
import com.gpetuhov.android.hive.util.Constants
import com.gpetuhov.android.hive.util.Settings
import com.gpetuhov.android.hive.util.epoxy.carousel
import com.gpetuhov.android.hive.util.epoxy.getPhotoUrlList
import com.gpetuhov.android.hive.util.epoxy.getSelectedPhotoPosition
import com.gpetuhov.android.hive.util.epoxy.withModelsFrom
import javax.inject.Inject

class ProfileListController(private val presenter: ProfileFragmentPresenter) : EpoxyController() {

    @Inject lateinit var context: Context
    @Inject lateinit var settings: Settings

    private var user: User? = null
    private var signOutEnabled = true
    private var deleteAccountEnabled = true

    init {
        HiveApp.appComponent.inject(this)
    }

    override fun buildModels() {
        val photoList = user?.photoList ?: mutableListOf()

        addPhoto {
            id("addPhoto")
            onClick { presenter.choosePhoto() }
            maxPhotoWarningVisible(photoList.size >= Constants.User.MAX_VISIBLE_PHOTO_COUNT)
        }

        if (!photoList.isEmpty()) {
            carousel {
                id("photo_carousel")

                paddingDp(0)

                withModelsFrom(photoList) {
                    PhotoItemModel_()
                        .id(it.uid)
                        .photoUrl(it.downloadUrl)
                        .onClick { presenter.openPhotos(getSelectedPhotoPosition(settings, it.uid, photoList), getPhotoUrlList(photoList)) }
                        .onLongClick { presenter.showDeletePhotoDialog(it.uid) }
                }
            }
        }

        details {
            id("details")

            val hasUsername = user?.hasUsername ?: false
            username(if (hasUsername) user?.username ?: "" else context.getString(R.string.enter_username))
            onUsernameClick { presenter.showUsernameDialog() }

            userPicUrl(user?.userPicUrl ?: "")
            onUserPicClick { presenter.chooseUserPic() }

            name(user?.name ?: "")
            email(user?.email ?: "")

            val hasDescription = user?.hasDescription ?: false
            description(if (hasDescription) user?.description ?: "" else context.getString(R.string.enter_description))
            onDescriptionClick { presenter.showDescriptionDialog() }

            val hasActiveOffer = user?.hasActiveOffer() ?: false
            noActiveOffersWarningVisible(!hasActiveOffer)
        }

        user?.offerList?.forEach {
            offerItem {
                id(it.uid)
                active(it.isActive)
                activeVisible(true)
                title(it.title)
                free(it.isFree)
                price(if (it.isFree) context.getString(R.string.free_caps) else "${it.price} USD")
                onClick { presenter.updateOffer(it.uid) }
            }
        }

        addOffer {
            id("addOffer")
            onClick { presenter.updateOffer("") }
        }

        settings {
            id("settings")

            onSignOutClick { presenter.showSignOutDialog() }
            signOutEnabled(signOutEnabled)

            onDeleteAccountClick { presenter.showDeleteUserDialog() }
            deleteAccountEnabled(deleteAccountEnabled)
        }
    }

    fun changeUser(user: User) {
        this.user = user
        requestModelBuild()
    }

    fun signOutEnabled(isEnabled: Boolean) {
        signOutEnabled = isEnabled
        requestModelBuild()
    }

    fun deleteAccountEnabled(isEnabled: Boolean) {
        deleteAccountEnabled = isEnabled
        requestModelBuild()
    }
}