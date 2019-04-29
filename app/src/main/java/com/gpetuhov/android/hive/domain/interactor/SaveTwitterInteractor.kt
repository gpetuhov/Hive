package com.gpetuhov.android.hive.domain.interactor

import com.gpetuhov.android.hive.application.HiveApp
import com.gpetuhov.android.hive.domain.interactor.base.Interactor
import com.gpetuhov.android.hive.domain.repository.Repo
import com.gpetuhov.android.hive.domain.util.ResultMessages
import javax.inject.Inject

class SaveTwitterInteractor(private val callback: Callback) : Interactor {

    interface Callback {
        fun onSaveTwitterError(errorMessage: String)
    }

    @Inject lateinit var repo: Repo
    @Inject lateinit var resultMessages: ResultMessages

    private var newTwitter = ""

    init {
        HiveApp.appComponent.inject(this)
    }

    // Do not call this directly!
    override fun execute() {
        repo.saveUserTwitter(newTwitter) { callback.onSaveTwitterError(resultMessages.getSaveTwitterErrorMessage()) }
    }

    // Call this method to save new Twitter
    fun saveTwitter(newTwitter: String) {
        this.newTwitter = newTwitter
        execute()
    }
}