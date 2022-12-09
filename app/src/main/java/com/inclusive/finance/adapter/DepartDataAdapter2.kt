package com.inclusive.finance.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder

import com.inclusive.finance.R
import com.inclusive.finance.bean.SearchBean
import com.inclusive.finance.databinding.DepartItemLayout2Binding
import com.inclusive.finance.databinding.DepartItemLayoutBinding
import com.inclusive.finance.databinding.SearchItemLayoutBinding
import com.inclusive.finance.databinding.UserItemLayoutBinding
import com.inclusive.finance.room.SysDepartment
import com.inclusive.finance.room.User
import java.util.ArrayList



class DepartDataAdapter2<T: SysDepartment> : BaseQuickAdapter<T, BaseViewHolder>(R.layout.depart_item_layout2, ArrayList()) ,
    LoadMoreModule {
    override fun onCreateDefViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return BaseViewHolder(DepartItemLayout2Binding.inflate(LayoutInflater.from(context), parent, false).root)
    }
    var pos=-1
    override fun convert(helper: BaseViewHolder, item: T) {
        DataBindingUtil.getBinding<DepartItemLayout2Binding>(helper.itemView)?.apply {
            data=item
            setHelper(helper)
        }
        if(pos==helper?.layoutPosition){
            helper.setBackgroundColor(R.id.ll, context.getColor(R.color.color_49a0ed)
            )
        }else{
            helper.setBackgroundColor(R.id.ll, context.getColor(R.color.white)
            )
        }
    }
}