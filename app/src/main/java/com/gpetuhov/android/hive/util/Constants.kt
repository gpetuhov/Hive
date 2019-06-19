package com.gpetuhov.android.hive.util

import com.google.android.gms.location.DetectedActivity
import com.google.android.gms.maps.model.LatLng
import com.gpetuhov.android.hive.R
import com.gpetuhov.android.hive.domain.model.Award
import com.gpetuhov.android.hive.domain.model.UserActivity

class Constants {
    class Map {
        companion object {
            const val DEFAULT_LATITUDE = 0.0
            const val DEFAULT_LONGITUDE = 0.0
            const val MIN_ZOOM = 10.0F
            const val MAX_ZOOM = 21.0F
            const val DEFAULT_BEARING = 0.0F
            const val DEFAULT_TILT = 0.0F
            const val DEFAULT_RADIUS = 1.0
            const val MAX_RADIUS = 20.0     // TODO: limit this to 2.0 km
            const val MAX_OFFER_TITLE_LENGTH = 30
            const val UPDATE_MARKERS_LATENCY = 500L
            const val SEARCH_START_LATENCY = 2000L

            val DEFAULT_LOCATION = LatLng(DEFAULT_LATITUDE, DEFAULT_LONGITUDE)

            private const val DEFAULT_ZOOM = 16.0F

            fun getZoomForLocation(location: LatLng): Float =
                if (location.latitude == DEFAULT_LATITUDE && location.longitude == DEFAULT_LONGITUDE) MIN_ZOOM else DEFAULT_ZOOM
        }
    }

