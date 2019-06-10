package com.gpetuhov.android.hive.presentation.presenter

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.gpetuhov.android.hive.domain.model.Filter
import com.gpetuhov.android.hive.presentation.view.FilterFragmentView
import timber.log.Timber

@InjectViewState
class FilterFragmentPresenter : MvpPresenter<FilterFragmentView>() {

    private var filter = Filter()

    // === Public methods ===

    // --- Basic filter params ---

    fun showUsersOffersAll() = filter.setShowUsersOffersAll()

    fun showUsersOnly() = filter.setShowUsersOnly()

    fun showOffersOnly() = filter.setShowOffersOnly()

    // --- Clear filter ---

    fun showClearFilterDialog() = viewState.showClearFilterDialog()

    fun clearFilter() {
        // TODO: clear filter

        dismissClearFilterDialog()
    }

    fun dismissClearFilterDialog() = viewState.dismissClearFilterDialog()

    // --- Show results ---

    fun showResult() {
        // TODO

        val filterJson = Filter.toJson(filter)
        Timber.tag("FilterPresenter").d(filterJson)
    }

    // --- Navigation ---

    fun navigateUp() = viewState.navigateUp()
}