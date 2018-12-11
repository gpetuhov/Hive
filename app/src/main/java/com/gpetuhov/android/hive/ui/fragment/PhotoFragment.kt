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
import com.gpetuhov.android.hive.databinding.FragmentPhotoBinding
import com.gpetuhov.android.hive.domain.model.User
import com.gpetuhov.android.hive.presentation.presenter.PhotoFragmentPresenter
import com.gpetuhov.android.hive.presentation.view.PhotoFragmentView
import com.gpetuhov.android.hive.ui.epoxy.photo.fullscreen.controller.PhotoFullscreenListController
import com.gpetuhov.android.hive.ui.viewmodel.UserDetailsViewModel
import com.gpetuhov.android.hive.util.hideBottomNavigationView
import com.gpetuhov.android.hive.util.hideToolbar
import com.gpetuhov.android.hive.util.setActivitySoftInputPan

class PhotoFragment : BaseFragment(), PhotoFragmentView {

    @InjectPresenter lateinit var presenter: PhotoFragmentPresenter

    private var controller: PhotoFullscreenListController? = null
    private var binding: FragmentPhotoBinding? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Adjust_pan is needed to prevent activity from being pushed up by the keyboard
        setActivitySoftInputPan()

        hideToolbar()
        hideBottomNavigationView()

        controller = PhotoFullscreenListController(presenter)
        controller?.onRestoreInstanceState(savedInstanceState)

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_photo, container, false)
        binding?.presenter = presenter

        val args = PhotoFragmentArgs.fromBundle(arguments)
        presenter.offerUid = args.offerUid
        presenter.photoUid = args.photoUid

        val photoRecyclerView = binding?.root?.findViewById<EpoxyRecyclerView>(R.id.photo_recycler_view)
        photoRecyclerView?.adapter = controller?.adapter

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

// === PhotoFragmentView ===

    override fun navigateUp() {
        findNavController().navigateUp()
    }
}