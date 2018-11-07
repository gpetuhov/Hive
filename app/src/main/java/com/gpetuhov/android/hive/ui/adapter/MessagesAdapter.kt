package com.gpetuhov.android.hive.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.gpetuhov.android.hive.R
import com.gpetuhov.android.hive.databinding.ItemMessageBinding
import com.gpetuhov.android.hive.domain.model.Message

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

        private var binding: ItemMessageBinding = itemBinding

        fun bindMessage(message: Message) {
            binding.message = message
        }
    }
}