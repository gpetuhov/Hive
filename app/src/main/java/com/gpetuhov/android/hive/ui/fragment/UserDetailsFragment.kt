package com.gpetuhov.android.hive.ui.fragment

import android.content.Intent
import android.net.Uri
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
import com.gpetuhov.android.hive.ui.epoxy.user.details.controller.UserDetailsListController
import com.gpetuhov.android.hive.ui.fragment.base.BaseFragment
import com.gpetuhov.android.hive.ui.viewmodel.UserDetailsViewModel
import com.gpetuhov.android.hive.util.hideMainHeader
import com.gpetuhov.android.hive.util.setActivitySoftInputPan
import com.gpetuhov.android.hive.util.showBottomNavigationView
import com.pawegio.kandroid.toast
import android.content.pm.PackageManager

// Shows user details on map marker click
class UserDetailsFragment : BaseFragment(), UserDetailsFragmentView {

    @InjectPresenter lateinit var presenter: UserDetailsFragmentPresenter

    private var controller: UserDetailsListController? = null
    private var binding: FragmentUserDetailsBinding? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Adjust_pan is needed to prevent activity from being pushed up by the keyboard
        setActivitySoftInputPan()

        hideMainHeader()
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
            presenter.userIsFavorite = user.isFavorite
            binding?.user = user
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

    override fun openAllReviews() {
        val action = UserDetailsFragmentDirections.actionUserDetailsFragmentToReviewsAllFragment(false)
        findNavController().navigate(action)
    }

    override fun dialPhone(phone: String) {
        val intent = Intent(Intent.ACTION_DIAL)
        intent.data = Uri.parse("tel:$phone")
        startActivity(intent)
    }

    override fun sendEmail(email: String) {
        val intent = Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", email, null))
        intent.putExtra(Intent.EXTRA_SUBJECT, context?.getString(R.string.email_subject) ?: "")
        startActivity(intent)
    }

    override fun showToast(message: String) {
        toast(message)
    }

    override fun callSkype(skype: String) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse("skype:$skype")

        val packageManager = activity?.packageManager
        if (packageManager != null && intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        } else {
            showToast(context?.getString(R.string.skype_not_installed) ?: "")
        }
    }

    override fun openFacebook(facebook: String) {
        val packageManager = activity?.packageManager
        val url = "https://www.facebook.com/$facebook"
        var uri = Uri.parse(url)
        try {
            val applicationInfo = packageManager?.getApplicationInfo("com.facebook.katana", 0)
            if (applicationInfo?.enabled == true) {
                // Facebook application installed. Change url to be opened in Facebook app
                uri = Uri.parse("fb://facewebmodal/f?href=$url")
            }
        } catch (ignored: PackageManager.NameNotFoundException) {
            // Do nothing. Facebook application not installed. Url will be opened in browser
        }

        val intent = Intent(Intent.ACTION_VIEW, uri)

        if (packageManager != null && intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        }
    }

    // TODO: refactor this with openFacebook
    override fun openTwitter(twitter: String) {
        val packageManager = activity?.packageManager
        val url = "https://twitter.com/$twitter"
        var uri = Uri.parse(url)
        try {
            val applicationInfo = packageManager?.getApplicationInfo("com.twitter.android", 0)
            if (applicationInfo?.enabled == true) {
                // Twitter application installed. Change url to be opened in Twitter app
                uri = Uri.parse("twitter://user?screen_name=$twitter")
            }
        } catch (ignored: PackageManager.NameNotFoundException) {
            // Do nothing. Twitter application not installed. Url will be opened in browser
        }

        val intent = Intent(Intent.ACTION_VIEW, uri)

        if (packageManager != null && intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        }
    }

    // TODO: refactor this with openFacebook
    override fun openInstagram(instagram: String) {
        val packageManager = activity?.packageManager
        val url = "http://instagram.com/$instagram"
        var uri = Uri.parse(url)
        var intent = Intent(Intent.ACTION_VIEW, uri)

        try {
            val instagramPackageName = "com.instagram.android"
            val applicationInfo = packageManager?.getApplicationInfo(instagramPackageName, 0)
            if (applicationInfo?.enabled == true) {
                // Instagram application installed. Change url to be opened in Instagram app
                uri = Uri.parse("http://instagram.com/_u/$instagram")
                intent = Intent(Intent.ACTION_VIEW, uri)
                intent.setPackage(instagramPackageName)
            }
        } catch (ignored: PackageManager.NameNotFoundException) {
            // Do nothing. Instagram application not installed. Url will be opened in browser
        }

        if (packageManager != null && intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        }
    }

    // TODO: use this
    // TODO: refactor this with openFacebook
    private fun openYouTube(youTube: String) {
        val packageManager = activity?.packageManager
        val url = "https://www.youtube.com/user/$youTube"
        val uri = Uri.parse(url)
        val intent = Intent(Intent.ACTION_VIEW, uri)

        try {
            val youTubePackageName = "com.google.android.youtube"
            val applicationInfo = packageManager?.getApplicationInfo(youTubePackageName, 0)
            if (applicationInfo?.enabled == true) {
                // YoutTube application installed. Set intent package to be opened in YouTube app
                intent.setPackage(youTubePackageName)
            }
        } catch (ignored: PackageManager.NameNotFoundException) {
            // Do nothing. YouTube application not installed. Url will be opened in browser
        }

        if (packageManager != null && intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        }
    }
}