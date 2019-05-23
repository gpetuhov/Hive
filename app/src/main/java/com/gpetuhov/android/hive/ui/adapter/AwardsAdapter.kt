package com.gpetuhov.android.hive.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.gpetuhov.android.hive.R
import com.gpetuhov.android.hive.databinding.ItemAwardBinding
import com.gpetuhov.android.hive.util.getAwardImageId
import com.gpetuhov.android.hive.util.getAwardNameId

class AwardsAdapter : RecyclerView.Adapter<AwardsAdapter.AwardViewHolder>() {

    private var awardsList = mutableListOf<Int>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AwardViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: ItemAwardBinding = DataBindingUtil.inflate(layoutInflater, R.layout.item_award, parent, false)

        return AwardViewHolder(binding)
    }

    override fun getItemCount() = awardsList.size

    override fun onBindViewHolder(holder: AwardViewHolder, position: Int) {
        val awardType = awardsList[position]

        val awardImageId = getAwardImageId(awardType)
        val awardNameId = getAwardNameId(awardType)

        holder.bindAwardImage(awardImageId)
        holder.binding.awardName = holder.binding.root.context.getString(awardNameId)
        holder.binding.executePendingBindings() // This line is important, it will force to load the variable in a custom view
    }

    // === Public methods ===

    fun setAwards(awardsList: MutableList<Int>) {
        this.awardsList.clear()
        this.awardsList.addAll(awardsList)
        notifyDataSetChanged()
    }

    // === Inner classes ===

    class AwardViewHolder(var binding: ItemAwardBinding) : RecyclerView.ViewHolder(binding.root) {

        private var awardImage = binding.root.findViewById<ImageView>(R.id.award_item_image)

        fun bindAwardImage(awardImageId: Int) = awardImage.setImageResource(awardImageId)
    }
}