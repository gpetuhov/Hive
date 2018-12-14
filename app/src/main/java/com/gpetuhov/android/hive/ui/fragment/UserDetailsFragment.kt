package com.gpetuhov.android.hive.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.airbnb.epoxy.EpoxyRecyclerView
import com.arellomobile.mvp.presenter.InjectPresenter
import com.gpetuhov.android.hive.R
import com.gpetuhov.android.hive.databinding.FragmentUserDetailsBinding
import com.gpetuhov.android.hive.domain.model.User
import com.gpetuhov.android.hive.presentation.presenter.UserDetailsFragmentPresenter
import com.gpetuhov.android.hive.presentation.view.UserDetailsFragmentView
import com.gpetuhov.android.hive.ui.epoxy.user.controller.UserDetailsListController
import com.gpetuhov.android.hive.ui.viewmodel.UserDetailsViewModel
import com.gpetuhov.android.hive.util.hideToolbar
import com.gpetuhov.android.hive.util.setActivitySoftInputPan
import com.gpetuhov.android.hive.util.showBottomNavigationView

// Shows user details on map marker click
class UserDetailsFragment : BaseFragment(), UserDetailsFragmentView {

    @InjectPresenter lateinit var presenter: UserDetailsFragmentPresenter

    private var controller: UserDetailsListController? = null
    private var binding: FragmentUserDetailsBinding? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Adjust_pan is needed to prevent activity from being pushed up by the keyboard
        setActivitySoftInputPan()

        hideToolbar()
        showBottomNavigationView()

        controller = UserDetailsListController(presenter)
        controller?.onRestoreInstanceState(savedInstanceState)

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_user_details, container, false)
        binding?.presenter = presenter

        val userDetailsRecyclerView = binding?.root?.findViewById<EpoxyRecyclerView>(R.id.user_details_recycler_view)
        userDetailsRecyclerView?.adapter = controller?.adapter

        val viewModel = ViewModelProviders.of(this).get(UserDetailsViewModel::class.java)
        viewModel.userDetails.observe(this, Observer<User> { user ->
            presenter.userUid = user.uid
            controller?.changeUser(user)
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

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        controller?.onSaveInstanceState(outState)
    }

    // === UserDetailsFragmentView ===

    override fun navigateUp() {
        findNavController().navigateUp()
    }

    override fun openChat() {
        val action = UserDetailsFragmentDirections.actionUserDetailsFragmentToChatFragment()
        findNavController().navigate(action)
    }

    override fun openOffer(offerUid: String) {
        val action = UserDetailsFragmentDirections.actionUserDetailsFragmentToOfferDetailsFragment(offerUid)
        findNavController().navigate(action)
    }

    override fun openPhotos(photoUrlList: MutableList<String>) {
        val photoBundle = Bundle()
        photoBundle.putStringArrayList(PhotoFragment.PHOTO_URL_LIST_KEY, ArrayList(photoUrlList))

        val action = UserDetailsFragmentDirections.actionUserDetailsFragmentToPhotoFragment(photoBundle)
        findNavController().navigate(action)
    }

    override fun openLocation(userUid: String) {
        val action = UserDetailsFragmentDirections.actionUserDetailsFragmentToLocationFragment(userUid)
        findNavController().navigate(action)
    }
}