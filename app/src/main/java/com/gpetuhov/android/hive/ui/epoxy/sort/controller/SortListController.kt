package com.gpetuhov.android.hive.ui.epoxy.sort.controller

import com.airbnb.epoxy.EpoxyController
import com.gpetuhov.android.hive.presentation.presenter.SortFragmentPresenter
import com.gpetuhov.android.hive.ui.epoxy.sort.models.sortParam

class SortListController(private val presenter: SortFragmentPresenter) : EpoxyController()  {

    override fun buildModels() {
        sortParam {
            id("sort_param")

            // TODO: init with values from presenter

            sortByTitle(true)
            onSortByTitleClick {
                // TODO
            }

            sortByPrice(false)
            onSortByPriceClick {
                // TODO
            }

            sortByRating(false)
            onSortByRatingClick {
                // TODO
            }
        }
    }
}