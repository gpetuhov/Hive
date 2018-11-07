package com.gpetuhov.android.hive.domain.interactor

import com.gpetuhov.android.hive.application.HiveApp
import com.gpetuhov.android.hive.domain.repository.Repo
import com.gpetuhov.android.hive.domain.util.ResultMessages
import javax.inject.Inject

class SaveServiceInteractor(private val callback: Callback) : Interactor {

    interface Callback {
        fun onSaveServiceError(errorMessage: String)
    }

    @Inject lateinit var repo: Repo
    @Inject lateinit var resultMessages: ResultMessages

    private var newService = ""

    init {
        HiveApp.appComponent.inject(this)
    }

    // Do not call this directly!
    override fun execute() =
        repo.saveUserService(newService) { callback.onSaveServiceError(resultMessages.getSaveServiceErrorMessage()) }

    // Call this method to save new service
    fun saveService(newService: String) {
        this.newService = newService
        execute()
    }
}