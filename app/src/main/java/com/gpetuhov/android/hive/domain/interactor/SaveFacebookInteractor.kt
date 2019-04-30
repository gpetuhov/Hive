package com.gpetuhov.android.hive.domain.interactor

import com.gpetuhov.android.hive.application.HiveApp
import com.gpetuhov.android.hive.domain.interactor.base.SaveUserPropertyInteractor
import com.gpetuhov.android.hive.domain.repository.Repo
import com.gpetuhov.android.hive.domain.util.ResultMessages
import javax.inject.Inject

class SaveFacebookInteractor(private val callback: Callback) : SaveUserPropertyInteractor() {

    @Inject lateinit var repo: Repo
    @Inject lateinit var resultMessages: ResultMessages

    private var newFacebook = ""

    init {
        HiveApp.appComponent.inject(this)
    }

    // Do not call this directly!
    override fun execute() {
        repo.saveUserFacebook(newFacebook) { callback.onSaveError(resultMessages.getSaveFacebookErrorMessage()) }
    }

    // Call this method to save new Facebook
    fun saveFacebook(newFacebook: String) {
        this.newFacebook = newFacebook
        execute()
    }
}