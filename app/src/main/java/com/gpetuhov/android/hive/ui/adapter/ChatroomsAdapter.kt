package com.gpetuhov.android.hive.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.gpetuhov.android.hive.R
import com.gpetuhov.android.hive.databinding.ItemChatroomBinding
import com.gpetuhov.android.hive.domain.model.Chatroom

class ChatroomsAdapter(private val callback: Callback) : RecyclerView.Adapter<ChatroomsAdapter.ChatroomViewHolder>() {

    interface Callback {
        fun onChatroomClick(chatroom: Chatroom?)
    }

    private var chatroomList = mutableListOf<Chatroom>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatroomViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: ItemChatroomBinding = DataBindingUtil.inflate(layoutInflater, R.layout.item_chatroom, parent, false)

        binding.root.setOnClickListener { callback.onChatroomClick(binding.chatroom) }

        return ChatroomViewHolder(binding)
    }

    override fun getItemCount() = chatroomList.size

    override fun onBindViewHolder(holder: ChatroomViewHolder, position: Int) {
        holder.binding.chatroom = chatroomList[position]
        holder.binding.executePendingBindings() // This line is important, it will force to load the variable in a custom view
    }

    // === Public methods ===

    fun setChatrooms(chatrooms: MutableList<Chatroom>) {
        chatroomList.clear()
        chatroomList.addAll(chatrooms)
        notifyDataSetChanged()
    }

    // === Inner classes ===

    class ChatroomViewHolder(var binding: ItemChatroomBinding) : RecyclerView.ViewHolder(binding.root)
}