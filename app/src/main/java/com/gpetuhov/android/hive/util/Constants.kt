package com.gpetuhov.android.hive.util

import com.google.android.gms.maps.model.LatLng
import com.gpetuhov.android.hive.R
import com.gpetuhov.android.hive.domain.model.Award

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
            const val MAX_STATUS_LENGTH = 70
            const val MAX_DESCRIPTION_LENGTH = 255
            const val MAX_VISIBLE_PHOTO_COUNT = 5
            const val VISIBLE_RATING_REVIEW_COUNT = 10
            const val USER_STATUS_LATENCY = 1000L
            const val NO_ACTIVITY = 100L
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

    class Awards {
        companion object {
            const val TEXT_MASTER_ID = 0
            const val OFFER_PROVIDER_ID = 1

            private val TEXT_MASTER = Award(TEXT_MASTER_ID, R.raw.textmaster, R.drawable.ic_text_master, R.drawable.ic_text_master_big, R.string.text_master, R.string.text_master_info, R.string.text_master_tip)
            private val OFFER_PROVIDER = Award(OFFER_PROVIDER_ID, R.raw.offerprovider, R.drawable.ic_offer_provider, R.drawable.ic_offer_provider_big, R.string.offer_provider, R.string.offer_provider_info, R.string.offer_provider_tip)

            private val AWARDS_MAP = hashMapOf(
                TEXT_MASTER_ID to TEXT_MASTER,
                OFFER_PROVIDER_ID to OFFER_PROVIDER
            )

            fun getAward(awardType: Int) = AWARDS_MAP[awardType] ?: TEXT_MASTER
        }
    }
}