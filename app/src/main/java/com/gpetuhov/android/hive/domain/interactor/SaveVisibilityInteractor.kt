package com.gpetuhov.android.hive.domain.interactor

import com.gpetuhov.android.hive.application.HiveApp
import com.gpetuhov.android.hive.domain.repository.Repo
import com.gpetuhov.android.hive.domain.util.ResultMessages
import javax.inject.Inject

class SaveVisibilityInteractor(private val callback: Callback) : Interactor {

    interface Callback {
        fun onSaveVisibilityError(errorMessage: String)
    }

    @Inject lateinit var repo: Repo
    @Inject lateinit var resultMessages: ResultMessages

    private var newIsVisible = false

    init {
        HiveApp.appComponent.inject(this)
    }

    // Do not call this directly!
    override fun execute() =
        repo.saveUserVisibility(newIsVisible) { callback.onSaveVisibilityError(resultMessages.getSaveVisibilityErrorMessage()) }

    // Call this method to save new visibility
    fun saveVisibility(newIsVisible: Boolean) {
        this.newIsVisible = newIsVisible
        execute()
    }
}