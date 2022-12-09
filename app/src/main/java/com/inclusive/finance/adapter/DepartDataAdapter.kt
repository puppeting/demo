package com.inclusive.finance.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder

import com.inclusive.finance.R
import com.inclusive.finance.bean.SearchBean
import com.inclusive.finance.databinding.DepartItemLayoutBinding
import com.inclusive.finance.databinding.SearchItemLayoutBinding
import com.inclusive.finance.databinding.UserItemLayoutBinding
import com.inclusive.finance.room.SysDepartment
import com.inclusive.finance.room.SysLaiYuan
import com.inclusive.finance.room.User
import java.util.ArrayList



class DepartDataAdapter<T: SysLaiYuan> : BaseQuickAdapter<T, BaseViewHolder>(R.layout.depart_item_layout, ArrayList()) ,
    LoadMoreModule {
    override fun onCreateDefViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return BaseViewHolder(DepartItemLayoutBinding.inflate(LayoutInflater.from(context), parent, false).root)
    }
    override fun convert(helper: BaseViewHolder, item: T) {
        DataBindingUtil.getBinding<DepartItemLayoutBinding>(helper.itemView)?.apply {
            data=item
            setHelper(helper)
        }

    }
}