package com.gpetuhov.android.hive.domain.interactor

import com.gpetuhov.android.hive.application.HiveApp
import com.gpetuhov.android.hive.domain.repository.Repo
import com.gpetuhov.android.hive.domain.util.ResultMessages
import javax.inject.Inject

class DeleteServiceInteractor(private val callback: Callback) : Interactor {

    interface Callback {
        fun onDeleteServiceError(errorMessage: String)
    }

    @Inject lateinit var repo: Repo
    @Inject lateinit var resultMessages: ResultMessages

    init {
        HiveApp.appComponent.inject(this)
    }

    override fun execute() =
        repo.deleteUserService { callback.onDeleteServiceError(resultMessages.getDeleteServiceErrorMessage()) }
}