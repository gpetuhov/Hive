package com.gpetuhov.android.hive.ui.epoxy.sort.controller

import com.airbnb.epoxy.EpoxyController
import com.gpetuhov.android.hive.presentation.presenter.SortFragmentPresenter
import com.gpetuhov.android.hive.ui.epoxy.sort.models.sortOrder
import com.gpetuhov.android.hive.ui.epoxy.sort.models.sortParam

class SortListController(private val presenter: SortFragmentPresenter) : EpoxyController()  {

    override fun buildModels() {
        sortParam {
            id("sort_param")

            sortByTitle(presenter.isSortByTitle())
            onSortByTitleClick { presenter.sortByTitle() }

            sortByPrice(presenter.isSortByPrice())
            onSortByPriceClick { presenter.sortByPrice() }

            sortByRating(presenter.isSortByRating())
            onSortByRatingClick { presenter.sortByRating() }
        }

        sortOrder {
            id("sort_order")

            ascending(presenter.isSortOrderAscending())
            onAscendingClick { presenter.sortOrderAscending() }

            descending(presenter.isSortOrderDescending())
            onDescendingClick { presenter.sortOrderDescending() }
        }
    }
}