package com.gpetuhov.android.hive.ui.adapter

import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.gpetuhov.android.hive.R
import com.gpetuhov.android.hive.databinding.ItemMessageBinding
import com.gpetuhov.android.hive.domain.model.Message
import kotlinx.android.synthetic.main.item_message.view.*

class MessagesAdapter : RecyclerView.Adapter<MessagesAdapter.MessageViewHolder>() {

    private var messageList = mutableListOf<Message>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: ItemMessageBinding = DataBindingUtil.inflate(layoutInflater, R.layout.item_message, parent, false)
        return MessageViewHolder(binding)
    }

    override fun getItemCount() = messageList.size

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) = holder.bindMessage(messageList[position])

    // === Public methods ===

    fun setMessages(messages: MutableList<Message>) {
        messageList.clear()
        messageList.addAll(messages)
        notifyDataSetChanged()
    }

    // === Inner classes ===

    class MessageViewHolder(itemBinding: ItemMessageBinding) : RecyclerView.ViewHolder(itemBinding.root) {

        private var view = itemBinding.root
        private var binding: ItemMessageBinding = itemBinding

        fun bindMessage(message: Message) {
            binding.message = message

            view.apply {
                // Set message layout depending on message sender (current user or not)
                message_root_layout.gravity = if (message.isFromCurrentUser) Gravity.END else Gravity.START
                message_left_space.visibility = if (message.isFromCurrentUser) View.VISIBLE else View.GONE
                message_right_space.visibility = if (message.isFromCurrentUser) View.GONE else View.VISIBLE
                item_message_wrapper.setBackgroundResource(
                    if (message.isFromCurrentUser) R.drawable.message_background_current_user else R.drawable.message_background
                )
            }
        }
    }
}