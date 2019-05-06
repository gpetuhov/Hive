package com.gpetuhov.android.hive.interactors

import com.gpetuhov.android.hive.domain.interactor.SaveEducationInteractor
import com.gpetuhov.android.hive.interactors.base.SaveUserPropertyInteractorTest
import com.gpetuhov.android.hive.utils.Constants
import com.gpetuhov.android.hive.utils.TestRepository
import org.junit.Before
import org.junit.Test

class SaveEducationInteractorTest : SaveUserPropertyInteractorTest() {

    @Before
    fun init() = initTest { it.inject(this) }

    @Test
    fun saveEducationSuccess() = testSaveEducationInteractor(true)

    @Test
    fun saveEducationError() = testSaveEducationInteractor(false)

    private fun testSaveEducationInteractor(isSuccess: Boolean) {
        testInteractor(
            SaveEducationInteractor(getCallback(Constants.SAVE_EDUCATION_ERROR)),
            Constants.DUMMY_EDUCATION,
            isSuccess
        ) { (repo as TestRepository).education }
    }
}