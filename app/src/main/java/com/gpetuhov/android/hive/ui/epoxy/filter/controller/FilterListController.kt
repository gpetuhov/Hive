package com.gpetuhov.android.hive.ui.epoxy.filter.controller

import com.airbnb.epoxy.EpoxyController
import com.gpetuhov.android.hive.presentation.presenter.FilterFragmentPresenter
import com.gpetuhov.android.hive.ui.epoxy.filter.models.filterBasics
import com.gpetuhov.android.hive.ui.epoxy.filter.models.filterOffers

class FilterListController(private val presenter: FilterFragmentPresenter) : EpoxyController()  {

    override fun buildModels() {
        filterBasics {
            id("filter_basics")

            showUsersOffersAll(presenter.isShowUsersOffersAll())
            onShowUsersOffersAllClick { presenter.showUsersOffersAll() }

            showUsersOnly(presenter.isShowUsersOnly())
            onShowUsersOnlyClick { presenter.showUsersOnly() }

            showOffersOnly(presenter.isShowOffersOnly())
            onShowOffersOnlyClick { presenter.showOffersOnly() }
        }

        filterOffers {
            id("filter_offers")

            freeOffersOnly(presenter.isFreeOffersOnly())
            onFreeOffersOnlyClick { freeOffersOnly -> presenter.freeOffersOnly(freeOffersOnly) }

            offersWithReviewsOnly(presenter.isOffersWithReviewsOnly())
            onOffersWithReviewsClick { offersWithReviewsOnly -> presenter.offersWithReviewsOnly(offersWithReviewsOnly) }
        }
    }
}