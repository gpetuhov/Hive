package com.gpetuhov.android.hive.ui.epoxy.user.controller

import android.content.Context
import androidx.core.content.edit
import com.airbnb.epoxy.EpoxyController
import com.gpetuhov.android.hive.R
import com.gpetuhov.android.hive.application.HiveApp
import com.gpetuhov.android.hive.domain.model.User
import com.gpetuhov.android.hive.presentation.presenter.UserDetailsFragmentPresenter
import com.gpetuhov.android.hive.ui.epoxy.offer.item.models.offerItem
import com.gpetuhov.android.hive.ui.epoxy.photo.item.models.PhotoItemModel_
import com.gpetuhov.android.hive.ui.epoxy.user.models.userDetailsDescription
import com.gpetuhov.android.hive.ui.epoxy.user.models.userDetailsHeader
import com.gpetuhov.android.hive.ui.epoxy.user.models.userDetailsName
import com.gpetuhov.android.hive.ui.epoxy.user.models.userDetailsOfferHeader
import com.gpetuhov.android.hive.util.Constants
import com.gpetuhov.android.hive.util.epoxy.carousel
import com.gpetuhov.android.hive.util.epoxy.getPhotoUrlList
import com.gpetuhov.android.hive.util.epoxy.getSelectedPhotoPosition
import com.gpetuhov.android.hive.util.epoxy.withModelsFrom
import org.jetbrains.anko.defaultSharedPreferences
import javax.inject.Inject

class UserDetailsListController(private val presenter: UserDetailsFragmentPresenter) : EpoxyController() {

    @Inject lateinit var context: Context

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
                        val photoPosition = context.defaultSharedPreferences.getInt("selectedPhotoPosition", 0)
                        view.scrollToPosition(photoPosition)
                        context.defaultSharedPreferences.edit { putInt("selectedPhotoPosition", 0) }
                    }
                }

                withModelsFrom(visiblePhotos) {
                    PhotoItemModel_()
                        .id(it.uid)
                        .photoUrl(it.downloadUrl)
                        .onClick { presenter.openPhotos(getSelectedPhotoPosition(it.uid, visiblePhotos), getPhotoUrlList(visiblePhotos)) }
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

        user?.offerList?.forEach {
            if (it.isActive) {
                offerItem {
                    id(it.uid)
                    active(it.isActive)
                    activeVisible(false)
                    title(it.title)
                    free(it.isFree)
                    price(if (it.isFree) context.getString(R.string.free_caps) else "${it.price} USD")
                    onClick { presenter.openOffer(it.uid) }
                }
            }
        }
    }

    fun changeUser(user: User) {
        this.user = user
        requestModelBuild()
    }
}