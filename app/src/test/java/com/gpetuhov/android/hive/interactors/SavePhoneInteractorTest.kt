package com.gpetuhov.android.hive.interactors

import com.gpetuhov.android.hive.domain.interactor.SavePhoneInteractor
import com.gpetuhov.android.hive.interactors.base.SaveUserPropertyInteractorTest
import com.gpetuhov.android.hive.utils.Constants
import com.gpetuhov.android.hive.utils.TestRepository
import org.junit.Before
import org.junit.Test

class SavePhoneInteractorTest : SaveUserPropertyInteractorTest() {

    @Before
    fun init() = initTest { it.inject(this) }

    @Test
    fun savePhoneSuccess() = testSavePhoneInteractor(true)

    @Test
    fun savePhoneError() = testSavePhoneInteractor(false)

    private fun testSavePhoneInteractor(isSuccess: Boolean) {
        testInteractor(
            SavePhoneInteractor(getCallback(Constants.SAVE_PHONE_ERROR)),
            Constants.DUMMY_PHONE,
            isSuccess
        ) { (repo as TestRepository).phone }
    }
}