package com.gpetuhov.android.hive.ui.epoxy.sort.models

import android.widget.RadioButton
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.gpetuhov.android.hive.R
import com.gpetuhov.android.hive.ui.epoxy.base.KotlinHolder
import com.gpetuhov.android.hive.ui.epoxy.base.bind

@EpoxyModelClass(layout = R.layout.sort_param_view)
abstract class SortParamModel : EpoxyModelWithHolder<SortParamHolder>() {

    @EpoxyAttribute var sortByDistance = false
    @EpoxyAttribute lateinit var onSortByDistanceClick: () -> Unit

    @EpoxyAttribute var sortByTitle = false
    @EpoxyAttribute lateinit var onSortByTitleClick: () -> Unit

    @EpoxyAttribute var sortByPrice = false
    @EpoxyAttribute lateinit var onSortByPriceClick: () -> Unit

    @EpoxyAttribute var sortByRating = false
    @EpoxyAttribute lateinit var onSortByRatingClick: () -> Unit

    @EpoxyAttribute var sortByReviewCount = false
    @EpoxyAttribute lateinit var onSortByReviewCountClick: () -> Unit

    @EpoxyAttribute var sortByFavoriteStarCount = false
    @EpoxyAttribute lateinit var onSortByFavoriteStarCountClick: () -> Unit

    @EpoxyAttribute var sortByPhotoCount = false
    @EpoxyAttribute lateinit var onSortByPhotoCountClick: () -> Unit

    override fun bind(holder: SortParamHolder) {
        holder.sortByDistance.bind(sortByDistance) { onSortByDistanceClick() }
        holder.sortByTitle.bind(sortByTitle) { onSortByTitleClick() }
        holder.sortByPrice.bind(sortByPrice) { onSortByPriceClick() }
        holder.sortByRating.bind(sortByRating) { onSortByRatingClick() }
        holder.sortByReviewCount.bind(sortByReviewCount) { onSortByReviewCountClick() }
        holder.sortByFavoriteStarCount.bind(sortByFavoriteStarCount) { onSortByFavoriteStarCountClick() }
        holder.sortByPhotoCount.bind(sortByPhotoCount) { onSortByPhotoCountClick() }
    }
}

class SortParamHolder : KotlinHolder() {
    val sortByDistance by bind<RadioButton>(R.id.sort_by_distance)
    val sortByTitle by bind<RadioButton>(R.id.sort_by_title)
    val sortByPrice by bind<RadioButton>(R.id.sort_by_price)
    val sortByRating by bind<RadioButton>(R.id.sort_by_rating)
    val sortByReviewCount by bind<RadioButton>(R.id.sort_by_review_count)
    val sortByFavoriteStarCount by bind<RadioButton>(R.id.sort_by_favorite_star_count)
    val sortByPhotoCount by bind<RadioButton>(R.id.sort_by_photo_count)
}