    class Location {
        companion object {
            const val UPDATE_INTERVAL = 60000L
            const val UPDATE_FASTEST_INTERVAL = 60000L
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
            const val HIVECORE_ID = 7
            const val REVIEW_POSTER_ID = 8
            const val STORY_TELLER_ID = 9
            const val MEGA_CRITIC_ID = 10
            const val OFFER_FINDER_ID = 11
            const val OFLUMBUS_ID = 12
            const val FAVORITE_PROVIDER_ID = 13
            const val ADORABLE_PROVIDER_ID = 14
            const val ROCK_STAR_ID = 15
            const val FAVORITIZER_ID = 16

            const val GOOD_PROVIDER_AWARD_MIN_REVIEW_COUNT = 10
            const val GOOD_PROVIDER_AWARD_MIN_RATING = 4.5F

            const val SUPER_PROVIDER_AWARD_MIN_REVIEW_COUNT = 100
            const val SUPER_PROVIDER_AWARD_MIN_RATING = 4.5F

            const val NEWBIE_AWARD_DAYS_LIMIT = 31

            const val HIVECORE_AWARD_MIN_ACTIVE_OFFER_COUNT = 10

            const val STORY_TELLER_AWARD_MIN_REVIEW_COUNT = 10L

            const val MEGA_CRITIC_AWARD_MIN_REVIEW_COUNT = 100L

            const val OFLUMBUS_AWARD_MIN_REVIEW_COUNT = 10L

            const val ADORABLE_PROVIDER_AWARD_MIN_STAR_COUNT = 10L

            const val ROCK_STAR_AWARD_MIN_STAR_COUNT = 100L

            private val TEXT_MASTER = Award(TEXT_MASTER_ID, R.raw.textmaster, R.drawable.ic_text_master, R.drawable.ic_text_master_big, R.string.text_master, R.string.text_master_info, R.string.text_master_tip)
            private val OFFER_PROVIDER = Award(OFFER_PROVIDER_ID, R.raw.offerprovider, R.drawable.ic_offer_provider, R.drawable.ic_offer_provider_big, R.string.offer_provider, R.string.offer_provider_info, R.string.offer_provider_tip)
            private val ALTRUIST = Award(ALTRUIST_ID, R.raw.altruist, R.drawable.ic_altruist, R.drawable.ic_altruist_big, R.string.altruist, R.string.altruist_info, R.string.altruist_tip)
            private val GOOD_PROVIDER = Award(GOOD_PROVIDER_ID, R.raw.goodprovider, R.drawable.ic_good_provider, R.drawable.ic_good_provider_big, R.string.good_provider, R.string.good_provider_info, R.string.text_master_tip)
            private val SUPER_PROVIDER = Award(SUPER_PROVIDER_ID, R.raw.superprovider, R.drawable.ic_super_provider, R.drawable.ic_super_provider_big, R.string.super_provider, R.string.super_provider_info, R.string.text_master_tip)
            private val NEWBIE = Award(NEWBIE_ID, R.raw.newbie, R.drawable.ic_newbie, R.drawable.ic_newbie_big, R.string.newbie, R.string.newbie_info, R.string.text_master_tip)
            private val REVIEWED_PROVIDER = Award(REVIEWED_PROVIDER_ID, R.raw.reviewedprovider, R.drawable.ic_reviewed_provider, R.drawable.ic_reviewed_provider_big, R.string.reviewed_provider, R.string.reviewed_provider_info, R.string.text_master_tip)
            private val HIVECORE = Award(HIVECORE_ID, R.raw.hivecore, R.drawable.ic_hivecore, R.drawable.ic_hivecore_big, R.string.hive_core, R.string.hive_core_info, R.string.hive_core_tip)
            private val REVIEW_POSTER = Award(REVIEW_POSTER_ID, R.raw.reviewposter, R.drawable.ic_review_poster, R.drawable.ic_review_poster_big, R.string.review_poster, R.string.review_poster_info, R.string.review_poster_tip)
            private val STORY_TELLER = Award(STORY_TELLER_ID, R.raw.storyteller, R.drawable.ic_story_teller, R.drawable.ic_story_teller_big, R.string.story_teller, R.string.story_teller_info, R.string.story_teller_tip)
            private val MEGA_CRITIC = Award(MEGA_CRITIC_ID, R.raw.megacritic, R.drawable.ic_mega_critic, R.drawable.ic_mega_critic_big, R.string.mega_critic, R.string.mega_critic_info, R.string.mega_critic_tip)
            private val OFFER_FINDER = Award(OFFER_FINDER_ID, R.raw.offerfinder, R.drawable.ic_offer_finder, R.drawable.ic_offer_finder_big, R.string.offer_finder, R.string.offer_finder_info, R.string.offer_finder_tip)
            private val OFLUMBUS = Award(OFLUMBUS_ID, R.raw.oflumbus, R.drawable.ic_oflumbus, R.drawable.ic_oflumbus_big, R.string.oflumbus, R.string.oflumbus_info, R.string.oflumbus_tip)
            private val FAVORITE_PROVIDER = Award(FAVORITE_PROVIDER_ID, R.raw.favoriteprovider, R.drawable.ic_favorite_provider, R.drawable.ic_favorite_provider_big, R.string.favorite_provider, R.string.favorite_provider_info, R.string.text_master_tip)
            private val ADORABLE_PROVIDER = Award(ADORABLE_PROVIDER_ID, R.raw.adorableprovider, R.drawable.ic_adorable_provider, R.drawable.ic_adorable_provider_big, R.string.adorable_provider, R.string.adorable_provider_info, R.string.text_master_tip)
            private val ROCK_STAR = Award(ROCK_STAR_ID, R.raw.rockstar, R.drawable.ic_rock_star, R.drawable.ic_rock_star_big, R.string.rock_star, R.string.rock_star_info, R.string.text_master_tip)
            private val FAVORITIZER = Award(FAVORITIZER_ID, R.raw.favoritizer, R.drawable.ic_favoritizer, R.drawable.ic_favoritizer_big, R.string.favoritizer, R.string.favoritizer_info, R.string.favoritizer_tip)

            private val AWARDS_MAP = hashMapOf(
                TEXT_MASTER_ID to TEXT_MASTER,
                OFFER_PROVIDER_ID to OFFER_PROVIDER,
                ALTRUIST_ID to ALTRUIST,
                GOOD_PROVIDER_ID to GOOD_PROVIDER,
                SUPER_PROVIDER_ID to SUPER_PROVIDER,
                NEWBIE_ID to NEWBIE,
                REVIEWED_PROVIDER_ID to REVIEWED_PROVIDER,
                HIVECORE_ID to HIVECORE,
                REVIEW_POSTER_ID to REVIEW_POSTER,
                STORY_TELLER_ID to STORY_TELLER,
                MEGA_CRITIC_ID to MEGA_CRITIC,
                OFFER_FINDER_ID to OFFER_FINDER,
                OFLUMBUS_ID to OFLUMBUS,
                FAVORITE_PROVIDER_ID to FAVORITE_PROVIDER,
                ADORABLE_PROVIDER_ID to ADORABLE_PROVIDER,
                ROCK_STAR_ID to ROCK_STAR,
                FAVORITIZER_ID to FAVORITIZER
            )

            fun getAward(awardType: Int) = AWARDS_MAP[awardType] ?: TEXT_MASTER
        }
    }

    class UserActivities {
        companion object {
            private val STILL = UserActivity(R.raw.still, R.string.still_description)
            private val WALKING = UserActivity(R.raw.walking, R.string.walk_description)
            private val RUNNING = UserActivity(R.raw.running, R.string.running_description)
            private val BICYCLE = UserActivity(R.raw.bicycle, R.string.bicycle_description)
            private val VEHICLE = UserActivity(R.raw.vehicle, R.string.vehicle_description)

            private val ACTIVITY_MAP = hashMapOf(
                DetectedActivity.STILL to STILL,
                DetectedActivity.WALKING to WALKING,
                DetectedActivity.RUNNING to RUNNING,
                DetectedActivity.ON_BICYCLE to BICYCLE,
                DetectedActivity.IN_VEHICLE to VEHICLE
            )

            fun getUserActivity(userActivityType: Int) = ACTIVITY_MAP[userActivityType] ?: STILL
        }
    }

    class Chat {
        companion object {
            const val MAX_REAL_TIME_UPDATE_MESSAGES = 50L
            const val CHAT_ARCHIVE_PAGE_SIZE = 20
        }
    }
}