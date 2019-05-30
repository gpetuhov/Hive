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
        controller?.onSaveInstanceState(outState)
        super.onSaveInstanceState(outState)
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
        val webUrl = "https://www.facebook.com/$facebook"
        val appUri = "fb://facewebmodal/f?href=$webUrl"
        openSocialAccount("com.facebook.katana", webUrl, appUri)
    }

    override fun openTwitter(twitter: String) {
        openSocialAccount(
            "com.twitter.android",
            "https://twitter.com/$twitter",
            "twitter://user?screen_name=$twitter"
        )
    }

    override fun openInstagram(instagram: String) {
        openSocialAccount(
            "com.instagram.android",
            "http://instagram.com/$instagram",
            "http://instagram.com/_u/$instagram"
        )
    }

    override fun openYouTube(youTube: String) {
        val url = "https://www.youtube.com/user/$youTube"
        openSocialAccount("com.google.android.youtube", url, url)
    }

    private fun openSocialAccount(appPackageName: String, webUrl: String, appUri: String) {
        val packageManager = activity?.packageManager
        var uri = Uri.parse(webUrl)
        var intent = Intent(Intent.ACTION_VIEW, uri)

        try {
            val applicationInfo = packageManager?.getApplicationInfo(appPackageName, 0)
            if (applicationInfo?.enabled == true) {
                // Social application installed. Change url to be opened in the app
                uri = Uri.parse(appUri)
                intent = Intent(Intent.ACTION_VIEW, uri)
                intent.setPackage(appPackageName)
            }
        } catch (ignored: PackageManager.NameNotFoundException) {
            // Do nothing. Social application not installed. Url will be opened in browser
        }

        if (packageManager != null && intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        }
    }

    override fun openWebsite(website: String) {
        val packageManager = activity?.packageManager
        val uri = Uri.parse(website)
        val intent = Intent(Intent.ACTION_VIEW, uri)

        if (packageManager != null && intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        }
    }

    override fun openAward(awardType: Int) {
        val action = UserDetailsFragmentDirections.actionUserDetailsFragmentToAwardFragment(awardType)
        findNavController().navigate(action)
    }

    override fun openUserActivity(userActivityType: Int) {
        val action = UserDetailsFragmentDirections.actionUserDetailsFragmentToUserActivityFragment(userActivityType)
        findNavController().navigate(action)
    }
}