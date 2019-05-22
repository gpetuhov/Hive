package com.gpetuhov.android.hive.domain.interactor

import com.gpetuhov.android.hive.application.HiveApp
import com.gpetuhov.android.hive.domain.interactor.base.SaveUserPropertyInteractor

class SaveAwardCongratulationShownInteractor : SaveUserPropertyInteractor() {

    private var newAwardCongratulationShownList = mutableListOf<Int>()

    init {
        HiveApp.appComponent.inject(this)
    }

    // Do not call this directly, call saveAwardCongratulationShown() instead!
    override fun execute() {
        repo.saveAwardCongratulationShown(newAwardCongratulationShownList)
    }

    fun saveAwardCongratulationShown(newAwardCongratulationShownList: MutableList<Int>) {
        this.newAwardCongratulationShownList = newAwardCongratulationShownList
        execute()
    }
}