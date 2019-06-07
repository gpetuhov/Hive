package com.gpetuhov.android.hive.ui.fragment.base

import android.text.InputType
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.WhichButton
import com.afollestad.materialdialogs.actions.setActionButtonEnabled
import com.afollestad.materialdialogs.input.getInputField
import com.afollestad.materialdialogs.input.input
import com.gpetuhov.android.hive.R
import com.gpetuhov.android.hive.util.moxy.MvpAppCompatFragment

// This class is needed to handle back pressed inside fragments
open class BaseFragment : MvpAppCompatFragment() {

    // === Public methods ===

    // Return true if back button click is handled by fragment
    open fun onBackPressed() = false

    // === Protected methods ===

    protected fun getInputDialog(
        titleId: Int,
        hintId: Int,
        errorMessageId: Int = R.string.username_not_valid,
        maxLength: Int? = null,
        inputType: Int = InputType.TYPE_CLASS_TEXT,
        onInputChange: (String) -> Unit,
        isInputValid: (String) -> Boolean,
        onPositive: () -> Unit,
        onNegative: () -> Unit
    ): MaterialDialog? {

        if (context != null) {
            val errorMessage = context?.getString(errorMessageId)

            return MaterialDialog(context!!)
                .title(titleId)
                .noAutoDismiss()
                .cancelable(false)
                .input(
                    maxLength = maxLength,
                    inputType = inputType,
                    hintRes = hintId,
                    waitForPositiveButton = false
                ) { dialog, text ->
                    val inputText = text.toString()
                    onInputChange(inputText)

                    val inputValid = isInputValid(inputText)
                    dialog.getInputField()?.error = if (!inputValid) errorMessage else null

                    val lengthValid = if (maxLength != null) inputText.length <= maxLength else true

                    // Enable positive button if input text and length is valid
                    // (for invalid length we do not show any error message).
                    dialog.setActionButtonEnabled(WhichButton.POSITIVE, inputValid && lengthValid)
                }
                .positiveButton { onPositive() }
                .negativeButton { onNegative() }

        } else {
            return null
        }
    }

    protected fun showDialog(dialog: MaterialDialog?, prefill: String) {
        // Prefill dialog corresponding prefill text (provided by presenter)
        val editText = dialog?.getInputField()
        editText?.setText(prefill)
        editText?.setSelection(editText.text.length)
        dialog?.show()
    }
}