package com.gpetuhov.android.hive.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.gpetuhov.android.hive.R
import com.gpetuhov.android.hive.databinding.ItemMessageBinding
import com.gpetuhov.android.hive.domain.model.Message
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListUpdateCallback
import kotlinx.coroutines.*

class MessagesAdapter(private var callback: Callback) : RecyclerView.Adapter<MessagesAdapter.MessageViewHolder>() {

    interface Callback {
        fun onMessagesUpdated()
    }

    private var messageList = mutableListOf<Message>()
    private var calculateDiffJob: Job? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: ItemMessageBinding = DataBindingUtil.inflate(layoutInflater, R.layout.item_message, parent, false)
        return MessageViewHolder(binding)
    }

    override fun getItemCount() = messageList.size

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        holder.binding.message = messageList[position]
        holder.binding.executePendingBindings() // This line is important, it will force to load the variable in a custom view
    }

    // === Public methods ===

    fun setMessages(messages: MutableList<Message>) {
        // Cancel previously started coroutine
        calculateDiffJob?.cancel()

        calculateDiffJob = GlobalScope.launch {
            // Calculate diff result in background
            val diffResult = DiffUtil.calculateDiff(DiffCallback(messages, messageList))

            GlobalScope.launch(Dispatchers.Main) {
                // Dispatch updates on the main thread

                // The backing data must be updated at the same time with notifying the adapter about the changes
                messageList.clear()
                messageList.addAll(messages)

                diffResult.dispatchUpdatesTo(object : ListUpdateCallback{
                    override fun onChanged(position: Int, count: Int, payload: Any?) = notifyItemRangeChanged(position, count, payload)
                    override fun onMoved(fromPosition: Int, toPosition: Int) = notifyItemMoved(fromPosition, toPosition)
                    override fun onInserted(position: Int, count: Int) = notifyItemRangeInserted(position, count)
                    override fun onRemoved(position: Int, count: Int) = notifyItemRangeRemoved(position, count)
                })

                callback.onMessagesUpdated()
            }
        }
    }

    // === Inner classes ===

    class MessageViewHolder(var binding: ItemMessageBinding) : RecyclerView.ViewHolder(binding.root)

    // This class is needed to calculate difference between old and new lists of messages
    // (to minimize RecyclerView updates)
    class DiffCallback(
        var newMessages: List<Message>,
        var oldMessages: List<Message>
    ) : DiffUtil.Callback() {

        override fun getOldListSize() = oldMessages.size

        override fun getNewListSize() = newMessages.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
            oldMessages[oldItemPosition].uid == newMessages[newItemPosition].uid

        // This is called if areItemsTheSame() returns true
        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
            oldMessages[oldItemPosition].text == newMessages[newItemPosition].text
    }
}