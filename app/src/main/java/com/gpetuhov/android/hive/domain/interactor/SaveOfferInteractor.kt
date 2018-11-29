package com.gpetuhov.android.hive.domain.interactor

import com.gpetuhov.android.hive.application.HiveApp
import com.gpetuhov.android.hive.domain.model.Offer
import com.gpetuhov.android.hive.domain.repository.Repo
import com.gpetuhov.android.hive.domain.util.ResultMessages
import javax.inject.Inject

class SaveOfferInteractor(private val callback: Callback) : Interactor {

    interface Callback {
        fun onSaveOfferSuccess()
        fun onSaveOfferError(errorMessage: String)
    }

    @Inject lateinit var repo: Repo
    @Inject lateinit var resultMessages: ResultMessages

    private var offer: Offer? = null

    init {
        HiveApp.appComponent.inject(this)
    }

    // Do not call this directly!
    override fun execute() {
        repo.saveOffer(
            offer,
            { callback.onSaveOfferSuccess() },
            { callback.onSaveOfferError(resultMessages.getSaveOfferErrorMessage()) }
        )
    }

    // Call this method to save offer
    fun saveOffer(offer: Offer) {
        this.offer = offer
        execute()
    }
}