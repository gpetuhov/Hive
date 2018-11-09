package com.gpetuhov.android.hive.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.gpetuhov.android.hive.R
import com.gpetuhov.android.hive.application.HiveApp
import com.gpetuhov.android.hive.databinding.FragmentChatroomsBinding
import com.gpetuhov.android.hive.domain.model.Chatroom
import com.gpetuhov.android.hive.domain.repository.Repo
import com.gpetuhov.android.hive.ui.adapter.ChatroomsAdapter
import com.gpetuhov.android.hive.ui.recycler.SimpleItemDecoration
import com.gpetuhov.android.hive.ui.viewmodel.ChatroomsViewModel
import com.pawegio.kandroid.toast
import kotlinx.android.synthetic.main.fragment_chatrooms.*
import javax.inject.Inject

class ChatroomsFragment : Fragment(), ChatroomsAdapter.Callback {

    @Inject lateinit var repo: Repo

    private val chatroomsAdapter = ChatroomsAdapter(this)
    private var binding: FragmentChatroomsBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        HiveApp.appComponent.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_chatrooms, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        chatrooms.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        chatrooms.adapter = chatroomsAdapter

        // Add divider between items
        val dividerItemDecoration = SimpleItemDecoration(context?.let { ContextCompat.getDrawable(it, R.drawable.item_divider) })
        chatrooms.addItemDecoration(dividerItemDecoration)

        val viewModel = ViewModelProviders.of(this).get(ChatroomsViewModel::class.java)
        viewModel.chatrooms.observe(this, Observer<MutableList<Chatroom>> { chatroomList ->
            chatroomsAdapter.setChatrooms(chatroomList)
            binding?.chatroomListEmpty = chatroomList.isEmpty()
        })
    }

    override fun onResume() {
        super.onResume()
        repo.startGettingChatroomsUpdates()
    }

    override fun onPause() {
        super.onPause()
        repo.stopGettingChatroomsUpdates()
    }

    // === ChatroomsAdapter.Callback ===

    override fun onChatroomClick(chatroom: Chatroom?) {
        toast("Chatroom with ${chatroom?.secondUserName}")
    }
}