package com.gpetuhov.android.hive.domain.interactor

import com.gpetuhov.android.hive.application.HiveApp
import com.gpetuhov.android.hive.domain.interactor.base.SaveUserPropertyInteractor
import com.gpetuhov.android.hive.util.Constants

class SaveActivityInteractor() : SaveUserPropertyInteractor() {

    private var newActivity = Constants.User.NO_ACTIVITY

    init {
        HiveApp.appComponent.inject(this)
    }

    // Do not call this directly, call saveActivity() instead!
    override fun execute() {
        repo.saveUserActivity(newActivity)
    }

    fun saveActivity(newActivity: Long) {
        this.newActivity = newActivity
        execute()
    }
}