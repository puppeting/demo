package com.inclusive.finance.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.inclusive.finance.R
import com.inclusive.finance.bean.MainMenuBean
import com.inclusive.finance.databinding.ItemMainMenuBinding
import com.inclusive.finance.glide.imageloder.GlideApp
import com.inclusive.finance.utils.SZWUtils

class ItemMainMenuAdapter<T : MainMenuBean.MenuBean> :
    BaseQuickAdapter<T, BaseViewHolder>(R.layout.item_main_menu,ArrayList<T>()) {
    override fun onCreateDefViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return BaseViewHolder(ItemMainMenuBinding.inflate(LayoutInflater.from(context),parent,false).root)
    }
    override fun convert(helper: BaseViewHolder, item: T) {
        DataBindingUtil.getBinding<ItemMainMenuBinding>(helper.itemView)?.apply {
            this.data=item
            SZWUtils.loadPhotoImg(context,item.img,img)
        }

    }

}