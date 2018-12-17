package com.gpetuhov.android.hive.ui.adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.gpetuhov.android.hive.R
import com.gpetuhov.android.hive.ui.fragment.FavoriteOffersFragment
import com.gpetuhov.android.hive.ui.fragment.FavoriteUsersFragment

class FavoritesAdapter(private val context: Context?, fm: FragmentManager?) : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment = if (position == 0) {
        FavoriteUsersFragment()
    } else {
        FavoriteOffersFragment()
    }

    override fun getCount(): Int {
        return 2
    }

    override fun getPageTitle(position: Int): CharSequence? = if (position == 0) {
        context?.getString(R.string.users)
    } else {
        context?.getString(R.string.offers)
    }
}
