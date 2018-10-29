package com.gpetuhov.android.hive.interactors

import com.gpetuhov.android.hive.utils.dagger.components.DaggerTestAppComponent
import org.junit.Before
import org.junit.Test

class DeleteUserInteractorTest {

    @Before
    fun init() {
        val testAppComponent = DaggerTestAppComponent.builder().build()
    }

    @Test
    fun deleteUser() {

    }
}