package com.gpetuhov.android.hive.util

import android.content.Context
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
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

fun Fragment.showToolbar(titleId: Int) {
    showToolbar(getString(titleId))
}

fun Fragment.showToolbar(title: String) {
    initToolbar(true, title, 0, { /* Do nothing */ }, { /* Do nothing */ })
}

fun Fragment.showToolbar(title: String, navigationIconId: Int, onNavigationClick: () -> Unit, onClick: () -> Unit) {
    initToolbar(true, title, navigationIconId, onNavigationClick, onClick)
}

fun Fragment.hideToolbar() {
    initToolbar(false, "", 0, { /* Do nothing */ }, { /* Do nothing */ })
}

fun Fragment.setToolbarTitle(title: String) {
    getToolbarTitle()?.text = title
}

// === Private methods ===

private fun Fragment.setActivitySoftInput(isResize: Boolean) {
    val resizeOrPan =
        if (isResize) WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE else WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN

    activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_UNCHANGED or resizeOrPan)
}

private fun Fragment.getBottomNavigationView() = activity?.findViewById<BottomNavigationView>(R.id.navigation_view)

private fun Fragment.getToolbar() = activity?.findViewById<Toolbar>(R.id.toolbar)

private fun Fragment.getToolbarImage() = activity?.findViewById<ImageView>(R.id.toolbar_image)

private fun Fragment.getToolbarTitle() = activity?.findViewById<TextView>(R.id.toolbar_title)

private fun Fragment.initToolbar(isVisible: Boolean, title: String, navigationIconId: Int, onNavigationClick: () -> Unit, onClick: () -> Unit) {
    val toolbar = getToolbar()

    toolbar?.visibility = if (isVisible) View.VISIBLE else  View.GONE
    toolbar?.title = title

    if (navigationIconId != 0) {
        toolbar?.setNavigationIcon(navigationIconId)
    } else {
        toolbar?.navigationIcon = null
    }

    toolbar?.setNavigationOnClickListener { onNavigationClick() }
    toolbar?.setOnClickListener { onClick() }
}
