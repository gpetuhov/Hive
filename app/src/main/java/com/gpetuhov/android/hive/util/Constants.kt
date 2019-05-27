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
            const val ALTRUIST_ID = 2
            const val GOOD_PROVIDER_ID = 3
            const val SUPER_PROVIDER_ID = 4
            const val NEWBIE_ID = 5
            const val REVIEWED_PROVIDER_ID = 6

            private val TEXT_MASTER = Award(TEXT_MASTER_ID, R.raw.textmaster, R.drawable.ic_text_master, R.drawable.ic_text_master_big, R.string.text_master, R.string.text_master_info, R.string.text_master_tip)
            private val OFFER_PROVIDER = Award(OFFER_PROVIDER_ID, R.raw.offerprovider, R.drawable.ic_offer_provider, R.drawable.ic_offer_provider_big, R.string.offer_provider, R.string.offer_provider_info, R.string.offer_provider_tip)
            private val ALTRUIST = Award(ALTRUIST_ID, R.raw.altruist, R.drawable.ic_altruist, R.drawable.ic_altruist_big, R.string.altruist, R.string.altruist_info, R.string.altruist_tip)
            private val GOOD_PROVIDER = Award(GOOD_PROVIDER_ID, R.raw.goodprovider, R.drawable.ic_good_provider, R.drawable.ic_good_provider_big, R.string.good_provider, R.string.good_provider_info, R.string.text_master_tip)
            private val SUPER_PROVIDER = Award(SUPER_PROVIDER_ID, R.raw.superprovider, R.drawable.ic_super_provider, R.drawable.ic_super_provider_big, R.string.super_provider, R.string.super_provider_info, R.string.text_master_tip)
            private val NEWBIE = Award(NEWBIE_ID, R.raw.newbie, R.drawable.ic_newbie, R.drawable.ic_newbie_big, R.string.newbie, R.string.newbie_info, R.string.text_master_tip)
            private val REVIEWED_PROVIDER = Award(REVIEWED_PROVIDER_ID, R.raw.reviewedprovider, R.drawable.ic_reviewed_provider, R.drawable.ic_reviewed_provider_big, R.string.reviewed_provider, R.string.reviewed_provider_info, R.string.text_master_tip)

            private val AWARDS_MAP = hashMapOf(
                TEXT_MASTER_ID to TEXT_MASTER,
                OFFER_PROVIDER_ID to OFFER_PROVIDER,
                ALTRUIST_ID to ALTRUIST,
                GOOD_PROVIDER_ID to GOOD_PROVIDER,
                SUPER_PROVIDER_ID to SUPER_PROVIDER,
                NEWBIE_ID to NEWBIE,
                REVIEWED_PROVIDER_ID to REVIEWED_PROVIDER
            )

            fun getAward(awardType: Int) = AWARDS_MAP[awardType] ?: TEXT_MASTER

            const val GOOD_PROVIDER_AWARD_MIN_REVIEW_COUNT = 10
            const val GOOD_PROVIDER_AWARD_MIN_RATING = 4.5F

            const val SUPER_PROVIDER_AWARD_MIN_REVIEW_COUNT = 100
            const val SUPER_PROVIDER_AWARD_MIN_RATING = 4.5F

            const val NEWBIE_AWARD_DAYS_LIMIT = 31
        }
    }
}