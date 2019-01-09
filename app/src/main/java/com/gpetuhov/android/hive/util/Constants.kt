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
            const val MAX_OFFER_TITLE_LENGTH = 30

            fun getZoomForLocation(location: LatLng): Float =
                if (
                    location.latitude == DEFAULT_LATITUDE
                    && location.longitude == DEFAULT_LONGITUDE
                ) {
                    Constants.Map.MIN_ZOOM
                } else {
                    Constants.Map.DEFAULT_ZOOM
                }
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

    class User {
        companion object {
            const val MAX_USERNAME_LENGTH = 30
            const val MAX_DESCRIPTION_LENGTH = 255
            const val MAX_VISIBLE_PHOTO_COUNT = 5
            const val VISIBLE_RATING_REVIEW_COUNT = 10
        }
    }

    class Offer {
        companion object {
            const val MAX_TITLE_LENGTH = 70
            const val MAX_DESCRIPTION_LENGTH = 255
            const val MAX_ACTIVE_OFFER_COUNT = 3
            const val DEFAULT_PRICE = 0.01
            const val MAX_VISIBLE_PHOTO_COUNT = 5
        }
    }

    class Image {
        companion object {
            const val USER_PIC_SIZE = 100
            const val USER_PHOTO_SIZE = 600
            const val OFFER_PHOTO_SIZE = 600
            const val JPEG_QUALITY = 70
        }
    }
}