package com.inclusive.finance.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatCheckedTextView
import androidx.databinding.DataBindingUtil
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.google.android.material.chip.Chip
import com.inclusive.finance.R
import com.inclusive.finance.bean.BaseTypeBean
import com.inclusive.finance.databinding.ItemDownChipBinding

class ItemDownAdapter<T : BaseTypeBean.Enum12> :
    BaseQuickAdapter<T, BaseViewHolder>(R.layout.item_down_chip, ArrayList()) {
    override fun onCreateDefViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return BaseViewHolder(ItemDownChipBinding.inflate(LayoutInflater.from(context), parent, false).root)
    }

    override fun convert(holder: BaseViewHolder, item: T) {
        DataBindingUtil.getBinding<ItemDownChipBinding>(holder.itemView)?.apply {
            this.data = item
            (root as AppCompatCheckedTextView).setTextColor(Color.parseColor(item.textColor))
        }

    }

}