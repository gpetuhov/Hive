package com.gpetuhov.android.hive.presentation.presenter

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.gpetuhov.android.hive.domain.model.Sort
import com.gpetuhov.android.hive.presentation.view.SortFragmentView

@InjectViewState
class SortFragmentPresenter : MvpPresenter<SortFragmentView>() {

    private var sort = Sort()
    private var isSortChanged = false

    // === Public methods ===
    // --- Sort param ---

    // --- Basic filter params ---

    fun sortByTitle() {
        setFilterChanged()
        sort.setSortByTitle()
    }

    fun isSortByTitle() = sort.isSortByTitle

    fun sortByPrice() {
        setFilterChanged()
        sort.setSortByPrice()
    }

    fun isSortByPrice() = sort.isSortByPrice

    fun sortByRating() {
        setFilterChanged()
        sort.setSortByRating()
    }

    fun isSortByRating() = sort.isSortByRating

    // --- Navigation ---

    fun navigateUp() = viewState.navigateUp()

    // === Private methods ===

    private fun setFilterChanged() {
        isSortChanged = true
    }
}