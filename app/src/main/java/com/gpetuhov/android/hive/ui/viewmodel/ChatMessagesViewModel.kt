package com.gpetuhov.android.hive.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.gpetuhov.android.hive.application.HiveApp
import com.gpetuhov.android.hive.domain.model.Message
import com.gpetuhov.android.hive.domain.repository.Repo
import javax.inject.Inject

class ChatMessagesViewModel : ViewModel() {

    @Inject lateinit var repo: Repo

    val messages: LiveData<MutableList<Message>>

    init {
        HiveApp.appComponent.inject(this)
        messages = repo.messages()
    }
}