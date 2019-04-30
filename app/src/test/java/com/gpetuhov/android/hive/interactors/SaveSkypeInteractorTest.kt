package com.gpetuhov.android.hive.interactors

import com.gpetuhov.android.hive.domain.interactor.SaveSkypeInteractor
import com.gpetuhov.android.hive.interactors.base.SaveUserPropertyInteractorTest
import com.gpetuhov.android.hive.utils.Constants
import com.gpetuhov.android.hive.utils.TestRepository
import org.junit.Before
import org.junit.Test

class SaveSkypeInteractorTest : SaveUserPropertyInteractorTest() {

    @Before
    fun init() = initTest { it.inject(this) }

    @Test
    fun saveSkypeSuccess() = testSaveSkypeInteractor(true)

    @Test
    fun saveSkypeError() = testSaveSkypeInteractor(false)

    private fun testSaveSkypeInteractor(isSuccess: Boolean) {
        testInteractor(
            SaveSkypeInteractor(getCallback(Constants.SAVE_SKYPE_ERROR)),
            Constants.DUMMY_SKYPE,
            isSuccess
        ) { (repo as TestRepository).skype }
    }
}