package com.gpetuhov.android.hive.interactors

import android.content.Context
import com.gpetuhov.android.hive.application.HiveApp
import com.gpetuhov.android.hive.domain.interactor.SearchInteractor
import com.gpetuhov.android.hive.utils.dagger.components.DaggerTestAppComponent
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import javax.inject.Inject

class SearchInteractorTest {

    @Inject lateinit var context: Context

    @Before
    fun init() {
        val testAppComponent = DaggerTestAppComponent.builder().build()
        testAppComponent.inject(this)

        HiveApp.application = context as HiveApp
        HiveApp.appComponent = testAppComponent
    }

    @Test
    fun search() {
        var completeCounter = 0

        val callback = object : SearchInteractor.Callback {
            override fun onSearchComplete() {
                completeCounter++
            }
        }

        val interactor = SearchInteractor(callback)
        interactor.search("something")

        assertEquals(1, completeCounter)
    }
}