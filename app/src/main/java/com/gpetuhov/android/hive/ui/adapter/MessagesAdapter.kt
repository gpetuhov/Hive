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
import timber.log.Timber

class MessagesAdapter(
    private var callback: Callback,
    initialMessagesList: MutableList<Message>?
) : RecyclerView.Adapter<MessagesAdapter.MessageViewHolder>() {

    interface Callback {
        fun onMessagesUpdated(isChanged: Boolean)
    }

    private var messageList = mutableListOf<Message>()
    private var calculateDiffJob: Job? = null

    init {
        if (initialMessagesList != null) {
            messageList.addAll(initialMessagesList)
            notifyDataSetChanged()
        }
    }

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

                var changesCount = 0

                Timber.tag("MessageAdapter").d("Dispatching updates")

                diffResult.dispatchUpdatesTo(object : ListUpdateCallback{
                    override fun onChanged(position: Int, count: Int, payload: Any?) {
                        notifyItemRangeChanged(position, count, payload)
                        changesCount++
                    }

                    override fun onMoved(fromPosition: Int, toPosition: Int) {
                        notifyItemMoved(fromPosition, toPosition)
                        changesCount++
                    }

                    override fun onInserted(position: Int, count: Int) {
                        notifyItemRangeInserted(position, count)
                        changesCount++
                    }

                    override fun onRemoved(position: Int, count: Int) {
                        notifyItemRangeRemoved(position, count)
                        changesCount++
                    }
                })

                callback.onMessagesUpdated(changesCount > 0)
            }
        }
    }

    // === Inner classes ===

    class MessageViewHolder(var binding: ItemMessageBinding) : RecyclerView.ViewHolder(binding.root)

    // This class is needed to calculate difference between old and new lists of messages
    // (to minimize RecyclerView updates)
    class DiffCallback(newMessagesSource: List<Message>, oldMessagesSource: List<Message>) : DiffUtil.Callback() {

        private var newMessages = mutableListOf<Message>()
        private var oldMessages = mutableListOf<Message>()

        init {
            // We have to copy source lists,
            // so that lists won't change while diff is being calculated.
            newMessages.addAll(newMessagesSource)
            oldMessages.addAll(oldMessagesSource)
        }

        override fun getOldListSize() = oldMessages.size

        override fun getNewListSize() = newMessages.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
            oldMessages[oldItemPosition].uid == newMessages[newItemPosition].uid

        // This is called if areItemsTheSame() returns true
        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val isTextTheSame = oldMessages[oldItemPosition].text == newMessages[newItemPosition].text
            val isReadTheSame = oldMessages[oldItemPosition].isRead == newMessages[newItemPosition].isRead

            // Do not consider isRead flag if the message is not from current user
            return if (newMessages[newItemPosition].isFromCurrentUser) isTextTheSame && isReadTheSame else isTextTheSame
        }
    }
}