package com.gpetuhov.android.hive.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gpetuhov.android.hive.R
import com.gpetuhov.android.hive.domain.model.Chatroom
import com.gpetuhov.android.hive.ui.adapter.ChatroomsAdapter
import com.gpetuhov.android.hive.ui.viewmodel.ChatroomsViewModel
import kotlinx.android.synthetic.main.fragment_chatrooms.*

class ChatroomsFragment : Fragment() {

    private val chatroomsAdapter = ChatroomsAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_chatrooms, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        chatrooms.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        chatrooms.adapter = chatroomsAdapter

        val viewModel = ViewModelProviders.of(this).get(ChatroomsViewModel::class.java)
        viewModel.chatrooms.observe(this, Observer<MutableList<Chatroom>> { chatroomList ->
            chatroomsAdapter.setChatrooms(chatroomList)
        })
    }
}