package com.gpetuhov.android.hive.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.gpetuhov.android.hive.application.HiveApp
import com.gpetuhov.android.hive.domain.model.User
import com.gpetuhov.android.hive.domain.repository.Repo
import javax.inject.Inject

// Provides result list to the UI
class SearchResultViewModel : ViewModel() {

    @Inject lateinit var repo: Repo

    val resultList: LiveData<MutableList<User>>

    init {
        HiveApp.appComponent.inject(this)
        resultList = repo.resultList()
    }
}