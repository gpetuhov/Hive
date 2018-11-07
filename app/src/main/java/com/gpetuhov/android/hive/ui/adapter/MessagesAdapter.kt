package com.gpetuhov.android.hive.ui.adapter

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.gpetuhov.android.hive.R
import com.gpetuhov.android.hive.application.HiveApp
import com.gpetuhov.android.hive.domain.model.Message
import com.gpetuhov.android.hive.domain.repository.Repo
import javax.inject.Inject

class MessagesAdapter : RecyclerView.Adapter<MessagesAdapter.MessageViewHolder>() {

    @Inject lateinit var context: Context

    private var messageList = mutableListOf<Message>()

    init {
        HiveApp.appComponent.inject(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val layoutInflater = LayoutInflater.from(context)
        val view = layoutInflater.inflate(R.layout.item_message, parent, false)
        return MessageViewHolder(view)
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

    class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        @Inject lateinit var repo: Repo

        private lateinit var message: Message
        private var rootLayout: LinearLayout = itemView.findViewById(R.id.message_root_layout)
        private var leftSpace: View = itemView.findViewById(R.id.message_left_space)
        private var rightSpace: View = itemView.findViewById(R.id.message_right_space)
        private var messageWrapper: LinearLayout = itemView.findViewById(R.id.item_message_wrapper)
        private var messageTextView: TextView = itemView.findViewById(R.id.item_message_text)
        private var messageTimeTextView: TextView = itemView.findViewById(R.id.item_message_time)

        init {
            HiveApp.appComponent.inject(this)
        }

        fun bindMessage(message: Message) {
            this.message = message
            messageTextView.text = message.text
            messageTimeTextView.text = (message.timestamp / 1000000).toString()

            if (message.isFromUser(repo.currentUserUid())) {
                rootLayout.gravity = Gravity.END
                leftSpace.visibility = View.VISIBLE
                rightSpace.visibility = View.GONE
                messageWrapper.setBackgroundResource(R.drawable.message_background_current_user)

            } else {
                rootLayout.gravity = Gravity.START
                leftSpace.visibility = View.GONE
                rightSpace.visibility = View.VISIBLE
                messageWrapper.setBackgroundResource(R.drawable.message_background)
            }
        }
    }
}