package com.gpetuhov.android.hive.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.gpetuhov.android.hive.application.HiveApp
import com.gpetuhov.android.hive.domain.model.User
import com.gpetuhov.android.hive.domain.repository.Repo
import javax.inject.Inject

class ChatArchiveViewModel : ViewModel() {

    @Inject lateinit var repo: Repo

    var secondUser: LiveData<User>

    init {
        HiveApp.appComponent.inject(this)
        secondUser = repo.secondUser()
    }
}