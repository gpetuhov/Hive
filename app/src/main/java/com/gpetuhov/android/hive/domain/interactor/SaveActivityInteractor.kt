package com.gpetuhov.android.hive.domain.interactor

import com.gpetuhov.android.hive.application.HiveApp
import com.gpetuhov.android.hive.domain.interactor.base.SaveUserPropertyInteractor

class SaveActivityInteractor() : SaveUserPropertyInteractor() {

    private var newActivity = 100

    init {
        HiveApp.appComponent.inject(this)
    }

    // Do not call this directly, call saveActivity() instead!
    override fun execute() {
        repo.saveUserActivity(newActivity)
    }

    fun saveActivity(newActivity: Int) {
        this.newActivity = newActivity
        execute()
    }
}