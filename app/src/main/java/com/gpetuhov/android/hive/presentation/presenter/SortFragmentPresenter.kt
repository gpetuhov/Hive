package com.gpetuhov.android.hive.presentation.presenter

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.gpetuhov.android.hive.application.HiveApp
import com.gpetuhov.android.hive.domain.model.Sort
import com.gpetuhov.android.hive.presentation.view.SortFragmentView
import com.gpetuhov.android.hive.util.Settings
import javax.inject.Inject

@InjectViewState
class SortFragmentPresenter : MvpPresenter<SortFragmentView>() {

    @Inject lateinit var settings: Settings

    private var sort = Sort()
    private var isSortChanged = false

    init {
        HiveApp.appComponent.inject(this)
    }

    // === Public methods ===

    // --- Init presenter ---

    fun init() {
        // This check is needed to prevent overwriting changed sort
        // with sort from settings on screen rotation.
        if (!isSortChanged) sort = settings.getSearchSort()
    }

    // --- Sort param ---

    fun sortByTitle() {
        setSortChanged()
        sort.setSortByTitle()
    }

    fun isSortByTitle() = sort.isSortByTitle

    fun sortByPrice() {
        setSortChanged()
        sort.setSortByPrice()
    }

    fun isSortByPrice() = sort.isSortByPrice

    fun sortByRating() {
        setSortChanged()
        sort.setSortByRating()
    }

    fun isSortByRating() = sort.isSortByRating

    // --- Sort order ---

    fun sortOrderAscending() {
        setSortChanged()
        sort.setSortOrderAscending()
    }

    fun isSortOrderAscending() = sort.isSortOrderAscending

    fun sortOrderDescending() {
        setSortChanged()
        sort.setSortOrderDescending()
    }

    fun isSortOrderDescending() = sort.isSortOrderDescending

    // --- Clear sort ---

    fun showClearSortDialog() = viewState.showClearSortDialog()

    fun clearSort() {
        setSortChanged()
        sort = Sort()
        viewState.updateUI()
        dismissClearSortDialog()
    }

    fun dismissClearSortDialog() = viewState.dismissClearSortDialog()

    // --- Show results ---

    fun showResult() {
        settings.setSearchSort(sort)
        navigateUp()
    }

    // --- Navigation ---

    fun navigateUp() = viewState.navigateUp()

    // === Private methods ===

    private fun setSortChanged() {
        isSortChanged = true
    }
}