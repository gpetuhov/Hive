package com.gpetuhov.android.hive.interactors

import com.gpetuhov.android.hive.domain.interactor.SaveWeblinkInteractor
import com.gpetuhov.android.hive.interactors.base.SaveUserPropertyInteractorTest
import com.gpetuhov.android.hive.utils.Constants
import com.gpetuhov.android.hive.utils.TestRepository
import org.junit.Before
import org.junit.Test

class SaveWeblinkInteractorTest : SaveUserPropertyInteractorTest() {

    @Before
    fun init() = initTest { it.inject(this) }

    @Test
    fun saveWeblinkSuccess() = testSaveWeblinkInteractor(true)

    @Test
    fun saveWeblinkError() = testSaveWeblinkInteractor(false)

    private fun testSaveWeblinkInteractor(isSuccess: Boolean) {
        testInteractor(
            SaveWeblinkInteractor(getCallback(Constants.SAVE_WEBLINK_ERROR)),
            Constants.DUMMY_WEBLINK,
            isSuccess
        ) { (repo as TestRepository).weblink }
    }
}