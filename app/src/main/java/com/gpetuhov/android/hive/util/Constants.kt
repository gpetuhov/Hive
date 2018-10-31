package com.gpetuhov.android.hive.util

class Constants {
    class Map {
        companion object {
            const val DEFAULT_LATITUDE = 0.0
            const val DEFAULT_LONGITUDE = 0.0
            const val DEFAULT_ZOOM = 16.0F
            const val MIN_ZOOM = 10.0F
            const val MAX_ZOOM = 21.0F
            const val DEFAULT_BEARING = 0.0F
            const val DEFAULT_TILT = 0.0F
        }
    }

    class Location {
        companion object {
            const val UPDATE_INTERVAL = 30000L
            const val UPDATE_FASTEST_INTERVAL = 30000L
        }
    }

    class Auth {
        companion object {
            const val DEFAULT_USER_NAME = "Anonymous"
            const val DEFAULT_USER_MAIL = ""
        }
    }
}