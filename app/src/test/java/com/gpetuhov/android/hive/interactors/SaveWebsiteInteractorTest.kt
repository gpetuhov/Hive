package com.gpetuhov.android.hive.interactors

import com.gpetuhov.android.hive.domain.interactor.SaveWebsiteInteractor
import com.gpetuhov.android.hive.interactors.base.SaveUserPropertyInteractorTest
import com.gpetuhov.android.hive.utils.Constants
import com.gpetuhov.android.hive.utils.TestRepository
import org.junit.Before
import org.junit.Test

class SaveWebsiteInteractorTest : SaveUserPropertyInteractorTest() {

    @Before
    fun init() = initTest { it.inject(this) }

    @Test
    fun saveWebsiteSuccess() = testSaveWebsiteInteractor(true)

    @Test
    fun saveWebsiteError() = testSaveWebsiteInteractor(false)

    private fun testSaveWebsiteInteractor(isSuccess: Boolean) {
        testInteractor(
            SaveWebsiteInteractor(getCallback(Constants.SAVE_WEBSITE_ERROR)),
            Constants.DUMMY_WEBSITE,
            isSuccess
        ) { (repo as TestRepository).website }
    }
}