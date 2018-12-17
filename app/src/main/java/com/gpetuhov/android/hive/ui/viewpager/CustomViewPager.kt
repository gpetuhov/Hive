package com.gpetuhov.android.hive.ui.viewpager

import android.content.Context
import android.view.MotionEvent
import android.util.AttributeSet
import androidx.viewpager.widget.ViewPager


class CustomViewPager(context: Context, attrs: AttributeSet) : ViewPager(context, attrs) {

    var scrollable: Boolean = true

    override fun onTouchEvent(event: MotionEvent) = if (scrollable) super.onTouchEvent(event) else false

    override fun onInterceptTouchEvent(event: MotionEvent) = if (scrollable) super.onInterceptTouchEvent(event) else false
}
