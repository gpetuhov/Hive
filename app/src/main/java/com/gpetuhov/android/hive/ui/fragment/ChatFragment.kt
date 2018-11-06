package com.gpetuhov.android.hive.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.gpetuhov.android.hive.R
import kotlinx.android.synthetic.main.fragment_chat.*

class ChatFragment : Fragment() {

    private var uid = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_chat, container, false)
    }

    override fun onResume() {
        super.onResume()
        uid = ChatFragmentArgs.fromBundle(arguments).uid
        val name = ChatFragmentArgs.fromBundle(arguments).name
        chat_text.text = name
    }
}