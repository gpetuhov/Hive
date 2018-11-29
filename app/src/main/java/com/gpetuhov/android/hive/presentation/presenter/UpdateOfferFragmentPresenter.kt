package com.gpetuhov.android.hive.presentation.presenter

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.gpetuhov.android.hive.presentation.view.UpdateOfferFragmentView

@InjectViewState
class UpdateOfferFragmentPresenter : MvpPresenter<UpdateOfferFragmentView>() {

    // === Public methods ===

    // TODO: show "Are you sure?" dialog here
    fun navigateUp() = viewState.navigateUp()
}