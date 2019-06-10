package com.gpetuhov.android.hive.presentation.presenter

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.gpetuhov.android.hive.presentation.view.FilterFragmentView

@InjectViewState
class FilterFragmentPresenter : MvpPresenter<FilterFragmentView>() {

    // === Public methods ===

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
    }

    // --- Navigation ---

    fun navigateUp() = viewState.navigateUp()
}