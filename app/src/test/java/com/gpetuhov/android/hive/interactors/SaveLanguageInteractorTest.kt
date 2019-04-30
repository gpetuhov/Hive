package com.gpetuhov.android.hive.interactors

import com.gpetuhov.android.hive.domain.interactor.SaveLanguageInteractor
import com.gpetuhov.android.hive.interactors.base.SaveUserPropertyInteractorTest
import com.gpetuhov.android.hive.utils.Constants
import com.gpetuhov.android.hive.utils.TestRepository
import org.junit.Before
import org.junit.Test

class SaveLanguageInteractorTest : SaveUserPropertyInteractorTest() {

    @Before
    fun init() = initTest { it.inject(this) }

    @Test
    fun saveLanguageSuccess() = testSaveLanguageInteractor(true)

    @Test
    fun saveLanguageError() = testSaveLanguageInteractor(false)

    private fun testSaveLanguageInteractor(isSuccess: Boolean) {
        testInteractor(
            SaveLanguageInteractor(getCallback(Constants.SAVE_LANGUAGE_ERROR)),
            Constants.DUMMY_LANGUAGE,
            isSuccess
        ) { (repo as TestRepository).language }
    }
}