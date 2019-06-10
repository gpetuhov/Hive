package com.gpetuhov.android.hive.ui.epoxy.filter.controller

import com.airbnb.epoxy.EpoxyController
import com.gpetuhov.android.hive.presentation.presenter.FilterFragmentPresenter
import com.gpetuhov.android.hive.ui.epoxy.filter.models.filterBasics

class FilterListController(private val presenter: FilterFragmentPresenter) : EpoxyController()  {

    override fun buildModels() {
        filterBasics {
            id("filter_basics")

            // TODO: get initial values from the presenter

            showUsersOffersAll(true)
            onShowUsersOffersAllClick { presenter.showUsersOffersAll() }

            showUsersOnly(false)
            onShowUsersOnlyClick { presenter.showUsersOnly() }

            showOffersOnly(false)
            onShowOffersOnlyClick { presenter.showOffersOnly() }
        }
    }
}