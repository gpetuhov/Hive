package com.gpetuhov.android.hive.domain.interactor

import com.gpetuhov.android.hive.application.HiveApp
import com.gpetuhov.android.hive.domain.repository.Repo
import com.gpetuhov.android.hive.domain.util.ResultMessages
import javax.inject.Inject

class DeleteUserPhotoInteractor(private val callback: Callback) : Interactor {

    interface Callback {
        fun onDeletePhotoError(errorMessage: String)
    }

    @Inject lateinit var repo: Repo
    @Inject lateinit var resultMessages: ResultMessages

    private var photoUid = ""

    init {
        HiveApp.appComponent.inject(this)
    }

    // Do not call this directly!
    override fun execute() = repo.deleteUserPhoto(photoUid) { callback.onDeletePhotoError(resultMessages.getDeletePhotoErrorMessage()) }

    // Call this method to delete photo
    fun deletePhoto(photoUid: String) {
        this.photoUid = photoUid
        execute()
    }
}