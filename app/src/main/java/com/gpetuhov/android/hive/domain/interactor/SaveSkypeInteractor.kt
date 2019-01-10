package com.gpetuhov.android.hive.domain.interactor

import com.gpetuhov.android.hive.application.HiveApp
import com.gpetuhov.android.hive.domain.repository.Repo
import com.gpetuhov.android.hive.domain.util.ResultMessages
import javax.inject.Inject

class SaveSkypeInteractor(private val callback: Callback) : Interactor {

    interface Callback {
        fun onSaveSkypeError(errorMessage: String)
    }

    @Inject lateinit var repo: Repo
    @Inject lateinit var resultMessages: ResultMessages

    private var newSkype = ""

    init {
        HiveApp.appComponent.inject(this)
    }

    // Do not call this directly!
    override fun execute() {
        repo.saveUserSkype(newSkype) { callback.onSaveSkypeError(resultMessages.getSaveSkypeErrorMessage()) }
    }

    // Call this method to save new Skype
    fun saveSkype(newSkype: String) {
        this.newSkype = newSkype
        execute()
    }
}