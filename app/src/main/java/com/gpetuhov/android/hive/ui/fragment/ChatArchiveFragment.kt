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
import com.gpetuhov.android.hive.domain.model.User
import com.gpetuhov.android.hive.presentation.presenter.ChatArchiveFragmentPresenter
import com.gpetuhov.android.hive.presentation.view.ChatArchiveFragmentView
import com.gpetuhov.android.hive.ui.adapter.MessagesArchiveAdapter
import com.gpetuhov.android.hive.ui.fragment.base.BaseFragment
import com.gpetuhov.android.hive.ui.viewmodel.ChatArchiveViewModel
import com.gpetuhov.android.hive.util.*
import kotlinx.android.synthetic.main.fragment_chat_archive.*

// Shows ALL chat messages with pagination but WITHOUT real-time updates.
// Pagination is required to load messages in portions when needed only (infinite scroll).
// New messages are NOT delivered here, because Firestore does not allow
// pagination together with real-time updates.

class ChatArchiveFragment :
    BaseFragment(),
    ChatArchiveFragmentView,
    MessagesArchiveAdapter.Callback {

    @InjectPresenter lateinit var presenter: ChatArchiveFragmentPresenter

    private var binding: FragmentChatArchiveBinding? = null
    private var messagesArchiveAdapter: MessagesArchiveAdapter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Adjust_pan is needed to prevent activity from being pushed up by the keyboard
        setActivitySoftInputPan()

        hideMainHeader()
        showBottomNavigationView()

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_chat_archive, container, false)
        binding?.presenter = presenter

        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initMessagesList()

        val viewModel = ViewModelProviders.of(this).get(ChatArchiveViewModel::class.java)
        viewModel.secondUser.observe(this, Observer<User> { secondUser ->
            presenter.secondUserUid = secondUser.uid
            binding?.username = secondUser.getUsernameOrName()
            updateUserPic(this, secondUser, chat_archive_header_image)
        })
    }

    override fun onResume() {
        super.onResume()
        presenter.onResume()
    }

    override fun onPause() {
        super.onPause()
        presenter.onPause()
    }

    // === ChatArchiveFragmentView ===

    override fun scrollDown() = chat_archive_messages.scrollToPosition(0)

    override fun showScrollDownButton() {
        if (chat_archive_scroll_down_button.visibility != View.VISIBLE) chat_archive_scroll_down_button.show()
    }

    override fun hideScrollDownButton() {
        if (chat_archive_scroll_down_button.visibility == View.VISIBLE) chat_archive_scroll_down_button.hide()
    }

    override fun openUserDetails() {
        val action = ChatArchiveFragmentDirections.actionChatArchiveFragmentToUserDetailsFragment()
        findNavController().navigate(action)
    }

    override fun navigateUp() {
        findNavController().navigateUp()
    }

    // === MessagesArchiveAdapter.Callback ===

    // Scroll to latest message on initial page loaded.
    // We do not save last scroll position and do not restore it on screen rotation,
    // like in ChatFragment, because with pagination it does not work, if user scrolled several pages.
    // So just scroll to the very beginning.
    override fun onInitialLoaded() = scrollDown()

    // === Private methods ===

    private fun initMessagesList() {
        val options = presenter.getChatArchivePagingOptions(this)

        if (options != null) {
            messagesArchiveAdapter = MessagesArchiveAdapter(this, options)
            chat_archive_messages.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, true)
            chat_archive_messages.adapter = messagesArchiveAdapter
            chat_archive_messages.addOnScrollListener(presenter.scrollListener)
        }
    }
}