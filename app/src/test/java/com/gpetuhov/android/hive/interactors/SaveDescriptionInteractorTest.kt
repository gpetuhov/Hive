package com.gpetuhov.android.hive.interactors

import com.gpetuhov.android.hive.domain.interactor.SaveDescriptionInteractor
import com.gpetuhov.android.hive.interactors.base.SaveUserPropertyInteractorTest
import com.gpetuhov.android.hive.utils.Constants
import com.gpetuhov.android.hive.utils.TestRepository
import org.junit.Before
import org.junit.Test

class SaveDescriptionInteractorTest : SaveUserPropertyInteractorTest() {

    @Before
    fun init() = initTest { it.inject(this) }

    @Test
    fun saveDescriptionSuccess() = testSaveDescriptionInteractor(true)

    @Test
    fun saveDescriptionError() = testSaveDescriptionInteractor(false)

    private fun testSaveDescriptionInteractor(isSuccess: Boolean) {
        testInteractor(
            SaveDescriptionInteractor(getCallback(Constants.SAVE_DESCRIPTION_ERROR)),
            Constants.DUMMY_DESCRIPTION,
            isSuccess
        ) { (repo as TestRepository).description }
    }
}