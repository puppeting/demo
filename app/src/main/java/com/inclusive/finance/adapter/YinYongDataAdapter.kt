package com.inclusive.finance.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder

import com.inclusive.finance.R
import com.inclusive.finance.bean.SearchBean
import com.inclusive.finance.databinding.CaiyongItemLayoutBinding
import com.inclusive.finance.databinding.SearchItemLayoutBinding
import com.inclusive.finance.databinding.UserItemLayoutBinding
import com.inclusive.finance.room.User
import java.util.ArrayList



class YinYongDataAdapter<T: String> : BaseQuickAdapter<T, BaseViewHolder>(R.layout.caiyong_item_layout, ArrayList()) ,
    LoadMoreModule {
    override fun onCreateDefViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return BaseViewHolder(CaiyongItemLayoutBinding.inflate(LayoutInflater.from(context), parent, false).root)
    }
    override fun convert(helper: BaseViewHolder, item: T) {
//        DataBindingUtil.getBinding<CaiyongItemLayoutBinding>(helper.itemView)?.apply {
////            data=item
//            setHelper(helper)
//        }
        Log.e("caiyong","****"+item.toString())
        helper.setText(R.id.tv_name,item)
    }
}