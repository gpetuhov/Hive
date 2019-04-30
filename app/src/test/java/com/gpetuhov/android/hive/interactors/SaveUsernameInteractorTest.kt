package com.gpetuhov.android.hive.interactors

import com.gpetuhov.android.hive.domain.interactor.SaveUsernameInteractor
import com.gpetuhov.android.hive.interactors.base.SaveUserPropertyInteractorTest
import com.gpetuhov.android.hive.utils.Constants
import com.gpetuhov.android.hive.utils.TestRepository
import org.junit.Before
import org.junit.Test

class SaveUsernameInteractorTest : SaveUserPropertyInteractorTest() {

    @Before
    fun init() = initTest { it.inject(this) }

    @Test
    fun saveUsernameSuccess() = testSaveUsernameInteractor(true)

    @Test
    fun saveUsernameError() = testSaveUsernameInteractor(false)

    private fun testSaveUsernameInteractor(isSuccess: Boolean) {
        testInteractor(
            SaveUsernameInteractor(getCallback(Constants.SAVE_USERNAME_ERROR)),
            Constants.DUMMY_USERNAME,
            isSuccess
        ) { (repo as TestRepository).username }
    }
}