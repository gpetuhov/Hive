package com.gpetuhov.android.hive.ui.epoxy.search.models

import android.widget.TextView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.gpetuhov.android.hive.R
import com.gpetuhov.android.hive.ui.epoxy.base.KotlinHolder

@EpoxyModelClass(layout = R.layout.search_totals_view)
abstract class SearchTotalsModel : EpoxyModelWithHolder<SearchTotalsHolder>() {

    @EpoxyAttribute lateinit var searchTotals: String

    override fun bind(holder: SearchTotalsHolder) {
        holder.searchTotals.text = searchTotals
    }
}

class SearchTotalsHolder : KotlinHolder() {
    val searchTotals by bind<TextView>(R.id.search_totals_count)
}