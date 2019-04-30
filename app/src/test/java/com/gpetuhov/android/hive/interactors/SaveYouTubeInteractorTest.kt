package com.gpetuhov.android.hive.interactors

import com.gpetuhov.android.hive.domain.interactor.SaveYouTubeInteractor
import com.gpetuhov.android.hive.interactors.base.SaveUserPropertyInteractorTest
import com.gpetuhov.android.hive.utils.Constants
import com.gpetuhov.android.hive.utils.TestRepository
import org.junit.Before
import org.junit.Test

class SaveYouTubeInteractorTest : SaveUserPropertyInteractorTest() {

    @Before
    fun init() = initTest { it.inject(this) }

    @Test
    fun saveYouTubeSuccess() = testSaveYouTubeInteractor(true)

    @Test
    fun saveYouTubeError() = testSaveYouTubeInteractor(false)

    private fun testSaveYouTubeInteractor(isSuccess: Boolean) {
        testInteractor(
            SaveYouTubeInteractor(getCallback(Constants.SAVE_YOUTUBE_ERROR)),
            Constants.DUMMY_YOUTUBE,
            isSuccess
        ) { (repo as TestRepository).youtube }
    }
}