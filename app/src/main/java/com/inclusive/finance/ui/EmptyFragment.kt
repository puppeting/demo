package com.inclusive.finance.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.inclusive.finance.DataCtrlClass
import com.inclusive.finance.IRouter
import com.inclusive.finance.R
 import com.inclusive.finance.base.BaseActivity
import com.inclusive.finance.base.MyBaseFragment
import com.inclusive.finance.bean.BaseTypeBean
import com.inclusive.finance.bean.model.ApplyModel
import com.inclusive.finance.config.Urls

import com.inclusive.finance.databinding.FragmentEmptyBinding
import com.inclusive.finance.interfaces.PresenterClick
import com.inclusive.finance.utils.SZWUtils
import org.jetbrains.anko.support.v4.act

/**
 * 暂未开发
 *
 * */
class EmptyFragment : MyBaseFragment(), PresenterClick {
    lateinit var viewBind: FragmentEmptyBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        viewBind = FragmentEmptyBinding.inflate(inflater, container, false).apply {
            presenterClick = this@EmptyFragment
            lifecycleOwner = viewLifecycleOwner
        }
        return viewBind.root
    }

    override fun initView() {
    }
    override fun initData() {
    }


    override fun onClick(v: View?) {
    }

}