package com.gpetuhov.android.hive.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.gpetuhov.android.hive.application.HiveApp
import com.gpetuhov.android.hive.domain.model.User
import com.gpetuhov.android.hive.domain.repository.Repo
import javax.inject.Inject

// Provides user details for the UserDetailsFragment
class UserDetailsViewModel : ViewModel() {

    @Inject lateinit var repo: Repo

    var userDetails: LiveData<User>

    init {
        HiveApp.appComponent.inject(this)
        userDetails = repo.secondUser()
    }
}