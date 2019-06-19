package com.gpetuhov.android.hive.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.firebase.ui.firestore.paging.FirestorePagingAdapter
import com.firebase.ui.firestore.paging.FirestorePagingOptions
import com.firebase.ui.firestore.paging.LoadingState
import com.gpetuhov.android.hive.R
import com.gpetuhov.android.hive.databinding.ItemMessageBinding
import com.gpetuhov.android.hive.domain.model.Message
import com.gpetuhov.android.hive.ui.adapter.viewholder.MessageViewHolder

// This adapter supports pagination while reading data from Firestore

class MessagesArchiveAdapter(
    private var callback: Callback,
    options: FirestorePagingOptions<Message>
) : FirestorePagingAdapter<Message, MessageViewHolder>(options) {

    interface Callback {
        fun onInitialLoaded()
    }

    private var isLoadingInitial = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: ItemMessageBinding = DataBindingUtil.inflate(layoutInflater, R.layout.item_message, parent, false)
        return MessageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int, message: Message) {
        holder.binding.message = message
        holder.binding.executePendingBindings() // This line is important, it will force to load the variable in a custom view
    }

    override fun onLoadingStateChanged(state: LoadingState) {
        when (state) {
            // This is needed to trigger callback, when initial page is loaded
            LoadingState.LOADING_INITIAL -> { isLoadingInitial = true }
            LoadingState.LOADED -> {
                if (isLoadingInitial) {
                    isLoadingInitial = false
                    callback.onInitialLoaded()
                }
            }
            else -> { /* Do nothing */ }
        }
    }
}