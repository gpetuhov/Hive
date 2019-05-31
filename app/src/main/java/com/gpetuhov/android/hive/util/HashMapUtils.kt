package com.gpetuhov.android.hive.util

fun getStringKeyMapFromGeneric(genericMap: MutableMap<*, *>): MutableMap<String, Any?> {
    val stringMap = mutableMapOf<String, Any?>()

    for (key in genericMap.keys) {
        val value = genericMap[key]
        if (key is String) {
            stringMap[key] = value
        }
    }

    return stringMap
}