package com.gpetuhov.android.hive.util

import com.gpetuhov.android.hive.R

fun getAwardAnimationId(awardType: Int): Int {
    return when (awardType) {
        Constants.Award.TEXT_MASTER -> R.raw.textmaster
        else -> R.raw.gears
    }
}

fun getAwardImageId(awardType: Int): Int {
    return when (awardType) {
        Constants.Award.TEXT_MASTER -> R.drawable.ic_text_master_big
        else -> R.drawable.ic_text_master_big
    }
}

fun getAwardNameId(awardType: Int): Int {
    return when (awardType) {
        Constants.Award.TEXT_MASTER -> R.string.text_master
        else -> R.string.info
    }
}

fun getAwardDescriptionId(awardType: Int): Int {
    return when (awardType) {
        Constants.Award.TEXT_MASTER -> R.string.text_master_info
        else -> R.string.info
    }
}