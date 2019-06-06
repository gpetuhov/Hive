package com.gpetuhov.android.hive.ui.epoxy.search.controller

import android.content.Context
import com.gpetuhov.android.hive.R
import com.gpetuhov.android.hive.application.HiveApp
import com.gpetuhov.android.hive.presentation.presenter.SearchListFragmentPresenter
import com.gpetuhov.android.hive.ui.epoxy.base.carousel
import com.gpetuhov.android.hive.ui.epoxy.base.controller.UserBaseController
import com.gpetuhov.android.hive.ui.epoxy.base.withModelsFrom
import com.gpetuhov.android.hive.ui.epoxy.offer.item.models.OfferItemOnePhotoModel_
import com.gpetuhov.android.hive.ui.epoxy.search.models.searchTotals
import com.gpetuhov.android.hive.ui.epoxy.user.item.model.userItem
import com.gpetuhov.android.hive.util.Settings
import javax.inject.Inject

class SearchListController(private val presenter: SearchListFragmentPresenter) : UserBaseController() {

    @Inject lateinit var context: Context
    @Inject lateinit var settings: Settings

    init {
        HiveApp.appComponent.inject(this)
    }

    override fun buildModels() {
        val searchResultList = presenter.searchResultList
        val searchResultCount = searchResultList.size

        searchTotals {
            id("search_totals")

            searchTotals("${context.getString(R.string.search_results)}: $searchResultCount")
        }

        searchResultList.forEach { user ->
            val offer = user.getSearchedOffer()

            if (offer != null) {
                userOfferItem(
                    context,
                    settings,
                    offer,
                    false,
                    { presenter.favoriteOffer(offer.isFavorite, offer.userUid, offer.uid) },
                    { presenter.showDetails(offer.userUid, offer.uid) }
                )

            } else {
                // TODO: refactor this

                userItem {
                    id(user.uid)
                    userPicUrl(user.userPicUrl)
                    onClick { presenter.showDetails(user.uid, "") }
                    username(user.getUsernameOrName())
                    userStarCount(user.userStarCountString)
                    onFavoriteButtonClick {
                        // TODO: implement
                    }
                }

                val activeOfferList = user.offerList.filter { it.isActive }

                if (!activeOfferList.isEmpty()) {
                    carousel {
                        id("${user.uid}_offer_carousel")

                        withModelsFrom(activeOfferList) { index, offer ->
                            OfferItemOnePhotoModel_()
                                .id(offer.uid)
                                .photoUrl(offer.photoList.firstOrNull()?.downloadUrl ?: "")
                                .title(offer.title)
                                .free(offer.isFree)
                                .price(if (offer.isFree) context.getString(R.string.free_caps) else "${offer.price} USD")
                                .favorite(offer.isFavorite)
                                .rating(offer.rating)
                                .reviewCount(offer.reviewCount)
                                .onFavoriteButtonClick { presenter.favoriteOffer(offer.isFavorite, user.uid, offer.uid) }
                                .onClick { presenter.showDetails(user.uid, offer.uid) }
                        }
                    }
                }
            }
        }
    }
}