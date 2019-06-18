package com.gpetuhov.android.hive.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.gpetuhov.android.hive.application.HiveApp
import com.gpetuhov.android.hive.domain.model.Message
import com.gpetuhov.android.hive.domain.repository.Repo
import javax.inject.Inject

class ChatArchiveViewModel : ViewModel() {

    @Inject lateinit var repo: Repo

    val chatArchiveMessages: LiveData<MutableList<Message>>

    init {
        HiveApp.appComponent.inject(this)
        chatArchiveMessages = repo.chatArchiveMessages()
    }
}