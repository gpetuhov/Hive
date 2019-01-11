package com.gpetuhov.android.hive.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.arellomobile.mvp.presenter.InjectPresenter
import com.gpetuhov.android.hive.R
import com.gpetuhov.android.hive.databinding.FragmentPrivacyPolicyBinding
import com.gpetuhov.android.hive.presentation.presenter.PrivacyPolicyFragmentPresenter
import com.gpetuhov.android.hive.presentation.view.PrivacyPolicyFragmentView
import com.gpetuhov.android.hive.ui.fragment.base.BaseFragment
import com.gpetuhov.android.hive.util.hideMainHeader
import com.gpetuhov.android.hive.util.setActivitySoftInputPan
import com.gpetuhov.android.hive.util.showBottomNavigationView
import kotlinx.android.synthetic.main.fragment_privacy_policy.*

class PrivacyPolicyFragment : BaseFragment(), PrivacyPolicyFragmentView {

    @InjectPresenter lateinit var presenter: PrivacyPolicyFragmentPresenter

    private var binding: FragmentPrivacyPolicyBinding? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Adjust_pan is needed to prevent activity from being pushed up by the keyboard
        setActivitySoftInputPan()

        hideMainHeader()
        showBottomNavigationView()

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_privacy_policy, container, false)
        binding?.presenter = presenter

        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (savedInstanceState != null) {
            privacy_policy_webview.restoreState(savedInstanceState)
        } else {
            privacy_policy_webview.loadUrl("file:///android_asset/privacy_policy.html")
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        privacy_policy_webview.saveState(outState)
        super.onSaveInstanceState(outState)
    }

    // === PrivacyPolicyFragmentView ===

    override fun navigateUp() {
        findNavController().navigateUp()
    }
}