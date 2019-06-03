package com.gpetuhov.android.hive.domain.interactor

import com.gpetuhov.android.hive.application.HiveApp
import com.gpetuhov.android.hive.domain.interactor.base.SaveUserPropertyInteractor

class SaveHiveRunningInteractor : SaveUserPropertyInteractor() {

    private var isHiveRunning = false

    init {
        HiveApp.appComponent.inject(this)
    }

    // Do not call this directly, call saveHiveRunning() instead!
    override fun execute() {
        repo.saveHiveRunning(isHiveRunning)
    }

    fun saveHiveRunning(isHiveRunning: Boolean) {
        this.isHiveRunning = isHiveRunning
        execute()
    }
}