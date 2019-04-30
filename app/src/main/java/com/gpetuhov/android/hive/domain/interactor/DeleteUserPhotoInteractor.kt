package com.gpetuhov.android.hive.domain.interactor

import com.gpetuhov.android.hive.application.HiveApp
import com.gpetuhov.android.hive.domain.interactor.base.SaveUserPropertyInteractor

class DeleteUserPhotoInteractor(private val callback: Callback) : SaveUserPropertyInteractor() {

    private var photoUid = ""

    init {
        HiveApp.appComponent.inject(this)
    }

    // Do not call this directly!
    override fun execute() = repo.deleteUserPhoto(photoUid) { callback.onSaveError(resultMessages.getDeletePhotoErrorMessage()) }

    // Call this method to delete photo
    fun deletePhoto(photoUid: String) {
        this.photoUid = photoUid
        execute()
    }
}