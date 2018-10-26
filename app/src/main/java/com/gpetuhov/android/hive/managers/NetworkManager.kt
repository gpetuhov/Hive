package com.gpetuhov.android.hive.managers

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import com.gpetuhov.android.hive.application.HiveApp
import com.gpetuhov.android.hive.domain.network.Network
import javax.inject.Inject

class NetworkManager : Network {

    @Inject lateinit var context: Context

    init {
        HiveApp.appComponent.inject(this)
    }

    override fun isOnline(): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
        return activeNetwork?.isConnected == true
    }
}