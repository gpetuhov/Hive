package com.gpetuhov.android.hive.ui.epoxy.filter.models

import android.widget.CheckBox
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.gpetuhov.android.hive.R
import com.gpetuhov.android.hive.ui.epoxy.base.KotlinHolder
import com.gpetuhov.android.hive.ui.epoxy.base.bind

@EpoxyModelClass(layout = R.layout.filter_awards_view)
abstract class FilterAwardsModel : EpoxyModelWithHolder<FilterAwardsHolder>() {

    @EpoxyAttribute var hasSuperProvider = false
    @EpoxyAttribute lateinit var onHasSuperProviderClick: (Boolean) -> Unit

    @EpoxyAttribute var hasGoodProvider = false
    @EpoxyAttribute lateinit var onHasGoodProviderClick: (Boolean) -> Unit

    @EpoxyAttribute var hasRockStar = false
    @EpoxyAttribute lateinit var onHasRockStarClick: (Boolean) -> Unit

    @EpoxyAttribute var hasAdorableProvider = false
    @EpoxyAttribute lateinit var onHasAdorableProviderClick: (Boolean) -> Unit

    @EpoxyAttribute var hasFavoriteProvider = false
    @EpoxyAttribute lateinit var onHasFavoriteProviderClick: (Boolean) -> Unit

    @EpoxyAttribute var hasTextMaster = false
    @EpoxyAttribute lateinit var onHasTextMasterClick: (Boolean) -> Unit

    @EpoxyAttribute var hasNewbie = false
    @EpoxyAttribute lateinit var onHasNewbieClick: (Boolean) -> Unit

    override fun bind(holder: FilterAwardsHolder) {
        holder.hasSuperProvider.bind(hasSuperProvider) { onHasSuperProviderClick(it) }
        holder.hasGoodProvider.bind(hasGoodProvider) { onHasGoodProviderClick(it) }
        holder.hasRockStar.bind(hasRockStar) { onHasRockStarClick(it) }
        holder.hasAdorableProvider.bind(hasAdorableProvider) { onHasAdorableProviderClick(it) }
        holder.hasFavoriteProvider.bind(hasFavoriteProvider) { onHasFavoriteProviderClick(it) }
        holder.hasTextMaster.bind(hasTextMaster) { onHasTextMasterClick(it) }
        holder.hasNewbie.bind(hasNewbie) { onHasNewbieClick(it) }
    }
}

class FilterAwardsHolder : KotlinHolder() {
    val hasSuperProvider by bind<CheckBox>(R.id.filter_has_super_provider)
    val hasGoodProvider by bind<CheckBox>(R.id.filter_has_good_provider)
    val hasRockStar by bind<CheckBox>(R.id.filter_has_rock_star)
    val hasAdorableProvider by bind<CheckBox>(R.id.filter_has_adorable_provider)
    val hasFavoriteProvider by bind<CheckBox>(R.id.filter_has_favorite_provider)
    val hasTextMaster by bind<CheckBox>(R.id.filter_has_textmaster)
    val hasNewbie by bind<CheckBox>(R.id.filter_has_newbie)
}