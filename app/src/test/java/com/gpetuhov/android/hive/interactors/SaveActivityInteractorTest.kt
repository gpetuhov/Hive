package com.gpetuhov.android.hive.interactors

import com.gpetuhov.android.hive.domain.interactor.SaveActivityInteractor
import com.gpetuhov.android.hive.interactors.base.SaveUserPropertyInteractorTest
import com.gpetuhov.android.hive.utils.Constants
import com.gpetuhov.android.hive.utils.TestRepository
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class SaveActivityInteractorTest : SaveUserPropertyInteractorTest() {

    @Before
    fun init() = initTest { it.inject(this) }

    @Test
    fun saveActivitySuccess() = testSaveActivityInteractor(true)

    @Test
    fun saveActivityError() = testSaveActivityInteractor(false)

    private fun testSaveActivityInteractor(isSuccess: Boolean) {
        (repo as TestRepository).isSuccess = isSuccess

        val interactor = SaveActivityInteractor()
        interactor.saveActivity(Constants.DUMMY_ACTIVITY)

        Assert.assertEquals(if (isSuccess) Constants.DUMMY_ACTIVITY else Constants.DEFAULT_ACTIVITY, (repo as TestRepository).activity)
    }
}