package com.gpetuhov.android.hive.ui.epoxy.offer.favorite.controller

import android.content.Context
import com.gpetuhov.android.hive.R
import com.gpetuhov.android.hive.application.HiveApp
import com.gpetuhov.android.hive.domain.model.Offer
import com.gpetuhov.android.hive.ui.epoxy.base.controller.BaseController
import com.gpetuhov.android.hive.ui.epoxy.offer.item.models.offerItem
import javax.inject.Inject

class OfferFavoriteListController : BaseController() {

    @Inject lateinit var context: Context

    private var favoriteOffersList = mutableListOf<Offer>()

    init {
        HiveApp.appComponent.inject(this)
    }

    override fun buildModels() {
        favoriteOffersList.forEach { offer ->
            offerItem {
                id("${offer.userUid}${offer.uid}")
                active(offer.isActive)
                activeVisible(false)
                title(offer.title)
                free(offer.isFree)
                price(if (offer.isFree) context.getString(R.string.free_caps) else "${offer.price} USD")
                onClick {
                    // TODO: implement this
                }
            }
        }
    }

    fun changeFavoriteOffersList(favoriteOffersList: MutableList<Offer>) {
        this.favoriteOffersList = favoriteOffersList
        requestModelBuild()
    }
}