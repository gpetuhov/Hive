package com.gpetuhov.android.hive.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.gpetuhov.android.hive.application.HiveApp
import com.gpetuhov.android.hive.domain.model.Chatroom
import com.gpetuhov.android.hive.domain.repository.Repo
import javax.inject.Inject

class ChatroomsViewModel : ViewModel() {

    @Inject lateinit var repo: Repo

    val chatrooms: LiveData<MutableList<Chatroom>>

    init {
        HiveApp.appComponent.inject(this)
        chatrooms = repo.chatrooms()
    }
}