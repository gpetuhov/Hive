package com.gpetuhov.android.hive.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gpetuhov.android.hive.R
import com.gpetuhov.android.hive.databinding.FragmentChatBinding
import com.gpetuhov.android.hive.ui.adapter.MessagesAdapter
import kotlinx.android.synthetic.main.fragment_chat.*

class ChatFragment : Fragment() {

    private var binding: FragmentChatBinding? = null
    private var uid = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_chat, container, false)
        binding?.handler = this

        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        messages.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, true)
        messages.adapter = MessagesAdapter()
    }

    override fun onResume() {
        super.onResume()
        uid = ChatFragmentArgs.fromBundle(arguments).uid
        val name = ChatFragmentArgs.fromBundle(arguments).name
        chat_user_name_text.text = name
    }

    // === Public methods ===

    fun navigateUp() {
        findNavController().navigateUp()
    }
}