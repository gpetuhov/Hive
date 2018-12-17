package com.gpetuhov.android.hive.interactors

import android.content.Context
import com.gpetuhov.android.hive.application.HiveApp
import com.gpetuhov.android.hive.domain.interactor.FavoritesInteractor
import com.gpetuhov.android.hive.domain.interactor.SaveUsernameInteractor
import com.gpetuhov.android.hive.domain.repository.Repo
import com.gpetuhov.android.hive.utils.Constants
import com.gpetuhov.android.hive.utils.TestRepository
import com.gpetuhov.android.hive.utils.dagger.components.DaggerTestAppComponent
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import javax.inject.Inject

class FavoritesInteractorTest {

    @Inject lateinit var context: Context
    @Inject lateinit var repo: Repo

    @Before
    fun init() {
        val testAppComponent = DaggerTestAppComponent.builder().build()
        testAppComponent.inject(this)

        HiveApp.application = context as HiveApp
        HiveApp.appComponent = testAppComponent
    }

    @Test
    fun addFavoriteSuccess() {
        testFavoritesInteractor(false, true)
    }

    @Test
    fun addFavoriteError() {
        testFavoritesInteractor(false, false)
    }

    private fun testFavoritesInteractor(isFavorite: Boolean, isSuccess: Boolean) {
        (repo as TestRepository).isSuccess = isSuccess

        var errorCounter = 0

        val callback = object : FavoritesInteractor.Callback {
            override fun onFavoritesError(errorMessage: String) {
                errorCounter++
                assertEquals(if (isFavorite) Constants.REMOVE_FAVORITE_ERROR else Constants.ADD_FAVORITE_ERROR, errorMessage)
            }
        }

        val interactor = FavoritesInteractor(callback)
        interactor.favorite(isFavorite, "5g2kj54", "")

        assertEquals(isSuccess, !(repo as TestRepository).favoriteList.isEmpty())
        assertEquals(if (isSuccess) 0 else 1, errorCounter)
    }
}