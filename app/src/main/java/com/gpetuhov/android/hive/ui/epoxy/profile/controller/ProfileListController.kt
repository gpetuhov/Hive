package com.gpetuhov.android.hive.ui.epoxy.profile.controller

import android.content.Context
import com.airbnb.epoxy.Carousel
import com.airbnb.epoxy.EpoxyController
import com.gpetuhov.android.hive.R
import com.gpetuhov.android.hive.application.HiveApp
import com.gpetuhov.android.hive.domain.model.User
import com.gpetuhov.android.hive.presentation.presenter.ProfileFragmentPresenter
import com.gpetuhov.android.hive.ui.epoxy.offer.item.models.offerItem
import com.gpetuhov.android.hive.ui.epoxy.photo.item.models.PhotoItemModel_
import com.gpetuhov.android.hive.ui.epoxy.photo.item.models.PhotoOfferItemModel_
import com.gpetuhov.android.hive.ui.epoxy.profile.models.addOffer
import com.gpetuhov.android.hive.ui.epoxy.profile.models.addPhoto
import com.gpetuhov.android.hive.ui.epoxy.profile.models.details
import com.gpetuhov.android.hive.ui.epoxy.profile.models.settings
import com.gpetuhov.android.hive.util.Constants
import com.gpetuhov.android.hive.util.Settings
import com.gpetuhov.android.hive.util.epoxy.*
import javax.inject.Inject

class ProfileListController(private val presenter: ProfileFragmentPresenter) : EpoxyController() {

    @Inject lateinit var context: Context
    @Inject lateinit var settings: Settings

    private var user: User? = null
    private var signOutEnabled = true
    private var deleteAccountEnabled = true
    private var scrollToSelectedPhoto = true
    private var selectedOfferPhotoMap = mutableMapOf<String, Int>()

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

                onBind { model, view, position ->
                    if (scrollToSelectedPhoto) {
                        scrollToSelectedPhoto = false
                        scrollToSavedSelectedPhotoPosition(settings, view, photoList.size, true)
                    }
                }

                withModelsIndexedFrom(photoList) { index, item ->
                    PhotoItemModel_()
                        .id(item.uid)
                        .photoUrl(item.downloadUrl)
                        .onClick {
                            settings.setSelectedPhotoPosition(index)
                            presenter.openPhotos(getPhotoUrlList(photoList))
                        }
                        .onLongClick { presenter.showDeletePhotoDialog(item.uid) }
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

        user?.offerList?.forEach { offer ->
            // Offer photo carousel
            val offerPhotoList = offer.photoList
            if (!offerPhotoList.isEmpty()) {
                carousel {
                    id("${offer.uid}_photo_carousel")

                    val padding = Carousel.Padding.dp(16, 0, 16, 0, 0)
                    padding(padding)

                    onBind { model, view, position ->
                        view.clipToPadding = true
                        view.addOnScrollListener(
                            buildScrollListener { lastScrollPosition -> selectedOfferPhotoMap[offer.uid] = lastScrollPosition}
                        )
                    }

                    withModelsIndexedFrom(offerPhotoList) { index, photo ->
                        PhotoOfferItemModel_()
                            .id(photo.uid)
                            .photoUrl(photo.downloadUrl)
                            .onClick {
                                settings.setSelectedPhotoPosition(index)
                                presenter.updateOffer(offer.uid)
                            }
                    }
                }
            }

            // Offer details
            offerItem {
                id(offer.uid)
                active(offer.isActive)
                activeVisible(true)
                title(offer.title)
                free(offer.isFree)
                price(if (offer.isFree) context.getString(R.string.free_caps) else "${offer.price} USD")
                onClick {
                    settings.setSelectedPhotoPosition(selectedOfferPhotoMap[offer.uid] ?: 0)
                    presenter.updateOffer(offer.uid)
                }
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