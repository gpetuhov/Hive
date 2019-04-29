package com.gpetuhov.android.hive.domain.interactor.base

abstract class SaveUserPropertyInteractor : Interactor {

    interface Callback {
        fun onSaveError(errorMessage: String)
    }
}