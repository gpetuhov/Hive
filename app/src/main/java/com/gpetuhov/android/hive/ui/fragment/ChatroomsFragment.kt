package com.gpetuhov.android.hive.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.arellomobile.mvp.presenter.InjectPresenter
import com.gpetuhov.android.hive.R
import com.gpetuhov.android.hive.databinding.FragmentChatroomsBinding
import com.gpetuhov.android.hive.domain.model.Chatroom
import com.gpetuhov.android.hive.presentation.presenter.ChatroomsFragmentPresenter
import com.gpetuhov.android.hive.presentation.view.ChatroomsFragmentView
import com.gpetuhov.android.hive.ui.adapter.ChatroomsAdapter
import com.gpetuhov.android.hive.ui.recycler.SimpleItemDecoration
import com.gpetuhov.android.hive.ui.viewmodel.ChatroomsViewModel
import com.gpetuhov.android.hive.util.hideToolbar
import com.gpetuhov.android.hive.util.moxy.MvpAppCompatFragment
import com.gpetuhov.android.hive.util.setActivitySoftInputPan
import com.gpetuhov.android.hive.util.showBottomNavigationView
import kotlinx.android.synthetic.main.fragment_chatrooms.*

class ChatroomsFragment : MvpAppCompatFragment(), ChatroomsFragmentView {

    @InjectPresenter lateinit var presenter: ChatroomsFragmentPresenter

    private var chatroomsAdapter: ChatroomsAdapter? = null
    private var binding: FragmentChatroomsBinding? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Adjust_pan is needed to prevent activity from being pushed up by the keyboard
        setActivitySoftInputPan()

        hideToolbar()
        showBottomNavigationView()

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_chatrooms, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        chatrooms.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        chatroomsAdapter = ChatroomsAdapter(presenter)
        chatrooms.adapter = chatroomsAdapter

        // Add divider between items
        val dividerItemDecoration = SimpleItemDecoration(context?.let { ContextCompat.getDrawable(it, R.drawable.item_divider) })
        chatrooms.addItemDecoration(dividerItemDecoration)

        val viewModel = ViewModelProviders.of(this).get(ChatroomsViewModel::class.java)
        viewModel.chatrooms.observe(this, Observer<MutableList<Chatroom>> { chatroomList ->
            chatroomsAdapter?.setChatrooms(chatroomList)
            binding?.chatroomListEmpty = chatroomList.isEmpty()
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

    // === ChatroomsFragmentView ===

    override fun openChat() {
        val action = ChatroomsFragmentDirections.actionNavigationMessagesToChatFragment()
        findNavController().navigate(action)
    }
}