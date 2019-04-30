package com.gpetuhov.android.hive.interactors.base

import android.content.Context
import com.gpetuhov.android.hive.application.HiveApp
import com.gpetuhov.android.hive.domain.interactor.base.SaveUserPropertyInteractor
import com.gpetuhov.android.hive.domain.repository.Repo
import com.gpetuhov.android.hive.utils.TestRepository
import com.gpetuhov.android.hive.utils.dagger.components.DaggerTestAppComponent
import com.gpetuhov.android.hive.utils.dagger.components.TestAppComponent
import org.junit.Assert
import javax.inject.Inject

open class SaveUserPropertyInteractorTest {

    @Inject lateinit var context: Context
    @Inject lateinit var repo: Repo

    private var errorCounter = 0

    protected fun initTest(inject: (TestAppComponent) -> Unit) {
        val testAppComponent = DaggerTestAppComponent.builder().build()
        inject(testAppComponent)

        HiveApp.application = context as HiveApp
        HiveApp.appComponent = testAppComponent
    }

    protected fun testInteractor(
        interactor: SaveUserPropertyInteractor,
        newPropertyValue: String,
        isSuccess: Boolean,
        getSavedValue: () -> String
    ) {
        (repo as TestRepository).isSuccess = isSuccess

        errorCounter = 0

        interactor.save(newPropertyValue)

        Assert.assertEquals(if (isSuccess) newPropertyValue else "", getSavedValue())
        Assert.assertEquals(if (isSuccess) 0 else 1, errorCounter)
    }

    protected fun getCallback(expectedErrorMessage: String): SaveUserPropertyInteractor.Callback {
        return object : SaveUserPropertyInteractor.Callback {
            override fun onSaveError(errorMessage: String) {
                errorCounter++
                Assert.assertEquals(expectedErrorMessage, errorMessage)
            }
        }
    }
}