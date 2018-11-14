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
import com.gpetuhov.android.hive.ui.viewmodel.DetailsViewModel
import com.gpetuhov.android.hive.util.moxy.MvpAppCompatFragment
import com.gpetuhov.android.hive.util.setActivitySoftInputPan
import com.gpetuhov.android.hive.util.showBottomNavigationView

// Shows user details on map marker click
class DetailsFragment : MvpAppCompatFragment(), DetailsFragmentView {

    @InjectPresenter lateinit var presenter: DetailsFragmentPresenter

    private var binding: FragmentDetailsBinding? = null
    private var isOpenFromChat = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Adjust_pan is needed to prevent activity from being pushed up by the keyboard
        setActivitySoftInputPan()

        showBottomNavigationView()

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_details, container, false)
        binding?.presenter = presenter

        isOpenFromChat = DetailsFragmentArgs.fromBundle(arguments).isOpenFromChat
        presenter.isOpenFromChat = isOpenFromChat

        val viewModel = ViewModelProviders.of(this).get(DetailsViewModel::class.java)
        viewModel.userDetails.observe(this, Observer<User> { user ->
            presenter.userUid = user.uid
            binding?.user = user
        })

        return binding?.root
    }

    override fun onResume() {
        super.onResume()
        presenter.onResume()
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
        // If details fragment has been opened from chat fragment,
        // then just pop back stack.
        // Otherwise, open chat fragment.
        if (isOpenFromChat) {
            findNavController().popBackStack()
        } else {
            val action = DetailsFragmentDirections.actionDetailsFragmentToChatFragment(true)
            findNavController().navigate(action)
        }
    }
}