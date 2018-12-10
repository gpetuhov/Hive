package com.gpetuhov.android.hive.ui.epoxy.offer.update.controller

import android.content.Context
import com.airbnb.epoxy.EpoxyController
import com.gpetuhov.android.hive.R
import com.gpetuhov.android.hive.application.HiveApp
import com.gpetuhov.android.hive.presentation.presenter.UpdateOfferFragmentPresenter
import com.gpetuhov.android.hive.ui.epoxy.offer.update.models.updateOfferDetails
import com.gpetuhov.android.hive.ui.epoxy.offer.update.models.updateOfferHeader
import com.gpetuhov.android.hive.ui.epoxy.offer.update.models.updateOfferPrice
import com.gpetuhov.android.hive.ui.epoxy.photo.item.models.PhotoItemModel_
import com.gpetuhov.android.hive.ui.epoxy.profile.models.addPhoto
import com.gpetuhov.android.hive.util.Constants
import com.gpetuhov.android.hive.util.epoxy.carousel
import com.gpetuhov.android.hive.util.epoxy.withModelsFrom
import javax.inject.Inject

class UpdateOfferListController(private val presenter: UpdateOfferFragmentPresenter) : EpoxyController() {

    @Inject lateinit var context: Context

    private var saveButtonEnabled = true
    private var deleteButtonEnabled = true

    init {
        HiveApp.appComponent.inject(this)
    }

    override fun buildModels() {
        updateOfferHeader {
            id("update_offer_header")

            onBackButtonClick { presenter.showQuitOfferUpdateDialog() }

            deleteButtonVisible(presenter.uid != "")
            deleteButtonEnabled(deleteButtonEnabled)
            onDeleteButtonClick { presenter.showDeleteOfferDialog() }

            saveButtonEnabled(saveButtonEnabled)
            onSaveButtonClick { presenter.saveOffer() }
        }

        val photoList = presenter.photoList.filter { !it.isDeleted }

        addPhoto {
            id("addPhoto")
            onClick { presenter.choosePhoto() }
            maxPhotoWarningVisible(photoList.size >= Constants.Offer.MAX_VISIBLE_PHOTO_COUNT)
        }

        if (!photoList.isEmpty()) {
            carousel {
                id("photo_carousel")

                paddingDp(0)

                withModelsFrom(photoList) {
                    PhotoItemModel_()
                        .id(it.uid)
                        .photoUrl(it.downloadUrl)
                        .onClick { /* Do nothing */ }
                        .onLongClick { presenter.showDeletePhotoDialog(it.uid) }
                }
            }
        }

        updateOfferDetails {
            id("update_offer_details")

            active(presenter.active)
            activeEnabled(presenter.activeEnabled)
            onActiveClick { isActive -> presenter.onActiveClick(isActive) }

            maxActiveWarningVisible(!presenter.activeEnabled)

            title(if (presenter.title != "") presenter.title else context.getString(R.string.add_title))
            onTitleClick { presenter.showTitleDialog() }

            description(if (presenter.description != "") presenter.description else context.getString(R.string.add_description))
            onDescriptionClick { presenter.showDescriptionDialog() }
        }

        updateOfferPrice {
            id("update_offer_price")

            free(presenter.free)
            onFreeClick { isFree -> presenter.onFreeClick(isFree) }

            price(presenter.price.toString())
            onPriceClick { presenter.showPriceDialog() }
        }
    }

    fun saveButtonEnabled(isEnabled: Boolean) {
        saveButtonEnabled = isEnabled
        requestModelBuild()
    }

    fun deleteButtonEnabled(isEnabled: Boolean) {
        deleteButtonEnabled = isEnabled
        requestModelBuild()
    }
}