package com.gpetuhov.android.hive.presentation.presenter

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.gpetuhov.android.hive.presentation.view.SortFragmentView

@InjectViewState
class SortFragmentPresenter : MvpPresenter<SortFragmentView>() {

    // --- Navigation ---

    fun navigateUp() = viewState.navigateUp()
}