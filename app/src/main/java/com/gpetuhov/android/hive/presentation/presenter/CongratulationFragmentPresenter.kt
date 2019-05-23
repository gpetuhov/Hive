package com.gpetuhov.android.hive.presentation.presenter

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.gpetuhov.android.hive.domain.interactor.SaveAwardCongratulationShownInteractor
import com.gpetuhov.android.hive.presentation.view.CongratulationFragmentView

@InjectViewState
class CongratulationFragmentPresenter : MvpPresenter<CongratulationFragmentView>() {

    var newAwardsList = mutableListOf<Int>()

    private var saveAwardCongratulationShownInteractor = SaveAwardCongratulationShownInteractor()

    // === Public methods ===

    fun saveAwardCongratulationShown(newAwardsList: MutableList<Int>) {
        this.newAwardsList.clear()
        this.newAwardsList.addAll(newAwardsList)
        saveAwardCongratulationShownInteractor.saveAwardCongratulationShown(newAwardsList)
    }

    fun navigateUp() = viewState.navigateUp()
}