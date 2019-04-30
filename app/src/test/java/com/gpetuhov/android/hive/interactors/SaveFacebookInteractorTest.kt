package com.gpetuhov.android.hive.interactors

import com.gpetuhov.android.hive.domain.interactor.SaveFacebookInteractor
import com.gpetuhov.android.hive.interactors.base.SaveUserPropertyInteractorTest
import com.gpetuhov.android.hive.utils.Constants
import com.gpetuhov.android.hive.utils.TestRepository
import org.junit.Before
import org.junit.Test

class SaveFacebookInteractorTest : SaveUserPropertyInteractorTest() {

    @Before
    fun init() = initTest { it.inject(this) }

    @Test
    fun saveFacebookSuccess() = testSaveFacebookInteractor(true)

    @Test
    fun saveFacebookError() = testSaveFacebookInteractor(false)

    private fun testSaveFacebookInteractor(isSuccess: Boolean) {
        testInteractor(
            SaveFacebookInteractor(getCallback(Constants.SAVE_FACEBOOK_ERROR)),
            Constants.DUMMY_FACEBOOK,
            isSuccess
        ) { (repo as TestRepository).facebook }
    }
}