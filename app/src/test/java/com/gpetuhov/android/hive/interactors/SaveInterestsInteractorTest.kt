package com.gpetuhov.android.hive.interactors

import com.gpetuhov.android.hive.domain.interactor.SaveInterestsInteractor
import com.gpetuhov.android.hive.interactors.base.SaveUserPropertyInteractorTest
import com.gpetuhov.android.hive.utils.Constants
import com.gpetuhov.android.hive.utils.TestRepository
import org.junit.Before
import org.junit.Test

class SaveInterestsInteractorTest : SaveUserPropertyInteractorTest() {

    @Before
    fun init() = initTest { it.inject(this) }

    @Test
    fun saveInterestsSuccess() = testSaveInterestsInteractor(true)

    @Test
    fun saveInterestsError() = testSaveInterestsInteractor(false)

    private fun testSaveInterestsInteractor(isSuccess: Boolean) {
        testInteractor(
            SaveInterestsInteractor(getCallback(Constants.SAVE_INTERESTS_ERROR)),
            Constants.DUMMY_INTERESTS,
            isSuccess
        ) { (repo as TestRepository).interests }
    }
}