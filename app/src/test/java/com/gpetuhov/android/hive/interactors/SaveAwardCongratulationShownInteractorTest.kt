package com.gpetuhov.android.hive.interactors

import com.gpetuhov.android.hive.domain.interactor.SaveAwardCongratulationShownInteractor
import com.gpetuhov.android.hive.interactors.base.SaveUserPropertyInteractorTest
import com.gpetuhov.android.hive.util.Constants.Award.Companion.TEXT_MASTER
import com.gpetuhov.android.hive.utils.TestRepository
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class SaveAwardCongratulationShownInteractorTest : SaveUserPropertyInteractorTest() {

    @Before
    fun init() = initTest { it.inject(this) }

    @Test
    fun saveSuccess() = testInteractor(true)

    @Test
    fun saveError() = testInteractor(false)

    private fun testInteractor(isSuccess: Boolean) {
        (repo as TestRepository).isSuccess = isSuccess

        val interactor = SaveAwardCongratulationShownInteractor()
        val newAwardCongratulationShownList = mutableListOf<Int>()
        newAwardCongratulationShownList.add(TEXT_MASTER)
        interactor.saveAwardCongratulationShown(newAwardCongratulationShownList)

        Assert.assertEquals(isSuccess, (repo as TestRepository).awardCongratulationShownList.contains(TEXT_MASTER))
    }
}