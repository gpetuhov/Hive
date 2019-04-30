package com.gpetuhov.android.hive.interactors

import com.gpetuhov.android.hive.domain.interactor.SaveInstagramInteractor
import com.gpetuhov.android.hive.interactors.base.SaveUserPropertyInteractorTest
import com.gpetuhov.android.hive.utils.Constants
import com.gpetuhov.android.hive.utils.TestRepository
import org.junit.Before
import org.junit.Test

class SaveInstagramInteractorTest : SaveUserPropertyInteractorTest() {

    @Before
    fun init() = initTest { it.inject(this) }

    @Test
    fun saveInstagramSuccess() = testSaveInstagramInteractor(true)

    @Test
    fun saveInstagramError() = testSaveInstagramInteractor(false)

    private fun testSaveInstagramInteractor(isSuccess: Boolean) {
        testInteractor(
            SaveInstagramInteractor(getCallback(Constants.SAVE_INSTAGRAM_ERROR)),
            Constants.DUMMY_INSTAGRAM,
            isSuccess
        ) { (repo as TestRepository).instagram }
    }
}