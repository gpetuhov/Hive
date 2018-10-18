package com.gpetuhov.android.hive.util

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.support.v4.content.ContextCompat

fun checkPermissions(context: Context): Boolean {
    return checkHasLocationPermissions(context)
}

fun checkHasLocationPermissions(context: Context): Boolean {
    return ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
            && ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
}