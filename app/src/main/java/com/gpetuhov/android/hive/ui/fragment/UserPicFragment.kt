package com.gpetuhov.android.hive.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.arellomobile.mvp.presenter.InjectPresenter
import com.gpetuhov.android.hive.R
import com.gpetuhov.android.hive.databinding.FragmentUserPicBinding
import com.gpetuhov.android.hive.presentation.presenter.UserPicFragmentPresenter
import com.gpetuhov.android.hive.presentation.view.UserPicFragmentView
import com.gpetuhov.android.hive.ui.fragment.base.BaseFragment
import com.gpetuhov.android.hive.util.hideBottomNavigationView
import com.gpetuhov.android.hive.util.hideMainHeader
import com.gpetuhov.android.hive.util.setActivitySoftInputPan

class UserPicFragment : BaseFragment(), UserPicFragmentView {

    @InjectPresenter lateinit var presenter: UserPicFragmentPresenter

    private var binding: FragmentUserPicBinding? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Adjust_pan is needed to prevent activity from being pushed up by the keyboard
        setActivitySoftInputPan()

        hideMainHeader()
        hideBottomNavigationView()

        val args = UserPicFragmentArgs.fromBundle(arguments!!)
        val userPicUrl = args.userPicUrl

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_user_pic, container, false)
        binding?.presenter = presenter

        return binding?.root
    }

    // === UserPicFragmentView ===

    override fun navigateUp() {
        findNavController().navigateUp()
    }
}