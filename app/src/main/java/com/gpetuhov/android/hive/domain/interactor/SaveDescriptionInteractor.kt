package com.gpetuhov.android.hive.domain.interactor

import com.gpetuhov.android.hive.application.HiveApp
import com.gpetuhov.android.hive.domain.interactor.base.Interactor
import com.gpetuhov.android.hive.domain.repository.Repo
import com.gpetuhov.android.hive.domain.util.ResultMessages
import javax.inject.Inject

class SaveDescriptionInteractor(private val callback: Callback) : Interactor {

    interface Callback {
        fun onSaveDescriptionError(errorMessage: String)
    }

    @Inject lateinit var repo: Repo
    @Inject lateinit var resultMessages: ResultMessages

    private var newDescription = ""

    init {
        HiveApp.appComponent.inject(this)
    }

    // Do not call this directly!
    override fun execute() {
        repo.saveUserDescription(newDescription) { callback.onSaveDescriptionError(resultMessages.getSaveDescriptionErrorMessage()) }
    }

    // Call this method to save new description
    fun saveDescription(newDescription: String) {
        this.newDescription = newDescription
        execute()
    }
}