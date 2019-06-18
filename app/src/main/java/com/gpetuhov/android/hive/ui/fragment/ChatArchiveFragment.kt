package com.gpetuhov.android.hive.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.arellomobile.mvp.presenter.InjectPresenter
import com.gpetuhov.android.hive.R
import com.gpetuhov.android.hive.databinding.FragmentChatArchiveBinding
import com.gpetuhov.android.hive.presentation.presenter.ChatArchiveFragmentPresenter
import com.gpetuhov.android.hive.presentation.view.ChatArchiveFragmentView
import com.gpetuhov.android.hive.ui.fragment.base.BaseFragment
import com.gpetuhov.android.hive.util.*

class ChatArchiveFragment : BaseFragment(), ChatArchiveFragmentView {

    @InjectPresenter lateinit var presenter: ChatArchiveFragmentPresenter

    private var binding: FragmentChatArchiveBinding? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Adjust_pan is needed to prevent activity from being pushed up by the keyboard
        setActivitySoftInputPan()

        hideMainHeader()
        showBottomNavigationView()

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_chat_archive, container, false)
        binding?.presenter = presenter

        return binding?.root
    }

    // === ChatArchiveFragmentView ===

    override fun navigateUp() {
        findNavController().navigateUp()
    }
}