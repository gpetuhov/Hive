package com.gpetuhov.android.hive.domain.interactor

import com.gpetuhov.android.hive.application.HiveApp
import com.gpetuhov.android.hive.domain.interactor.base.Interactor
import com.gpetuhov.android.hive.domain.repository.Repo
import com.gpetuhov.android.hive.domain.util.ResultMessages
import javax.inject.Inject

class SavePhoneInteractor(private val callback: Callback) : Interactor {

    interface Callback {
        fun onSavePhoneError(errorMessage: String)
    }

    @Inject lateinit var repo: Repo
    @Inject lateinit var resultMessages: ResultMessages

    private var newPhone = ""

    init {
        HiveApp.appComponent.inject(this)
    }

    // Do not call this directly!
    override fun execute() {
        repo.saveUserPhone(newPhone) { callback.onSavePhoneError(resultMessages.getSavePhoneErrorMessage()) }
    }

    // Call this method to save new phone
    fun savePhone(newPhone: String) {
        this.newPhone = newPhone
        execute()
    }
}