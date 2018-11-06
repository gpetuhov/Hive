package com.gpetuhov.android.hive.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.arellomobile.mvp.presenter.InjectPresenter
import com.gpetuhov.android.hive.R
import com.gpetuhov.android.hive.databinding.FragmentDetailsBinding
import com.gpetuhov.android.hive.domain.model.User
import com.gpetuhov.android.hive.presentation.presenter.DetailsFragmentPresenter
import com.gpetuhov.android.hive.presentation.view.DetailsFragmentView
import com.gpetuhov.android.hive.ui.viewmodel.SearchUserDetailsViewModel
import com.gpetuhov.android.hive.util.moxy.MvpAppCompatFragment

// Shows user details on map marker click
class DetailsFragment : MvpAppCompatFragment(), DetailsFragmentView  {

    @InjectPresenter lateinit var presenter: DetailsFragmentPresenter

    private var binding: FragmentDetailsBinding? = null
    private var uid = ""
    private var name = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_details, container, false)
        binding?.presenter = presenter

        val viewModel = ViewModelProviders.of(this).get(SearchUserDetailsViewModel::class.java)

        uid = DetailsFragmentArgs.fromBundle(arguments).uid

        // This is needed to get user details immediately from the already available search results
        viewModel.getFirstUpdate(uid)

        viewModel.searchUserDetails.observe(this, Observer<User> { user ->
            binding?.user = user
            name = if (user.hasUsername) user.username else user.name
        })

        return binding?.root
    }

    override fun onResume() {
        super.onResume()
        presenter.onResume(uid)
    }

    override fun onPause() {
        super.onPause()
        presenter.onPause()
    }

    // === DetailsFragmentView ===

    override fun navigateUp() {
        findNavController().navigateUp()
    }

    override fun openChat() {
        val action = DetailsFragmentDirections.actionDetailsFragmentToChatFragment(uid, name)
        findNavController().navigate(action)
    }
}