package com.gpetuhov.android.hive.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.gpetuhov.android.hive.application.HiveApp
import com.gpetuhov.android.hive.domain.model.User
import com.gpetuhov.android.hive.domain.repository.Repo
import javax.inject.Inject

// Provides user details for the DetailsFragment
class SearchUserDetailsViewModel : ViewModel() {

    @Inject lateinit var repo: Repo

    var searchUserDetails: LiveData<User>

    init {
        HiveApp.appComponent.inject(this)
        searchUserDetails = repo.secondUser()
    }

    fun getFirstUpdate(uid: String) = repo.initSearchUserDetails(uid)
}