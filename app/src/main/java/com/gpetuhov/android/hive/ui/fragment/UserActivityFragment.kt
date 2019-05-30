package com.gpetuhov.android.hive.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.arellomobile.mvp.presenter.InjectPresenter
import com.gpetuhov.android.hive.R
import com.gpetuhov.android.hive.databinding.FragmentUserActivityBinding
import com.gpetuhov.android.hive.presentation.presenter.UserActivityFragmentPresenter
import com.gpetuhov.android.hive.presentation.view.UserActivityFragmentView
import com.gpetuhov.android.hive.ui.fragment.base.BaseFragment
import com.gpetuhov.android.hive.util.Constants
import com.gpetuhov.android.hive.util.hideMainHeader
import com.gpetuhov.android.hive.util.setActivitySoftInputPan
import com.gpetuhov.android.hive.util.showBottomNavigationView
import kotlinx.android.synthetic.main.fragment_user_activity.*

class UserActivityFragment : BaseFragment(), UserActivityFragmentView {

    @InjectPresenter lateinit var presenter: UserActivityFragmentPresenter

    private var binding: FragmentUserActivityBinding? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Adjust_pan is needed to prevent activity from being pushed up by the keyboard
        setActivitySoftInputPan()

        hideMainHeader()
        showBottomNavigationView()

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_user_activity, container, false)
        binding?.presenter = presenter

        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args = UserActivityFragmentArgs.fromBundle(arguments!!)
        val userActivityType = args.userActivityType

        updateUI(userActivityType)
    }

    // === UserActivityFragmentView ===

    override fun navigateUp() {
        findNavController().navigateUp()
    }

    // === Private methods ===

    private fun updateUI(userActivityType: Int) {
        val userActivityAnimationId = Constants.UserActivities.getUserActivity(userActivityType).animationId
        val userActivityDescriptionId = Constants.UserActivities.getUserActivity(userActivityType).descriptionId

        user_activity_animation.setAnimation(userActivityAnimationId)
        binding?.userActivityDescriptionText = getString(userActivityDescriptionId)
    }
}