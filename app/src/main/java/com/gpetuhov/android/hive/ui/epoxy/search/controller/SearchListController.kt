package com.gpetuhov.android.hive.ui.epoxy.search.controller

import android.content.Context
import com.gpetuhov.android.hive.R
import com.gpetuhov.android.hive.application.HiveApp
import com.gpetuhov.android.hive.domain.model.User
import com.gpetuhov.android.hive.presentation.presenter.SearchListFragmentPresenter
import com.gpetuhov.android.hive.ui.epoxy.base.controller.UserBaseController
import com.gpetuhov.android.hive.ui.epoxy.search.models.searchTotals
import com.gpetuhov.android.hive.util.Settings
import javax.inject.Inject

class SearchListController(private val presenter: SearchListFragmentPresenter) : UserBaseController() {

    @Inject lateinit var context: Context
    @Inject lateinit var settings: Settings

    private var searchResultList = mutableListOf<User>()

    init {
        HiveApp.appComponent.inject(this)
    }

    override fun buildModels() {
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
                    { presenter.removeOfferFromFavorite(offer.userUid, offer.uid) },
                    { presenter.showDetails(offer.userUid, offer.uid) }
                )

            } else {
                // TODO: add user model
            }

        }
    }

    fun changeSearchResultList(searchResultList: MutableList<User>) {
        this.searchResultList = searchResultList
        requestModelBuild()
    }
}