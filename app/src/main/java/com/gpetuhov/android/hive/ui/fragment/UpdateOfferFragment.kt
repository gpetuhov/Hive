package com.gpetuhov.android.hive.ui.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.WhichButton
import com.afollestad.materialdialogs.actions.setActionButtonEnabled
import com.afollestad.materialdialogs.input.getInputField
import com.afollestad.materialdialogs.input.input
import com.airbnb.epoxy.EpoxyRecyclerView
import com.arellomobile.mvp.presenter.InjectPresenter
import com.gpetuhov.android.hive.R
import com.gpetuhov.android.hive.databinding.FragmentUpdateOfferBinding
import com.gpetuhov.android.hive.domain.model.User
import com.gpetuhov.android.hive.presentation.presenter.UpdateOfferFragmentPresenter
import com.gpetuhov.android.hive.presentation.view.UpdateOfferFragmentView
import com.gpetuhov.android.hive.ui.epoxy.offer.update.controller.UpdateOfferListController
import com.gpetuhov.android.hive.ui.fragment.base.BaseFragment
import com.gpetuhov.android.hive.ui.viewmodel.CurrentUserViewModel
import com.gpetuhov.android.hive.util.*
import com.pawegio.kandroid.toast
import kotlinx.android.synthetic.main.fragment_update_offer.*

class UpdateOfferFragment : BaseFragment(), UpdateOfferFragmentView {

    companion object {
        private const val RC_PHOTO_PICKER = 1001
    }

    @InjectPresenter lateinit var presenter: UpdateOfferFragmentPresenter

    private var controller: UpdateOfferListController? = null
    private var binding: FragmentUpdateOfferBinding? = null

    private var titleDialog: MaterialDialog? = null
    private var descriptionDialog: MaterialDialog? = null
    private var deleteOfferDialog: MaterialDialog? = null
    private var quitDialog: MaterialDialog? = null
    private var priceDialog: MaterialDialog? = null
    private var deletePhotoDialog: MaterialDialog? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Adjust_pan is needed to prevent activity from being pushed up by the keyboard
        setActivitySoftInputPan()

        hideToolbar()

        // Hide bottom navigation view, so that user can't quit without quit dialog
        hideBottomNavigationView()

        initDialogs()

