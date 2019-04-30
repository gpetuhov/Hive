package com.gpetuhov.android.hive.interactors

import com.gpetuhov.android.hive.domain.interactor.SaveEmailInteractor
import com.gpetuhov.android.hive.interactors.base.SaveUserPropertyInteractorTest
import com.gpetuhov.android.hive.utils.Constants
import com.gpetuhov.android.hive.utils.TestRepository
import org.junit.Before
import org.junit.Test

class SaveEmailInteractorTest : SaveUserPropertyInteractorTest() {

    @Before
    fun init() = initTest { it.inject(this) }

    @Test
    fun saveEmailSuccess() = testSaveEmailInteractor(true)

    @Test
    fun saveEmailError() = testSaveEmailInteractor(false)

    private fun testSaveEmailInteractor(isSuccess: Boolean) {
        testInteractor(
            SaveEmailInteractor(getCallback(Constants.SAVE_EMAIL_ERROR)),
            Constants.DUMMY_EMAIL,
            isSuccess
        ) { (repo as TestRepository).email }
    }
}