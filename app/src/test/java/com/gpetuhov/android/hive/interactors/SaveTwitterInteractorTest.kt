package com.gpetuhov.android.hive.interactors

import com.gpetuhov.android.hive.domain.interactor.SaveTwitterInteractor
import com.gpetuhov.android.hive.interactors.base.SaveUserPropertyInteractorTest
import com.gpetuhov.android.hive.utils.Constants
import com.gpetuhov.android.hive.utils.TestRepository
import org.junit.Before
import org.junit.Test

class SaveTwitterInteractorTest : SaveUserPropertyInteractorTest() {

    @Before
    fun init() = initTest { it.inject(this) }

    @Test
    fun saveTwitterSuccess() = testSaveTwitterInteractor(true)

    @Test
    fun saveTwitterError() = testSaveTwitterInteractor(false)

    private fun testSaveTwitterInteractor(isSuccess: Boolean) {
        testInteractor(
            SaveTwitterInteractor(getCallback(Constants.SAVE_TWITTER_ERROR)),
            Constants.DUMMY_TWITTER,
            isSuccess
        ) { (repo as TestRepository).twitter }
    }
}