package com.gpetuhov.android.hive.ui.epoxy.filter.controller

import com.airbnb.epoxy.EpoxyController
import com.gpetuhov.android.hive.presentation.presenter.FilterFragmentPresenter
import com.gpetuhov.android.hive.ui.epoxy.filter.models.filterBasics

class FilterListController(private val presenter: FilterFragmentPresenter) : EpoxyController()  {

    override fun buildModels() {
        filterBasics {
            id("filter_basics")

            showUsersOffersAll(presenter.filter.isShowUsersOffersAll)
            onShowUsersOffersAllClick { presenter.filter.setShowUsersOffersAll() }

            showUsersOnly(presenter.filter.isShowUsersOnly)
            onShowUsersOnlyClick { presenter.filter.setShowUsersOnly() }

            showOffersOnly(presenter.filter.isShowOffersOnly)
            onShowOffersOnlyClick { presenter.filter.setShowOffersOnly() }
        }
    }
}