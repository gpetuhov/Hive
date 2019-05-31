package com.gpetuhov.android.hive.util

fun getStringMapFromGeneric(genericMap: MutableMap<*, *>): MutableMap<String, String> {
    val stringMap = mutableMapOf<String, String>()

    for (key in genericMap.keys) {
        val value = genericMap[key]
        if (key is String && value is String) {
            stringMap[key] = value
        }
    }

    return stringMap
}