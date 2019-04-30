package com.gpetuhov.android.hive.interactors

import com.gpetuhov.android.hive.domain.interactor.SaveResidenceInteractor
import com.gpetuhov.android.hive.interactors.base.SaveUserPropertyInteractorTest
import com.gpetuhov.android.hive.utils.Constants
import com.gpetuhov.android.hive.utils.TestRepository
import org.junit.Before
import org.junit.Test

class SaveResidenceInteractorTest : SaveUserPropertyInteractorTest() {

    @Before
    fun init() = initTest { it.inject(this) }

    @Test
    fun saveResidenceSuccess() = testSaveResidenceInteractor(true)

    @Test
    fun saveResidenceError() = testSaveResidenceInteractor(false)

    private fun testSaveResidenceInteractor(isSuccess: Boolean) {
        testInteractor(
            SaveResidenceInteractor(getCallback(Constants.SAVE_RESIDENCE_ERROR)),
            Constants.DUMMY_RESIDENCE,
            isSuccess
        ) { (repo as TestRepository).residence }
    }
}