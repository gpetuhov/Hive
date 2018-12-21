package com.gpetuhov.android.hive.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.afollestad.materialdialogs.MaterialDialog
import com.arellomobile.mvp.presenter.InjectPresenter
import com.gpetuhov.android.hive.R
import com.gpetuhov.android.hive.databinding.FragmentUpdateReviewBinding
import com.gpetuhov.android.hive.presentation.presenter.UpdateReviewFragmentPresenter
import com.gpetuhov.android.hive.presentation.view.UpdateReviewFragmentView
import com.gpetuhov.android.hive.ui.fragment.base.BaseFragment
import com.gpetuhov.android.hive.util.*
import kotlinx.android.synthetic.main.fragment_update_review.*

class UpdateReviewFragment : BaseFragment(), UpdateReviewFragmentView {

    @InjectPresenter lateinit var presenter: UpdateReviewFragmentPresenter

    private var binding: FragmentUpdateReviewBinding? = null

    private var quitDialog: MaterialDialog? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Adjust_pan is needed to prevent activity from being pushed up by the keyboard
        setActivitySoftInputPan()

        hideToolbar()
        showBottomNavigationView()

        initDialogs()

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_update_review, container, false)
        binding?.presenter = presenter

        val offerUid = UpdateReviewFragmentArgs.fromBundle(arguments).offerUid
        presenter.offerUid = offerUid

        return binding?.root
    }

    override fun onDestroyView() {
        super.onDestroyView()

        // This is needed to prevent memory leaks.
        // Here we intentially dismiss dialogs directly, not via the presenter,
        // so that Moxy command queue doesn't change.
        dismissDialogs()
    }

    override fun onBackPressed(): Boolean {
        presenter.showQuitReviewUpdateDialog()
        return true
    }

    // === UpdateReviewFragmentView ===

    override fun disableButtons() = saveButtonEnabled(false)

    override fun enableButtons() = saveButtonEnabled(true)

    override fun showProgress() = progressVisible(true)

    override fun hideProgress() = progressVisible(false)

    override fun showQuitReviewUpdateDialog() = quitDialog?.show() ?: Unit

    override fun dismissQuitReviewUpdateDialog() = quitDialog?.dismiss() ?: Unit

    override fun navigateUp() {
        findNavController().navigateUp()
        hideSoftKeyboard()
    }

    // === Private methods ===

    private fun initDialogs() {
        initQuitDialog()
    }

    private fun initQuitDialog() {
        if (context != null) {
            quitDialog = MaterialDialog(context!!)
                .title(R.string.quit)
                .message(R.string.quit_review_update_prompt)
                .noAutoDismiss()
                .cancelable(false)
                .positiveButton { presenter.quitReviewUpdate() }
                .negativeButton { presenter.quitReviewUpdateCancel() }
        }
    }

    private fun dismissDialogs() {
        dismissQuitReviewUpdateDialog()
    }

    private fun saveButtonEnabled(isEnabled: Boolean) {
        update_review_save_button.isEnabled = isEnabled
    }

    private fun progressVisible(isVisible: Boolean) = update_review_progress.setVisible(isVisible)
}