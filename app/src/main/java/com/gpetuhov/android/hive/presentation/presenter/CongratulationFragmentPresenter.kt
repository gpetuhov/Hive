package com.gpetuhov.android.hive.presentation.presenter

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.gpetuhov.android.hive.domain.interactor.SaveAwardCongratulationShownInteractor
import com.gpetuhov.android.hive.presentation.view.CongratulationFragmentView

@InjectViewState
class CongratulationFragmentPresenter : MvpPresenter<CongratulationFragmentView>() {

    private var saveAwardCongratulationShownInteractor = SaveAwardCongratulationShownInteractor()

    // === Public methods ===

    fun saveAwardCongratulationShown(newAwardsList: MutableList<Int>) =
        saveAwardCongratulationShownInteractor.saveAwardCongratulationShown(newAwardsList)

    fun navigateUp() = viewState.navigateUp()
}