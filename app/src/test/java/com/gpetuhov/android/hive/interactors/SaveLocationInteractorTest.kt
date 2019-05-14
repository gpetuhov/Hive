package com.gpetuhov.android.hive.interactors

import com.gpetuhov.android.hive.domain.interactor.SaveLocationInteractor
import com.gpetuhov.android.hive.interactors.base.SaveUserPropertyInteractorTest
import com.gpetuhov.android.hive.utils.Constants
import com.gpetuhov.android.hive.utils.TestRepository
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class SaveLocationInteractorTest : SaveUserPropertyInteractorTest() {

    @Before
    fun init() = initTest { it.inject(this) }

    @Test
    fun saveLocationSuccess() = testSaveLocationInteractor(true)

    @Test
    fun saveLocationError() = testSaveLocationInteractor(false)

    private fun testSaveLocationInteractor(isSuccess: Boolean) {
        (repo as TestRepository).isSuccess = isSuccess

        val interactor = SaveLocationInteractor()
        interactor.saveLocation(Constants.DUMMY_LOCATION)

        val expectedResult = if (isSuccess) Constants.DUMMY_LOCATION else com.gpetuhov.android.hive.util.Constants.Map.DEFAULT_LOCATION
        Assert.assertEquals(expectedResult, (repo as TestRepository).location)
    }
}