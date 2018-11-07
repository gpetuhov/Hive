package com.gpetuhov.android.hive.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gpetuhov.android.hive.R
import com.gpetuhov.android.hive.application.HiveApp
import com.gpetuhov.android.hive.databinding.FragmentChatBinding
import com.gpetuhov.android.hive.domain.model.Message
import com.gpetuhov.android.hive.domain.repository.Repo
import com.gpetuhov.android.hive.ui.adapter.MessagesAdapter
import com.gpetuhov.android.hive.ui.viewmodel.ChatMessagesViewModel
import kotlinx.android.synthetic.main.fragment_chat.*
import javax.inject.Inject

class ChatFragment : Fragment() {

    @Inject lateinit var repo: Repo

    private val messagesAdapter = MessagesAdapter()
    private var binding: FragmentChatBinding? = null
    private var userUid = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        HiveApp.appComponent.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_chat, container, false)
        binding?.handler = this

        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userUid = ChatFragmentArgs.fromBundle(arguments).uid
        val name = ChatFragmentArgs.fromBundle(arguments).name
        chat_user_name_text.text = name

        messages.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, true)
        messages.adapter = messagesAdapter

        val viewModel = ViewModelProviders.of(this).get(ChatMessagesViewModel::class.java)
        viewModel.messages.observe(this, Observer<MutableList<Message>> { messageList ->
            messagesAdapter.setMessages(messageList)
            messages.scrollToPosition(0)
        })
    }

    override fun onResume() {
        super.onResume()
        repo.startGettingMessagesUpdates(repo.currentUserUid(), userUid)
    }

    override fun onPause() {
        super.onPause()
        repo.stopGettingMessagesUpdates()
    }

    // === Public methods ===

    fun navigateUp() {
        findNavController().navigateUp()
    }

    fun sendMessage() {
        repo.sendMessage(message_text.text.toString()) { /* Do nothing */ }
        message_text.setText("")
    }
}