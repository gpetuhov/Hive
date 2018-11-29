package com.gpetuhov.android.hive.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.airbnb.epoxy.EpoxyRecyclerView
import com.arellomobile.mvp.presenter.InjectPresenter
import com.gpetuhov.android.hive.R
import com.gpetuhov.android.hive.databinding.FragmentDetailsBinding
import com.gpetuhov.android.hive.domain.model.User
import com.gpetuhov.android.hive.presentation.presenter.UserDetailsFragmentPresenter
import com.gpetuhov.android.hive.presentation.view.UserDetailsFragmentView
import com.gpetuhov.android.hive.ui.epoxy.user.controller.UserDetailsListController
import com.gpetuhov.android.hive.ui.viewmodel.DetailsViewModel
import com.gpetuhov.android.hive.util.hideToolbar
import com.gpetuhov.android.hive.util.moxy.MvpAppCompatFragment
import com.gpetuhov.android.hive.util.setActivitySoftInputPan
import com.gpetuhov.android.hive.util.showBottomNavigationView

// Shows user details on map marker click
class UserDetailsFragment : MvpAppCompatFragment(), UserDetailsFragmentView {

    @InjectPresenter lateinit var presenter: UserDetailsFragmentPresenter

    private lateinit var controller: UserDetailsListController

    private lateinit var userPic: ImageView

    private var binding: FragmentDetailsBinding? = null
    private var isOpenFromChat = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Adjust_pan is needed to prevent activity from being pushed up by the keyboard
        setActivitySoftInputPan()

        hideToolbar()
        showBottomNavigationView()

        val view = inflater.inflate(R.layout.fragment_user_details, container, false)

        controller = UserDetailsListController()

        val userDetailsRecyclerView = view.findViewById<EpoxyRecyclerView>(R.id.user_details_recycler_view)
        userDetailsRecyclerView.adapter = controller.adapter

//        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_details, container, false)
//        binding?.presenter = presenter
//
//        userPic = binding?.root?.findViewById(R.id.details_user_pic) ?: ImageView(context)

        isOpenFromChat = UserDetailsFragmentArgs.fromBundle(arguments).isOpenFromChat
        presenter.isOpenFromChat = isOpenFromChat

        val viewModel = ViewModelProviders.of(this).get(DetailsViewModel::class.java)
        viewModel.userDetails.observe(this, Observer<User> { user ->
            presenter.userUid = user.uid
            controller.changeUser(user)
//            binding?.user = user
//            updateUserPic(this, user, userPic)
        })

        return view
    }

    override fun onResume() {
        super.onResume()
        presenter.onResume()
    }

    override fun onPause() {
        super.onPause()
        presenter.onPause()
    }

    // === UserDetailsFragmentView ===

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
            val action = UserDetailsFragmentDirections.actionDetailsFragmentToChatFragment(true)
            findNavController().navigate(action)
        }
    }
}