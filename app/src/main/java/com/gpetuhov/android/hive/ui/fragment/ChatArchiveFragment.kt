package com.gpetuhov.android.hive.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.arellomobile.mvp.presenter.InjectPresenter
import com.gpetuhov.android.hive.R
import com.gpetuhov.android.hive.databinding.FragmentChatArchiveBinding
import com.gpetuhov.android.hive.domain.model.Message
import com.gpetuhov.android.hive.presentation.presenter.ChatArchiveFragmentPresenter
import com.gpetuhov.android.hive.presentation.view.ChatArchiveFragmentView
import com.gpetuhov.android.hive.ui.adapter.MessagesAdapter
import com.gpetuhov.android.hive.ui.adapter.MessagesArchiveAdapter
import com.gpetuhov.android.hive.ui.fragment.base.BaseFragment
import com.gpetuhov.android.hive.ui.viewmodel.ChatArchiveViewModel
import com.gpetuhov.android.hive.util.*
import kotlinx.android.synthetic.main.fragment_chat_archive.*

// Shows ALL chat messages with pagination but WITHOUT real-time updates.
// Pagination is required to load messages in portions when needed only (infinite scroll).
// New messages are NOT delivered here, because Firestore does not allow
// pagination together with real-time updates.

class ChatArchiveFragment : BaseFragment(), ChatArchiveFragmentView {

    @InjectPresenter lateinit var presenter: ChatArchiveFragmentPresenter

    private var binding: FragmentChatArchiveBinding? = null
    private var messagesAdapter: MessagesAdapter? = null
    private var messagesArchiveAdapter: MessagesArchiveAdapter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Adjust_pan is needed to prevent activity from being pushed up by the keyboard
        setActivitySoftInputPan()

        hideMainHeader()
        showBottomNavigationView()

//        presenter.getMessages()

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_chat_archive, container, false)
        binding?.presenter = presenter

        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        val viewModel = ViewModelProviders.of(this).get(ChatArchiveViewModel::class.java)
//
//        messagesAdapter = MessagesAdapter(presenter, viewModel.chatArchiveMessages.value)

        initMessagesList()

//        viewModel.chatArchiveMessages.observe(this, Observer<MutableList<Message>> { messageList ->
//            messagesAdapter?.setMessages(messageList)
//        })
    }

    override fun onResume() {
        super.onResume()
//        presenter.onResume()
    }

    override fun onPause() {
        super.onPause()
//        presenter.onPause()
    }

    // === ChatArchiveFragmentView ===

    override fun navigateUp() {
        findNavController().navigateUp()
    }

    // === Private methods ===

    private fun initMessagesList() {
        val options = presenter.getChatArchivePagingOptions(this)

        if (options != null) {
            messagesArchiveAdapter = MessagesArchiveAdapter(options)
            chat_archive_messages.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, true)
            chat_archive_messages.adapter = messagesArchiveAdapter
        }
    }
}