package com.gpetuhov.android.hive.interactors

import com.gpetuhov.android.hive.domain.interactor.SaveHiveRunningInteractor
import com.gpetuhov.android.hive.interactors.base.SaveUserPropertyInteractorTest
import com.gpetuhov.android.hive.utils.TestRepository
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class SaveHiveRunningInteractorTest : SaveUserPropertyInteractorTest() {

    @Before
    fun init() = initTest { it.inject(this) }

    @Test
    fun saveSuccess() = testInteractor(true)

    @Test
    fun saveError() = testInteractor(false)

    private fun testInteractor(isSuccess: Boolean) {
        (repo as TestRepository).isSuccess = isSuccess

        val interactor = SaveHiveRunningInteractor()
        interactor.saveHiveRunning(true)

        Assert.assertEquals(isSuccess, (repo as TestRepository).isHiveRunning)
    }
}