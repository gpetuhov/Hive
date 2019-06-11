package com.gpetuhov.android.hive.ui.epoxy.filter.controller

import com.airbnb.epoxy.EpoxyController
import com.gpetuhov.android.hive.presentation.presenter.FilterFragmentPresenter
import com.gpetuhov.android.hive.ui.epoxy.filter.models.filterBasics

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
    }
}