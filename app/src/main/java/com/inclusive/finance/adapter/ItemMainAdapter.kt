package com.inclusive.finance.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import com.blankj.utilcode.util.SizeUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.inclusive.finance.IRouter
import com.inclusive.finance.R
import com.inclusive.finance.bean.MainMenuBean
import com.inclusive.finance.bean.model.MainActivityModel
import com.inclusive.finance.databinding.ItemMainBinding
import com.inclusive.finance.utils.GridAutofitLayoutManager

class ItemMainAdapter<T : MainMenuBean>(var viewModel: MainActivityModel) :
    BaseQuickAdapter<T, BaseViewHolder>(R.layout.item_main,ArrayList<T>()) {
    override fun onCreateDefViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return BaseViewHolder(
            ItemMainBinding.inflate(
                LayoutInflater.from(context),
                parent,
                false
            ).root
        )
    }
    private var mLastClickTime: Long = 0
    private val TIME_INTERVAL = 500L
    override fun convert(helper: BaseViewHolder, item: T) {
        DataBindingUtil.getBinding<ItemMainBinding>(helper.itemView)?.apply {
            this.data = item
//            mRecyclerView.layoutManager = GridAutofitLayoutManager(context, SizeUtils.dp2px(104f))
            mRecyclerView.layoutManager = GridLayoutManager(context,5)
            mRecyclerView.adapter=ItemMainMenuAdapter<MainMenuBean.MenuBean>().apply {
                setNewInstance(item.list)
                setOnItemClickListener { adapter, view, position ->
                    var action = item.list?.get(position)?.className
//                    if (action =="action_to_applyActivity"){
//                       if (viewModel.kehuBean.value?.creditId.isNullOrEmpty()) {
//                           action="action_to_keHuGuanLi"
//                       }
//                    }
                    if (System.currentTimeMillis() - mLastClickTime > TIME_INTERVAL) {
                        IRouter.goF(view, action)
                        mLastClickTime = System.currentTimeMillis()
                    }
                }
            }
        }

    }

}