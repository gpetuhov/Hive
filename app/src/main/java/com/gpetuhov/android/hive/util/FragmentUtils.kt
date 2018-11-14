package com.gpetuhov.android.hive.util

import android.view.WindowManager
import androidx.fragment.app.Fragment

fun Fragment.setActivitySoftInputResize() = setActivitySoftInput(true)

fun Fragment.setActivitySoftInputPan() = setActivitySoftInput(false)

private fun Fragment.setActivitySoftInput(isResize: Boolean) {
    val resizeOrPan =
        if (isResize) WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE else WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN

    activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_UNCHANGED or resizeOrPan)
}