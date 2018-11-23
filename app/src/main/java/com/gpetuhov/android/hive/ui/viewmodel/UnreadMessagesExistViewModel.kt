package com.gpetuhov.android.hive.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.gpetuhov.android.hive.application.HiveApp
import com.gpetuhov.android.hive.domain.repository.Repo
import javax.inject.Inject

class UnreadMessagesExistViewModel : ViewModel() {

    @Inject lateinit var repo: Repo

    var unreadMessagesExist: LiveData<Boolean>

    init {
        HiveApp.appComponent.inject(this)
        unreadMessagesExist = repo.unreadMessagesExist()
    }
}