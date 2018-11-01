package com.gpetuhov.android.hive.domain.interactor

import com.gpetuhov.android.hive.application.HiveApp
import com.gpetuhov.android.hive.domain.repository.Repo
import com.gpetuhov.android.hive.domain.util.Messages
import javax.inject.Inject

class DeleteServiceInteractor(private val callback: Callback) : Interactor {

    interface Callback {
        fun onDeleteServiceError(errorMessage: String)
    }

    @Inject lateinit var repo: Repo
    @Inject lateinit var messages: Messages

    init {
        HiveApp.appComponent.inject(this)
    }

    override fun execute() =
        repo.deleteUserService { callback.onDeleteServiceError(messages.getDeleteServiceErrorMessage()) }
}