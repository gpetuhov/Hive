package com.gpetuhov.android.hive.utils

import com.gpetuhov.android.hive.domain.network.Network

// This mock is used in tests instead of the NetworkManager.
class TestNetworkManager : Network {
    var onlineResult = false

    override fun isOnline() = onlineResult
}