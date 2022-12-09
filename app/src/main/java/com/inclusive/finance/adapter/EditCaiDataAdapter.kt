package com.inclusive.finance.adapter

import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatEditText
import androidx.databinding.DataBindingUtil
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.hwangjr.rxbus.RxBus

import com.inclusive.finance.R
import com.inclusive.finance.bean.SearchBean
import com.inclusive.finance.databinding.CaiyongItemLayoutBinding
import com.inclusive.finance.databinding.EditcaiyongItemLayoutBinding
import com.inclusive.finance.databinding.SearchItemLayoutBinding
import com.inclusive.finance.databinding.UserItemLayoutBinding
import com.inclusive.finance.room.User
import java.util.ArrayList



class EditCaiDataAdapter<T: String> : BaseQuickAdapter<T, BaseViewHolder>(R.layout.editcaiyong_item_layout, ArrayList()) ,
    LoadMoreModule {
    override fun onCreateDefViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return BaseViewHolder(EditcaiyongItemLayoutBinding.inflate(LayoutInflater.from(context), parent, false).root)
    }
    override fun convert(helper: BaseViewHolder, item: T) {
        DataBindingUtil.getBinding<EditcaiyongItemLayoutBinding>(helper.itemView)?.apply {
//            data=item
            setHelper(helper)
        }
        var etName = helper?.itemView?.findViewById<AppCompatEditText>(R.id.tv_yinyongbiaozhu1)
        etName?.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
//                 item= etName.text.toString()
            }

        })
        if(item!="") {
                helper.setText(R.id.tv_yinyongbiaozhu1, item)
            }

        helper?.itemView?.findViewById<ImageView>(R.id.sb_yinyong_remove1)?.setOnClickListener {
            Log.e("dddd111","eeee")

            RxBus.get().post("delcai", ""+helper?.layoutPosition )

        }

    }
}