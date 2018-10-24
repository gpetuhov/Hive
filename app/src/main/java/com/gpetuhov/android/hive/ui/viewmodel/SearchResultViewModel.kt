package com.gpetuhov.android.hive.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.gpetuhov.android.hive.application.HiveApp
import com.gpetuhov.android.hive.model.User
import com.gpetuhov.android.hive.repository.Repository
import javax.inject.Inject

// Provides result list to the UI
class SearchResultViewModel : ViewModel() {

    @Inject lateinit var repo: Repository

    val resultList: LiveData<MutableList<User>>

    init {
        HiveApp.appComponent.inject(this)
        resultList = repo.resultList
    }
}