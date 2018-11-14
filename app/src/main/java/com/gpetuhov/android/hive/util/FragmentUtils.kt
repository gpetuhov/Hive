package com.gpetuhov.android.hive.util

import android.content.Context
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.gpetuhov.android.hive.R

// === Public methods ===

fun Fragment.setActivitySoftInputResize() = setActivitySoftInput(true)

fun Fragment.setActivitySoftInputPan() = setActivitySoftInput(false)

fun Fragment.hideSoftKeyboard() {
    if (activity != null) {
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view?.windowToken, 0)
    }
}

fun Fragment.showBottomNavigationView() {
    getBottomNavigationView()?.visibility = View.VISIBLE
}

fun Fragment.hideBottomNavigationView() {
    getBottomNavigationView()?.visibility = View.GONE
}

// === Private methods ===

private fun Fragment.setActivitySoftInput(isResize: Boolean) {
    val resizeOrPan =
        if (isResize) WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE else WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN

    activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_UNCHANGED or resizeOrPan)
}

private fun Fragment.getBottomNavigationView() = activity?.findViewById<BottomNavigationView>(R.id.navigation_view)