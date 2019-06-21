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
import com.gpetuhov.android.hive.util.load
import com.gpetuhov.android.hive.util.setActivitySoftInputPan
import kotlinx.android.synthetic.main.fragment_user_pic.*

class UserPicFragment : BaseFragment(), UserPicFragmentView {

    @InjectPresenter lateinit var presenter: UserPicFragmentPresenter

    private var binding: FragmentUserPicBinding? = null
    private var userPicUrl = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Adjust_pan is needed to prevent activity from being pushed up by the keyboard
        setActivitySoftInputPan()

        hideMainHeader()
        hideBottomNavigationView()

        val args = UserPicFragmentArgs.fromBundle(arguments!!)
        userPicUrl = args.userPicUrl

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_user_pic, container, false)
        binding?.presenter = presenter

        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        user_pic_fullscreen.load(userPicUrl, false)
    }

    // === UserPicFragmentView ===

    override fun navigateUp() {
        findNavController().navigateUp()
    }
}