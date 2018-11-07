package com.gpetuhov.android.hive.ui.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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
import com.gpetuhov.android.hive.application.HiveApp
import com.gpetuhov.android.hive.databinding.FragmentChatBinding
import com.gpetuhov.android.hive.domain.model.Message
import com.gpetuhov.android.hive.domain.repository.Repo
import com.gpetuhov.android.hive.presentation.presenter.ChatFragmentPresenter
import com.gpetuhov.android.hive.presentation.view.ChatFragmentView
import com.gpetuhov.android.hive.ui.adapter.MessagesAdapter
import com.gpetuhov.android.hive.ui.viewmodel.ChatMessagesViewModel
import com.gpetuhov.android.hive.util.moxy.MvpAppCompatFragment
import kotlinx.android.synthetic.main.fragment_chat.*
import javax.inject.Inject

class ChatFragment : MvpAppCompatFragment(), ChatFragmentView {

    @InjectPresenter lateinit var presenter: ChatFragmentPresenter

    @Inject lateinit var repo: Repo

    private val messagesAdapter = MessagesAdapter()
    private var binding: FragmentChatBinding? = null
    private var userUid = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        HiveApp.appComponent.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_chat, container, false)
        binding?.presenter = presenter

        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userUid = ChatFragmentArgs.fromBundle(arguments).uid
        val name = ChatFragmentArgs.fromBundle(arguments).name
        chat_user_name_text.text = name

        messages.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, true)
        messages.adapter = messagesAdapter

        message_send_button.isEnabled = false

        message_text.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                message_send_button.isEnabled = s.toString().trim { it <= ' ' }.isNotEmpty()
            }
        })

        val viewModel = ViewModelProviders.of(this).get(ChatMessagesViewModel::class.java)
        viewModel.messages.observe(this, Observer<MutableList<Message>> { messageList ->
            messagesAdapter.setMessages(messageList)
            messages.scrollToPosition(0)
        })
    }

    override fun onResume() {
        super.onResume()
        repo.startGettingMessagesUpdates(userUid)
    }

    override fun onPause() {
        super.onPause()
        repo.stopGettingMessagesUpdates()
    }

    // === ChatFragmentView

    override fun navigateUp() {
        findNavController().navigateUp()
    }

    override fun clearMessageText() = message_text.setText("")
}