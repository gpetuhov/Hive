package com.gpetuhov.android.hive.interactors

import com.gpetuhov.android.hive.domain.interactor.SaveWorkInteractor
import com.gpetuhov.android.hive.interactors.base.SaveUserPropertyInteractorTest
import com.gpetuhov.android.hive.utils.Constants
import com.gpetuhov.android.hive.utils.TestRepository
import org.junit.Before
import org.junit.Test

class SaveWorkInteractorTest : SaveUserPropertyInteractorTest() {

    @Before
    fun init() = initTest { it.inject(this) }

    @Test
    fun saveWorkSuccess() = testSaveWorkInteractor(true)

    @Test
    fun saveWorkError() = testSaveWorkInteractor(false)

    private fun testSaveWorkInteractor(isSuccess: Boolean) {
        testInteractor(
            SaveWorkInteractor(getCallback(Constants.SAVE_WORK_ERROR)),
            Constants.DUMMY_WORK,
            isSuccess
        ) { (repo as TestRepository).work }
    }
}