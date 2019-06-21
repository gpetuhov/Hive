package com.gpetuhov.android.hive.interactors

import android.content.Context
import com.gpetuhov.android.hive.application.HiveApp
import com.gpetuhov.android.hive.domain.interactor.DeleteUserPicInteractor
import com.gpetuhov.android.hive.domain.interactor.base.SaveUserPropertyInteractor
import com.gpetuhov.android.hive.domain.repository.Repo
import com.gpetuhov.android.hive.utils.Constants
import com.gpetuhov.android.hive.utils.TestRepository
import com.gpetuhov.android.hive.utils.dagger.components.DaggerTestAppComponent
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import javax.inject.Inject

class DeleteUserPicInteractorTest {

    @Inject lateinit var context: Context
    @Inject lateinit var repo: Repo

    @Before
    fun init() {
        val testAppComponent = DaggerTestAppComponent.builder().build()
        testAppComponent.inject(this)

        HiveApp.application = context as HiveApp
        HiveApp.appComponent = testAppComponent

        (repo as TestRepository).userPicUrl = "some_url"
    }

    @Test
    fun deletePhotoSuccess() = testDeleteUserPicInteractor(true)

    @Test
    fun deletePhotoError() = testDeleteUserPicInteractor(false)

    private fun testDeleteUserPicInteractor(isSuccess: Boolean) {
        (repo as TestRepository).isSuccess = isSuccess

        var errorCounter = 0

        val callback = object : SaveUserPropertyInteractor.Callback {
            override fun onSaveError(errorMessage: String) {
                errorCounter++
                assertEquals(Constants.DELETE_USER_PIC_ERROR, errorMessage)
            }
        }

        val interactor = DeleteUserPicInteractor(callback)
        interactor.execute()

        assertEquals(isSuccess, (repo as TestRepository).userPicUrl.isEmpty())
        assertEquals(if (isSuccess) 0 else 1, errorCounter)
    }
}