package com.gpetuhov.android.hive

import com.gpetuhov.android.hive.domain.interactor.SaveStatusInteractor
import com.gpetuhov.android.hive.interactors.base.SaveUserPropertyInteractorTest
import com.gpetuhov.android.hive.utils.Constants
import com.gpetuhov.android.hive.utils.TestRepository
import org.junit.Before
import org.junit.Test

class SaveStatusInteractorTest : SaveUserPropertyInteractorTest() {

    @Before
    fun init() = initTest { it.inject(this) }

    @Test
    fun saveStatusSuccess() = testSaveStatusInteractor(true)

    @Test
    fun saveStatusError() = testSaveStatusInteractor(false)

    private fun testSaveStatusInteractor(isSuccess: Boolean) {
        testInteractor(
            SaveStatusInteractor(getCallback(Constants.SAVE_STATUS_ERROR)),
            Constants.DUMMY_STATUS,
            isSuccess
        ) { (repo as TestRepository).status }
    }
}