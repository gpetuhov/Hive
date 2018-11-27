package com.gpetuhov.android.hive.domain.interactor

import com.gpetuhov.android.hive.application.HiveApp
import com.gpetuhov.android.hive.domain.repository.Repo
import com.gpetuhov.android.hive.domain.util.ResultMessages
import javax.inject.Inject

class DeleteOfferInteractor(private val callback: Callback) : Interactor {

    interface Callback {
        fun onDeleteOfferError(errorMessage: String)
    }

    @Inject lateinit var repo: Repo
    @Inject lateinit var resultMessages: ResultMessages

    init {
        HiveApp.appComponent.inject(this)
    }

    override fun execute() =
        repo.deleteUserOffer { callback.onDeleteOfferError(resultMessages.getDeleteOfferErrorMessage()) }
}