package com.gpetuhov.android.hive.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.airbnb.epoxy.EpoxyRecyclerView
import com.arellomobile.mvp.presenter.InjectPresenter
import com.gpetuhov.android.hive.R
import com.gpetuhov.android.hive.databinding.FragmentPhotoBinding
import com.gpetuhov.android.hive.presentation.presenter.PhotoFragmentPresenter
import com.gpetuhov.android.hive.presentation.view.PhotoFragmentView
import com.gpetuhov.android.hive.ui.epoxy.photo.fullscreen.controller.PhotoFullscreenListController
import com.gpetuhov.android.hive.ui.fragment.base.BaseFragment
import com.gpetuhov.android.hive.util.hideBottomNavigationView
import com.gpetuhov.android.hive.util.hideMainHeader
import com.gpetuhov.android.hive.util.setActivitySoftInputPan

class PhotoFragment : BaseFragment(), PhotoFragmentView {

    companion object {
        const val PHOTO_URL_LIST_KEY = "photoUrlList"
    }

    @InjectPresenter lateinit var presenter: PhotoFragmentPresenter

    private var controller: PhotoFullscreenListController? = null
    private var binding: FragmentPhotoBinding? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Adjust_pan is needed to prevent activity from being pushed up by the keyboard
        setActivitySoftInputPan()

        hideMainHeader()
        hideBottomNavigationView()

        controller = PhotoFullscreenListController()
        controller?.onRestoreInstanceState(savedInstanceState)

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_photo, container, false)
        binding?.presenter = presenter

        val args = PhotoFragmentArgs.fromBundle(arguments)
        val photoBundle = args.photoBundle
        val photoUrlList = photoBundle.getStringArrayList(PHOTO_URL_LIST_KEY)?.toMutableList() ?: mutableListOf()

        val photoRecyclerView = binding?.root?.findViewById<EpoxyRecyclerView>(R.id.photo_recycler_view)
        photoRecyclerView?.adapter = controller?.adapter

        controller?.setPhotos(photoUrlList)

        return binding?.root
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