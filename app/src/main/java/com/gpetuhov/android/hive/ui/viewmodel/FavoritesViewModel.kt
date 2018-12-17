package com.gpetuhov.android.hive.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.gpetuhov.android.hive.application.HiveApp
import com.gpetuhov.android.hive.domain.model.Favorite
import com.gpetuhov.android.hive.domain.repository.Repo
import javax.inject.Inject

class FavoritesViewModel : ViewModel() {

    @Inject lateinit var repo: Repo

    val favorites: LiveData<MutableList<Favorite>>

    init {
        HiveApp.appComponent.inject(this)
        favorites = repo.favorites()
    }
}