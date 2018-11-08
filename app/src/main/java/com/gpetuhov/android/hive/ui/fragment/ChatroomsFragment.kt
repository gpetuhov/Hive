package com.gpetuhov.android.hive.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gpetuhov.android.hive.R
import com.gpetuhov.android.hive.ui.adapter.ChatroomsAdapter
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
    }
}