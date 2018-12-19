package com.gpetuhov.android.hive.ui.epoxy.offer.favorite.controller

import android.content.Context
import com.gpetuhov.android.hive.application.HiveApp
import com.gpetuhov.android.hive.domain.model.Offer
import com.gpetuhov.android.hive.presentation.presenter.FavoriteOffersFragmentPresenter
import com.gpetuhov.android.hive.ui.epoxy.base.controller.UserBaseController
import com.gpetuhov.android.hive.util.Settings
import javax.inject.Inject

class OfferFavoriteListController(private val presenter: FavoriteOffersFragmentPresenter) : UserBaseController() {

    @Inject lateinit var context: Context
    @Inject lateinit var settings: Settings

    private var favoriteOffersList = mutableListOf<Offer>()

    init {
        HiveApp.appComponent.inject(this)
    }

    override fun buildModels() {
        favoriteOffersList.forEach { offer ->
            userOfferItem(context, settings, offer, false) { presenter.showOfferDetails(offer.userUid, offer.uid) }
        }
    }

    fun changeFavoriteOffersList(favoriteOffersList: MutableList<Offer>) {
        this.favoriteOffersList = favoriteOffersList
        requestModelBuild()
    }
}