package com.gpetuhov.android.hive.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.gpetuhov.android.hive.R
import com.gpetuhov.android.hive.databinding.ItemChatroomBinding
import com.gpetuhov.android.hive.domain.model.Chatroom
import com.gpetuhov.android.hive.domain.model.User
import com.gpetuhov.android.hive.util.updateUserPic

class ChatroomsAdapter(private val callback: Callback) : RecyclerView.Adapter<ChatroomsAdapter.ChatroomViewHolder>() {

    interface Callback {
        fun openChat(secondUserUid: String, secondUserName: String)
    }

    private var chatroomList = mutableListOf<Chatroom>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatroomViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: ItemChatroomBinding = DataBindingUtil.inflate(layoutInflater, R.layout.item_chatroom, parent, false)

        binding.root.setOnClickListener {
            val chatroom = binding.chatroom

            if (chatroom != null) {
                callback.openChat(chatroom.secondUserUid, chatroom.secondUserName)
            }
        }

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

    class ChatroomViewHolder(var binding: ItemChatroomBinding) : RecyclerView.ViewHolder(binding.root) {

//        private var userPic = binding.root.findViewById<ImageView>(R.id.item_chatroom_user_pic)

//        fun bindUserPic(user: User) = updateUserPic(binding.root.context, user, userPic)
    }
}