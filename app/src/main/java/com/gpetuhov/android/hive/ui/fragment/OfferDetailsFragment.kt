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
import com.gpetuhov.android.hive.ui.fragment.base.BaseFragment
import com.gpetuhov.android.hive.ui.viewmodel.UserDetailsViewModel
import com.gpetuhov.android.hive.util.hideMainHeader
import com.gpetuhov.android.hive.util.setActivitySoftInputPan
import com.gpetuhov.android.hive.util.showBottomNavigationView
import com.pawegio.kandroid.toast

class OfferDetailsFragment : BaseFragment(), OfferDetailsFragmentView {

    @InjectPresenter lateinit var presenter: OfferDetailsFragmentPresenter

    private var controller: OfferDetailsListController? = null
    private var binding: FragmentOfferDetailsBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        controller = OfferDetailsListController(presenter)
        controller?.onRestoreInstanceState(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Adjust_pan is needed to prevent activity from being pushed up by the keyboard
        setActivitySoftInputPan()

        hideMainHeader()
        showBottomNavigationView()

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_offer_details, container, false)
        binding?.presenter = presenter

        val offerUid = OfferDetailsFragmentArgs.fromBundle(arguments!!).offerUid
        presenter.offerUid = offerUid

        val offerDetailsRecyclerView = binding?.root?.findViewById<EpoxyRecyclerView>(R.id.offer_details_recycler_view)
        offerDetailsRecyclerView?.adapter = controller?.adapter

        val viewModel = ViewModelProviders.of(this).get(UserDetailsViewModel::class.java)
        viewModel.userDetails.observe(this, Observer<User> { user ->
            presenter.userUid = user.uid
            val userIsDeleted = user.isDeleted
            val offer = user.getOffer(offerUid)
            val offerIsDeleted = offer == null || !offer.isActive
            presenter.offerIsFavorite = offer?.isFavorite ?: false
            binding?.offerIsFavorite = presenter.offerIsFavorite
            binding?.chatButtonVisible = !userIsDeleted
            binding?.favoriteButtonVisible = !userIsDeleted && !offerIsDeleted
            binding?.offerStarCount = offer?.starCountString ?: ""
            controller?.changeOffer(user, offer)
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
        controller?.onSaveInstanceState(outState)
        super.onSaveInstanceState(outState)
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

    override fun openPhotos(photoUrlList: MutableList<String>) {
        val photoBundle = Bundle()
        photoBundle.putStringArrayList(PhotoFragment.PHOTO_URL_LIST_KEY, ArrayList(photoUrlList))

        val action = OfferDetailsFragmentDirections.actionOfferDetailsFragmentToPhotoFragment(photoBundle)
        findNavController().navigate(action)
    }

    override fun openLocation(userUid: String) {
        val action = OfferDetailsFragmentDirections.actionOfferDetailsFragmentToLocationFragment(userUid)
        findNavController().navigate(action)
    }

    override fun openReviews(userUid: String, offerUid: String) {
        val action = OfferDetailsFragmentDirections.actionOfferDetailsFragmentToReviewsFragment(userUid, offerUid, false)
        findNavController().navigate(action)
    }

    override fun showToast(message: String) {
        toast(message)
    }
}