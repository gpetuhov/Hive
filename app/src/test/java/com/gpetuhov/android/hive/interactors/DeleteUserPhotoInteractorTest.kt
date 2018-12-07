package com.gpetuhov.android.hive.interactors

import android.content.Context
import com.gpetuhov.android.hive.application.HiveApp
import com.gpetuhov.android.hive.domain.interactor.DeleteUserPhotoInteractor
import com.gpetuhov.android.hive.domain.repository.Repo
import com.gpetuhov.android.hive.utils.Constants
import com.gpetuhov.android.hive.utils.TestRepository
import com.gpetuhov.android.hive.utils.dagger.components.DaggerTestAppComponent
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import javax.inject.Inject

class DeleteUserPhotoInteractorTest {

    @Inject lateinit var context: Context
    @Inject lateinit var repo: Repo

    @Before
    fun init() {
        val testAppComponent = DaggerTestAppComponent.builder().build()
        testAppComponent.inject(this)

        HiveApp.application = context as HiveApp
        HiveApp.appComponent = testAppComponent

        (repo as TestRepository).photoList.add(Constants.DUMMY_PHOTO)
    }

    @Test
    fun deletePhotoSuccess() {
        testDeleteUserPhotoInteractor(true)
    }

    @Test
    fun deletePhotoError() {
        testDeleteUserPhotoInteractor(false)
    }

    private fun testDeleteUserPhotoInteractor(isSuccess: Boolean) {
        (repo as TestRepository).isSuccess = isSuccess

        var errorCounter = 0

        val callback = object : DeleteUserPhotoInteractor.Callback {
            override fun onDeletePhotoError(errorMessage: String) {
                errorCounter++
                assertEquals(Constants.DELETE_PHOTO_ERROR, errorMessage)
            }
        }

        val interactor = DeleteUserPhotoInteractor(callback)
        interactor.deletePhoto(Constants.DUMMY_PHOTO.uid)

        assertEquals(isSuccess, (repo as TestRepository).photoList.isEmpty())
        assertEquals(if (isSuccess) 0 else 1, errorCounter)
    }
}