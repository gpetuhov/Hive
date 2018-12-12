package com.gpetuhov.android.hive.ui.epoxy.user.controller

import android.content.Context
import com.airbnb.epoxy.Carousel
import com.airbnb.epoxy.EpoxyController
import com.gpetuhov.android.hive.R
import com.gpetuhov.android.hive.application.HiveApp
import com.gpetuhov.android.hive.domain.model.User
import com.gpetuhov.android.hive.presentation.presenter.UserDetailsFragmentPresenter
import com.gpetuhov.android.hive.ui.epoxy.offer.item.models.offerItem
import com.gpetuhov.android.hive.ui.epoxy.photo.item.models.PhotoItemModel_
import com.gpetuhov.android.hive.ui.epoxy.photo.item.models.PhotoOfferItemModel_
import com.gpetuhov.android.hive.ui.epoxy.user.models.userDetailsDescription
import com.gpetuhov.android.hive.ui.epoxy.user.models.userDetailsHeader
import com.gpetuhov.android.hive.ui.epoxy.user.models.userDetailsName
import com.gpetuhov.android.hive.ui.epoxy.user.models.userDetailsOfferHeader
import com.gpetuhov.android.hive.util.Constants
import com.gpetuhov.android.hive.util.Settings
import com.gpetuhov.android.hive.util.epoxy.*
import javax.inject.Inject

class UserDetailsListController(private val presenter: UserDetailsFragmentPresenter) : EpoxyController() {

    @Inject lateinit var context: Context
    @Inject lateinit var settings: Settings

    private var user: User? = null
    private var scrollToSelectedPhoto = true

    init {
        HiveApp.appComponent.inject(this)
    }

    override fun buildModels() {
        userDetailsHeader {
            id("user_details_header")
            onBackButtonClick { presenter.navigateUp() }
        }

        val photoList = user?.photoList ?: mutableListOf()
        if (!photoList.isEmpty()) {
            val visiblePhotos = photoList.filterIndexed { index, item -> index < Constants.User.MAX_VISIBLE_PHOTO_COUNT }.toMutableList()

            carousel {
                id("photo_carousel")

                paddingDp(0)

                onBind { model, view, position ->
                    if (scrollToSelectedPhoto) {
                        scrollToSelectedPhoto = false
                        scrollToSavedSelectedPhotoPosition(settings, view, visiblePhotos.size, true)
                    }
                }

                withModelsIndexedFrom(visiblePhotos) { index, item ->
                    PhotoItemModel_()
                        .id(item.uid)
                        .photoUrl(item.downloadUrl)
                        .onClick {
                            settings.setSelectedPhotoPosition(index)
                            presenter.openPhotos(getPhotoUrlList(visiblePhotos))
                        }
                        .onLongClick { /* Do nothing */ }
                }
            }
        }

        userDetailsName {
            id("user_details_name")
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

        user?.offerList?.forEach { offer ->
            if (offer.isActive) {
                // Offer photo carousel
                val offerPhotoList = offer.photoList
                if (!offerPhotoList.isEmpty()) {
                    val visibleOfferPhotos = offerPhotoList.filterIndexed { index, item -> index < Constants.Offer.MAX_VISIBLE_PHOTO_COUNT }.toMutableList()

                    carousel {
                        id("${offer.uid}_photo_carousel")

                        val padding = Carousel.Padding.dp(16, 0, 16, 0, 0)
                        padding(padding)

                        onBind { model, view, position -> view.clipToPadding = true }

                        withModelsIndexedFrom(visibleOfferPhotos) { index, photo ->
                            PhotoOfferItemModel_()
                                .id(photo.uid)
                                .photoUrl(photo.downloadUrl)
                                .onClick {
                                    settings.setSelectedPhotoPosition(index)
                                    presenter.openOffer(offer.uid)
                                }
                        }
                    }
                }

                // Offer details
                offerItem {
                    id(offer.uid)
                    active(offer.isActive)
                    activeVisible(false)
                    title(offer.title)
                    free(offer.isFree)
                    price(if (offer.isFree) context.getString(R.string.free_caps) else "${offer.price} USD")
                    onClick { presenter.openOffer(offer.uid) }
                }
            }
        }
    }

    fun changeUser(user: User) {
        this.user = user
        requestModelBuild()
    }
}