        presenter.initOffer(UpdateOfferFragmentArgs.fromBundle(arguments).offerUid)

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_update_offer, container, false)
        binding?.presenter = presenter

        controller = UpdateOfferListController(presenter)
        controller?.onRestoreInstanceState(savedInstanceState)

        val updateOfferRecyclerView = binding?.root?.findViewById<EpoxyRecyclerView>(R.id.update_offer_recycler_view)
        updateOfferRecyclerView?.adapter = controller?.adapter

        updateUI()

        val viewModel = ViewModelProviders.of(this).get(CurrentUserViewModel::class.java)
        viewModel.currentUser.observe(this, Observer<User> { user ->
            presenter.updateReviews(user)
        })

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
        presenter.showQuitOfferUpdateDialog()
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_PHOTO_PICKER && resultCode == Activity.RESULT_OK) {
            val selectedImageUri = data?.data
            if (selectedImageUri != null) presenter.addPhoto(selectedImageUri)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        controller?.onSaveInstanceState(outState)
    }

    // === UpdateOfferFragmentView ===

    override fun showTitleDialog() {
        // Prefill dialog with text provided by presenter
        val editText = titleDialog?.getInputField()
        editText?.setText(presenter.getTitlePrefill())
        editText?.setSelection(editText.text.length)
        titleDialog?.show()
    }

    override fun dismissTitleDialog() = titleDialog?.dismiss() ?: Unit

    override fun showDescriptionDialog() {
        // Prefill dialog with text provided by presenter
        val editText = descriptionDialog?.getInputField()
        editText?.setText(presenter.getDescriptionPrefill())
        editText?.setSelection(editText.text.length)
        descriptionDialog?.show()
    }

    override fun dismissDescriptionDialog() = descriptionDialog?.dismiss() ?: Unit

    override fun disableButtons() = saveDeleteButtonsEnabled(false)

    override fun enableButtons() = saveDeleteButtonsEnabled(true)

    override fun showProgress() = progressVisible(true)

    override fun hideProgress() = progressVisible(false)

    override fun showDeleteOfferDialog() = deleteOfferDialog?.show() ?: Unit

    override fun dismissDeleteOfferDialog() = deleteOfferDialog?.dismiss() ?: Unit

    override fun showQuitOfferUpdateDialog() = quitDialog?.show() ?: Unit

    override fun dismissQuitOfferUpdateDialog() = quitDialog?.dismiss() ?: Unit

    override fun showPriceDialog() {
        // Prefill dialog with text provided by presenter
        val editText = priceDialog?.getInputField()
        editText?.setText(presenter.getPricePrefill())
        editText?.setSelection(editText.text.length)
        priceDialog?.show()
    }

    override fun dismissPriceDialog() = priceDialog?.dismiss() ?: Unit

    override fun choosePhoto() = startPhotoPicker(RC_PHOTO_PICKER)

    override fun showDeletePhotoDialog() = deletePhotoDialog?.show() ?: Unit

    override fun dismissDeletePhotoDialog() = deletePhotoDialog?.dismiss() ?: Unit

    override fun openPhotos(photoUrlList: MutableList<String>) {
        val photoBundle = Bundle()
        photoBundle.putStringArrayList(PhotoFragment.PHOTO_URL_LIST_KEY, ArrayList(photoUrlList))

        val action = UpdateOfferFragmentDirections.actionUpdateOfferFragmentToPhotoFragment(photoBundle)
        findNavController().navigate(action)
    }

    override fun openReviews(offerUid: String) {
        val action = UpdateOfferFragmentDirections.actionUpdateOfferFragmentToReviewsFragment(offerUid, true)
        findNavController().navigate(action)
    }

    override fun updateUI() = controller?.requestModelBuild() ?: Unit

    override fun navigateUp() {
        findNavController().navigateUp()
        hideSoftKeyboard()
    }

    override fun showToast(message: String) {
        toast(message)
    }

    // === Private methods ===

    private fun initDialogs() {
        initTitleDialog()
        initDescriptionDialog()
        initDeleteOfferDialog()
        initQuitDialog()
        initPriceDialog()
        initDeletePhotoDialog()
    }

    private fun initTitleDialog() {
        if (context != null) {
            titleDialog = MaterialDialog(context!!)
                .title(R.string.offer_title)
                .noAutoDismiss()
                .cancelable(false)
                .input(
                    maxLength = Constants.Offer.MAX_TITLE_LENGTH,
                    hintRes = R.string.add_title,
                    waitForPositiveButton = false
                ) { dialog, text ->
                    val inputText = text.toString()
                    val positiveButtonEnabled = inputText.length <= Constants.Offer.MAX_TITLE_LENGTH
                    presenter.updateTempTitle(inputText)
                    dialog.setActionButtonEnabled(WhichButton.POSITIVE, positiveButtonEnabled)
                }
                .positiveButton { presenter.saveTitle() }
                .negativeButton { presenter.dismissTitleDialog() }
        }
    }

    private fun initDescriptionDialog() {
        if (context != null) {
            descriptionDialog = MaterialDialog(context!!)
                .title(R.string.offer_description)
                .noAutoDismiss()
                .cancelable(false)
                .input(
                    maxLength = Constants.Offer.MAX_DESCRIPTION_LENGTH,
                    inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_MULTI_LINE,
                    hintRes = R.string.add_description,
                    waitForPositiveButton = false
                ) { dialog, text ->
                    val inputText = text.toString()
                    val positiveButtonEnabled = inputText.length <= Constants.Offer.MAX_DESCRIPTION_LENGTH
                    presenter.updateTempDescription(inputText)
                    dialog.setActionButtonEnabled(WhichButton.POSITIVE, positiveButtonEnabled)
                }
                .positiveButton { presenter.saveDescription() }
                .negativeButton { presenter.dismissDescriptionDialog() }
        }
    }

    private fun initDeleteOfferDialog() {
        if (context != null) {
            deleteOfferDialog = MaterialDialog(context!!)
                .title(R.string.delete_offer)
                .message(R.string.prompt_delete_offer)
                .noAutoDismiss()
                .cancelable(false)
                .positiveButton { presenter.deleteOffer() }
                .negativeButton { presenter.deleteOfferCancel() }
        }
    }

    private fun initQuitDialog() {
        if (context != null) {
            quitDialog = MaterialDialog(context!!)
                .title(R.string.quit)
                .message(R.string.quit_offer_update_prompt)
                .noAutoDismiss()
                .cancelable(false)
                .positiveButton { presenter.quitOfferUpdate() }
                .negativeButton { presenter.quitOfferUpdateCancel() }
        }
    }

    private fun initPriceDialog() {
        if (context != null) {
            priceDialog = MaterialDialog(context!!)
                .title(R.string.offer_price)
                .noAutoDismiss()
                .cancelable(false)
                .input(
                    inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL,
                    hintRes = R.string.enter_price,
                    waitForPositiveButton = false
                ) { dialog, text ->
                    val inputField = dialog.getInputField()

                    var positiveButtonEnabled: Boolean
                    var error: String? = null

                    val isEmpty = text.toString().isEmpty()

                    if (!isEmpty) {
                        try {
                            val number = text.toString().toDouble()

                            if (number >= 0.01) {
                                presenter.updateTempPrice(number)
                                positiveButtonEnabled = true
                            } else {
                                error = "Must not be less than 0.01"
                                positiveButtonEnabled = false
                            }

                        } catch (e: Exception) {
                            error = "Not a number"
                            positiveButtonEnabled = false
                        }

                    } else {
                        positiveButtonEnabled = false
                    }

                    inputField?.error = error
                    dialog.setActionButtonEnabled(WhichButton.POSITIVE, positiveButtonEnabled)
                }
                .positiveButton { presenter.savePrice() }
                .negativeButton { presenter.dismissPriceDialog() }
        }
    }

    private fun initDeletePhotoDialog() {
        if (context != null) {
            deletePhotoDialog = MaterialDialog(context!!)
                .title(R.string.delete_photo)
                .message(R.string.prompt_delete_photo)
                .noAutoDismiss()
                .cancelable(false)
                .positiveButton { presenter.deletePhoto() }
                .negativeButton { presenter.deletePhotoCancel() }
        }
    }

    private fun dismissDialogs() {
        dismissTitleDialog()
        dismissDescriptionDialog()
        dismissDeleteOfferDialog()
        dismissQuitOfferUpdateDialog()
        dismissPriceDialog()
        dismissDeletePhotoDialog()
    }

    private fun saveDeleteButtonsEnabled(isEnabled: Boolean) {
        update_offer_save_button.isEnabled = isEnabled
        update_offer_delete_button.isEnabled = isEnabled
    }

    private fun progressVisible(isVisible: Boolean) = update_offer_progress.setVisible(isVisible)
}