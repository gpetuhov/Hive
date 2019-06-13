package com.gpetuhov.android.hive.ui.epoxy.sort.models

import android.widget.RadioButton
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.gpetuhov.android.hive.R
import com.gpetuhov.android.hive.ui.epoxy.base.KotlinHolder

@EpoxyModelClass(layout = R.layout.sort_param_view)
abstract class SortParamModel : EpoxyModelWithHolder<SortParamHolder>() {

    @EpoxyAttribute var sortByTitle = false
    @EpoxyAttribute lateinit var onSortByTitleClick: () -> Unit

    @EpoxyAttribute var sortByPrice = false
    @EpoxyAttribute lateinit var onSortByPriceClick: () -> Unit

    @EpoxyAttribute var sortByRating = false
    @EpoxyAttribute lateinit var onSortByRatingClick: () -> Unit

    override fun bind(holder: SortParamHolder) {
        holder.sortByTitle.isChecked = sortByTitle
        holder.sortByTitle.setOnClickListener { onSortByTitleClick() }

        holder.sortByPrice.isChecked = sortByPrice
        holder.sortByPrice.setOnClickListener { onSortByPriceClick() }

        holder.sortByRating.isChecked = sortByRating
        holder.sortByRating.setOnClickListener { onSortByRatingClick() }
    }
}

class SortParamHolder : KotlinHolder() {
    val sortByTitle by bind<RadioButton>(R.id.sort_by_title)
    val sortByPrice by bind<RadioButton>(R.id.sort_by_price)
    val sortByRating by bind<RadioButton>(R.id.sort_by_rating)
}