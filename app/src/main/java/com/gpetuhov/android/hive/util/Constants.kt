package com.gpetuhov.android.hive.util

import com.google.android.gms.maps.model.LatLng

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
            const val DEFAULT_RADIUS = 1.0
            val DEFAULT_LOCATION = LatLng(DEFAULT_LATITUDE, DEFAULT_LONGITUDE)
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

    class FileTypes {
        companion object {
            const val IMAGE = "image/jpeg"
        }
    }

    class Offer {
        companion object {
            const val MAX_ACTIVE_OFFER_COUNT = 3
        }
    }
}