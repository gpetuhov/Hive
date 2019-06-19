package com.gpetuhov.android.hive.presentation.presenter.base

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

interface BaseChatPresenter {

    companion object {
        private const val MIN_SCROLL = 1
        private const val MIN_SCROLL_SUM = 200
    }

    var lastScrollPosition: Int
    var scrollSum: Int

    var scrollListener: RecyclerView.OnScrollListener

    fun initScrollListener() {
        scrollListener = object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                // Reset scroll sum if changed scroll direction
                if (scrollSum < 0 && dy > MIN_SCROLL || scrollSum > 0 && dy < -MIN_SCROLL) scrollSum = 0

                scrollSum += dy

                // Show scroll down button on messages list scroll down for more than MIN_SCROLL_SUM,
                // hide on scroll up for more than MIN_SCROLL_SUM.
                if (scrollSum > MIN_SCROLL_SUM) {
                    showScrollDownButton()
                } else if (scrollSum < -MIN_SCROLL_SUM) {
                    hideScrollDownButton()
                }
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                scrollSum = 0

                // Hide scroll down button, if reached bottom of the list
                if (!recyclerView.canScrollVertically(1)) {
                    hideScrollDownButton()
                }

                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    lastScrollPosition = (recyclerView.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
                }
            }
        }
    }

    fun showScrollDownButton()

    fun hideScrollDownButton()
}