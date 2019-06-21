package com.gpetuhov.android.hive.domain.interactor

import com.gpetuhov.android.hive.application.HiveApp
import com.gpetuhov.android.hive.domain.interactor.base.SaveUserPropertyInteractor

class DeleteUserPicInteractor(private val callback: Callback) : SaveUserPropertyInteractor() {

    init {
        HiveApp.appComponent.inject(this)
    }

    override fun execute() = repo.deleteUserPic { callback.onSaveError(resultMessages.getDeleteUserPicErrorMessage()) }
}