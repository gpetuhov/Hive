package com.gpetuhov.android.hive.domain.interactor.base

import com.gpetuhov.android.hive.domain.repository.Repo
import com.gpetuhov.android.hive.domain.util.ResultMessages
import javax.inject.Inject

abstract class SaveUserPropertyInteractor : Interactor {

    @Inject lateinit var repo: Repo
    @Inject lateinit var resultMessages: ResultMessages

    protected var newValue = ""

    interface Callback {
        fun onSaveError(errorMessage: String)
    }

    fun save(newValue: String) {
        this.newValue = newValue
        execute()
    }
}