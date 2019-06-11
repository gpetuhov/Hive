package com.gpetuhov.android.hive.presentation.presenter

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.gpetuhov.android.hive.application.HiveApp
import com.gpetuhov.android.hive.domain.model.Filter
import com.gpetuhov.android.hive.presentation.view.FilterFragmentView
import com.gpetuhov.android.hive.util.Settings
import javax.inject.Inject

@InjectViewState
class FilterFragmentPresenter : MvpPresenter<FilterFragmentView>() {

    @Inject lateinit var settings: Settings

    var filter = Filter()

    init {
        HiveApp.appComponent.inject(this)
    }

    // TODO: init presenter with filter from settings and update UI

    // === Public methods ===

    // --- Clear filter ---

    fun showClearFilterDialog() = viewState.showClearFilterDialog()

    fun clearFilter() {
        filter = Filter()
        viewState.updateUI()
        dismissClearFilterDialog()
    }

    fun dismissClearFilterDialog() = viewState.dismissClearFilterDialog()

    // --- Show results ---

    fun showResult() {
        settings.setSearchFilter(filter)
        navigateUp()
    }

    // --- Navigation ---

    fun navigateUp() = viewState.navigateUp()
}