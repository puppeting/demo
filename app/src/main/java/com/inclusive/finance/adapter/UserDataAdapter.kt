package com.inclusive.finance.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import com.blankj.utilcode.util.SPUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.hwangjr.rxbus.RxBus

import com.inclusive.finance.R
import com.inclusive.finance.app.ToolApplication
import com.inclusive.finance.bean.SearchBean
import com.inclusive.finance.config.Constants
import com.inclusive.finance.databinding.SearchItemLayoutBinding
import com.inclusive.finance.databinding.UserItemLayoutBinding
import com.inclusive.finance.room.User
import java.util.ArrayList



class UserDataAdapter<T: User> : BaseQuickAdapter<T, BaseViewHolder>(R.layout.user_item_layout, ArrayList()) ,
    LoadMoreModule {
    override fun onCreateDefViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return BaseViewHolder(UserItemLayoutBinding.inflate(LayoutInflater.from(context), parent, false).root)
    }
    override fun convert(helper: BaseViewHolder, item: T) {
        DataBindingUtil.getBinding<UserItemLayoutBinding>(helper.itemView)?.apply {
            data=item
            setHelper(helper)
        }

        if(item.UserStatus=="待审核"){
             helper.itemView.findViewById<TextView>(R.id.tv_shenhe1).visibility= View.GONE

            helper.itemView.findViewById<TextView>(R.id.tv_shenhe_tongguo).visibility= View.VISIBLE
            helper.itemView.findViewById<TextView>(R.id.tv_shenhe_jujue).visibility= View.VISIBLE
            helper.itemView.findViewById<TextView>(R.id.tv_shenhe_tongguo).setOnClickListener {
                ToolApplication.singletonTest(context).updateStatus(
                     "通过",
                    item.id
                )
                RxBus.get().post(Constants.BusAction.Bus_Refresh_List,"")
            }
            helper.itemView.findViewById<TextView>(R.id.tv_shenhe_jujue).setOnClickListener {
                ToolApplication.singletonTest(context).updateStatus(
                    "拒绝",
                    item.id
                )
                RxBus.get().post(Constants.BusAction.Bus_Refresh_List,"")

            }
        }else{
            helper.itemView.findViewById<TextView>(R.id.tv_shenhe1).visibility= View.VISIBLE

            helper.itemView.findViewById<TextView>(R.id.tv_shenhe_tongguo).visibility= View.GONE
            helper.itemView.findViewById<TextView>(R.id.tv_shenhe_jujue).visibility= View.GONE

        }
    }
}