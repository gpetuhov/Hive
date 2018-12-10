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
import com.gpetuhov.android.hive.databinding.FragmentOfferDetailsBinding
import com.gpetuhov.android.hive.domain.model.User
import com.gpetuhov.android.hive.presentation.presenter.OfferDetailsFragmentPresenter
import com.gpetuhov.android.hive.presentation.view.OfferDetailsFragmentView
import com.gpetuhov.android.hive.ui.epoxy.offer.details.controller.OfferDetailsListController
import com.gpetuhov.android.hive.ui.viewmodel.UserDetailsViewModel
import com.gpetuhov.android.hive.util.hideToolbar
import com.gpetuhov.android.hive.util.setActivitySoftInputPan
import com.gpetuhov.android.hive.util.showBottomNavigationView

class OfferDetailsFragment : BaseFragment(), OfferDetailsFragmentView {

    @InjectPresenter lateinit var presenter: OfferDetailsFragmentPresenter

    private lateinit var controller: OfferDetailsListController
    private var binding: FragmentOfferDetailsBinding? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Adjust_pan is needed to prevent activity from being pushed up by the keyboard
        setActivitySoftInputPan()

        hideToolbar()
        showBottomNavigationView()

        controller = OfferDetailsListController(presenter)

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_offer_details, container, false)
        binding?.presenter = presenter

        val offerUid = OfferDetailsFragmentArgs.fromBundle(arguments).offerUid
        presenter.offerUid = offerUid

        val offerDetailsRecyclerView = binding?.root?.findViewById<EpoxyRecyclerView>(R.id.offer_details_recycler_view)
        offerDetailsRecyclerView?.adapter = controller.adapter

        val viewModel = ViewModelProviders.of(this).get(UserDetailsViewModel::class.java)
        viewModel.userDetails.observe(this, Observer<User> { user ->
            presenter.userUid = user.uid
            controller.changeOffer(user, offerUid)
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

    // === UserDetailsFragmentView ===

    override fun navigateUp() {
        findNavController().navigateUp()
    }

    override fun openChat() {
        val action = OfferDetailsFragmentDirections.actionOfferDetailsFragmentToChatFragment()
        findNavController().navigate(action)
    }

    override fun openUserDetails() {
        val action = OfferDetailsFragmentDirections.actionOfferDetailsFragmentToUserDetailsFragment()
        findNavController().navigate(action)
    }

    override fun openPhotos(offerUid: String, selectedPhotoUid: String) {
        val action = OfferDetailsFragmentDirections.actionOfferDetailsFragmentToPhotoFragment(offerUid, selectedPhotoUid)
        findNavController().navigate(action)
    }
}