package com.gpetuhov.android.hive.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.gpetuhov.android.hive.application.HiveApp
import com.gpetuhov.android.hive.domain.model.User
import com.gpetuhov.android.hive.repository.Repository
import javax.inject.Inject

// Provides current user data to the UI
class CurrentUserViewModel : ViewModel() {

    @Inject lateinit var repo: Repository

    val currentUser: LiveData<User>

    init {
        HiveApp.appComponent.inject(this)
        currentUser = repo.currentUser
    }
}