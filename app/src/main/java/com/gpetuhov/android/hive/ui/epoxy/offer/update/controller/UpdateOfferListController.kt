package com.gpetuhov.android.hive.ui.epoxy.offer.update.controller

import android.content.Context
import com.airbnb.epoxy.EpoxyController
import com.gpetuhov.android.hive.R
import com.gpetuhov.android.hive.application.HiveApp
import com.gpetuhov.android.hive.presentation.presenter.UpdateOfferFragmentPresenter
import com.gpetuhov.android.hive.ui.epoxy.offer.update.models.updateOfferDetails
import com.gpetuhov.android.hive.ui.epoxy.offer.update.models.updateOfferHeader
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

        updateOfferDetails {
            id("update_offer_details")

            title(if (presenter.title != "") presenter.title else context.getString(R.string.add_title))
            onTitleClick { presenter.showTitleDialog() }

            description(if (presenter.description != "") presenter.description else context.getString(R.string.add_description))
            onDescriptionClick { presenter.showDescriptionDialog() }
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