package com.gpetuhov.android.hive.domain.interactor

import com.gpetuhov.android.hive.application.HiveApp
import com.gpetuhov.android.hive.domain.repository.Repo
import com.gpetuhov.android.hive.domain.util.ResultMessages
import javax.inject.Inject

class SaveOnlineInteractor(private val callback: Callback) : Interactor {

    interface Callback {
        fun onSaveOnlineComplete()
    }

    @Inject lateinit var repo: Repo
    @Inject lateinit var resultMessages: ResultMessages

    private var newIsOnline = false

    init {
        HiveApp.appComponent.inject(this)
    }

    // Do not call this directly!
    override fun execute() = repo.saveUserOnlineStatus(newIsOnline) { callback.onSaveOnlineComplete() }

    // Call this method to save new online status
    fun saveOnline(newIsOnline: Boolean) {
        this.newIsOnline = newIsOnline
        execute()
    }
}