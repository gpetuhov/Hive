package com.gpetuhov.android.hive.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.gpetuhov.android.hive.application.HiveApp
import com.gpetuhov.android.hive.domain.model.Offer
import com.gpetuhov.android.hive.domain.repository.Repo
import javax.inject.Inject

class FavoriteOffersViewModel : ViewModel() {

    @Inject lateinit var repo: Repo

    val favoriteOffers: LiveData<MutableList<Offer>>

    init {
        HiveApp.appComponent.inject(this)
        favoriteOffers = repo.favoriteOffers()
    }
}