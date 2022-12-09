package com.inclusive.finance.ui

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.bigkoo.pickerview.builder.TimePickerBuilder
import com.bigkoo.pickerview.view.TimePickerView
import com.blankj.utilcode.util.TimeUtils
import com.hwangjr.rxbus.annotation.Subscribe
import com.hwangjr.rxbus.annotation.Tag
import com.hwangjr.rxbus.thread.EventThread
import com.inclusive.finance.IRouter
import com.inclusive.finance.R
import com.inclusive.finance.adapter.CustomExpandableListAdapter
import com.inclusive.finance.adapter.SearchDataAdapter
import com.inclusive.finance.app.ToolApplication
import com.inclusive.finance.base.MyBaseFragment
import com.inclusive.finance.bean.*
import com.inclusive.finance.bean.model.ApplyModel
import com.inclusive.finance.bean.model.MainActivityModel
import com.inclusive.finance.config.Constants
import com.inclusive.finance.databinding.FragmentShoucangListBinding
import com.inclusive.finance.interfaces.PresenterClick
import com.inclusive.finance.pop.*
import com.inclusive.finance.room.Standard
import com.inclusive.finance.utils.StatusBarUtil
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener
import org.jetbrains.anko.support.v4.act
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.LinkedHashMap


/**
 * 收藏列表
 * */
class ShouCangListFragment : MyBaseFragment(), PresenterClick, OnRefreshLoadMoreListener {
    private var bean: ClickBean = ClickBean()
    private var startTime: String? = ""
    private var keyName: String = "Arc"
    private var endTime: String? = ""

    private var biaozhun: String = ""
    private var pizhun: String = ""

    private var tichu: String = ""
    var pager = 0
    private var adapter2: CustomExpandableListAdapter? = null
    private var mAdapter: SearchDataAdapter<Standard>? = null
    var titleList: ArrayList<String>? = arrayListOf()
    lateinit var viewModel: MainActivityModel
    lateinit var viewBind: FragmentShoucangListBinding
    private var refreshState = Constants.RefreshState.STATE_REFRESH
    private var currentPage = 1
    var event: Lifecycle.Event? = null
    var getListUrl = ""
    var xdtbUrl = ""
    var SearchBean: ArrayList<SearchBean>? = arrayListOf()
    private var businessType: Int = ApplyModel.BUSINESS_TYPE_APPLY
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        viewModel = ViewModelProvider(act).get(MainActivityModel::class.java)
        viewBind = FragmentShoucangListBinding.inflate(inflater, container, false).apply {
            data = viewModel
            presenterClick = this@ShouCangListFragment
            lifecycleOwner = viewLifecycleOwner
        }
        return viewBind.root
    }

    var enums12: LinkedHashMap<String, ArrayList<BaseTypeBean.Enum12>>? = LinkedHashMap()
    override fun initView() {
        businessType =
            arguments?.getInt("businessType", ApplyModel.BUSINESS_TYPE_APPLY) ?: businessType
        viewBind.actionBarCustom.toolbar.setNavigationOnClickListener {
            Navigation.findNavController(act, R.id.my_nav_host_fragment).navigateUp()
        }
        StatusBarUtil.setPaddingSmart(act, viewBind.actionBarCustom.appBar)
        mAdapter = SearchDataAdapter()
        mAdapter?.setEmptyView(LayoutInflater.from(context).inflate(R.layout.empty_view, null))
        viewBind.layoutBaseList.mRecyclerView.layoutManager = LinearLayoutManager(activity)
        viewBind.layoutBaseList.mRecyclerView.adapter = mAdapter
        SearchBean?.add(SearchBean())
        SearchBean?.add(SearchBean())
        SearchBean?.add(SearchBean())
        bean = ClickBean()
        bean.type = 0
        viewBind.dataType = bean
        var mbean = TooBarBean()
        mbean.type = 4
        viewBind.actionBarCustom.dataType = mbean
        viewBind.mRefreshLayout.setOnRefreshLoadMoreListener(this)

        viewBind.actionBarCustom.btSystem.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.navigation_ApplyPwdFragment);
        }
        viewBind.actionBarCustom.btSearch.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.navigation_ApplyLishiFragment);
        }
        viewBind.actionBarCustom.btShoucang.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.navigation_ShouCangListFragment);
        }
        viewBind.actionBarCustom.btFiel.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.navigation_ApplyWenJiantFragment);
        }
        initDownView()
        initStatusView()
        initApplyStatusView()

        mAdapter?.setOnItemClickListener { adapter, view, position ->
            IRouter.goF(view, R.id.action_to_navActivity, "文件详情", mAdapter?.data?.get(position)?.id)
        }

        viewBind.downEndDate1.setOnClickListener {
            val pvTime: TimePickerView = TimePickerBuilder(context) { date, _ ->
                endTime = TimeUtils.date2String(date, SimpleDateFormat("yyyy-MM-dd", Locale.CHINA))
                viewBind.downEndDate1.text = endTime
            }.isDialog(true).build()
            pvTime.dialog.window?.setGravity(Gravity.BOTTOM)
            pvTime.dialog.window?.setWindowAnimations(R.style.picker_view_slide_anim)
            pvTime.setOnDismissListener { viewBind.downEndDate1.isChecked = false }
            viewBind.downEndDate1.isChecked = true
            pvTime.show()

        }

        viewBind.downStartDate1.setOnClickListener {

            val pvTime: TimePickerView = TimePickerBuilder(context) { date, _ ->
                endTime = TimeUtils.date2String(date, SimpleDateFormat("yyyy-MM-dd", Locale.CHINA))
                viewBind.downStartDate1.text = endTime
            }.isDialog(true).build()
            pvTime.dialog.window?.setGravity(Gravity.BOTTOM)
            pvTime.dialog.window?.setWindowAnimations(R.style.picker_view_slide_anim)
            pvTime.setOnDismissListener { viewBind.downStartDate1.isChecked = false }
            viewBind.downStartDate1.isChecked = true
            pvTime.show()

        }
        if (viewBind.expandableListView != null) {
            viewBind.expandableListView!!.setOnChildClickListener { parent, v, groupPosition, childPosition, id ->

                enums12?.get((titleList as ArrayList<String>)[groupPosition])
                    ?.forEachIndexed { index, enum12 ->
                        enum12.checked = index == childPosition
                    }
                enums12?.let { (adapter2 as CustomExpandableListAdapter?)?.setPoss(it) }
                when ((titleList as ArrayList<String>)[groupPosition]) {
                    "标准体系" -> {
                        biaozhun = enums12?.get((titleList as ArrayList<String>)[groupPosition])
                            ?.get(childPosition)?.keyName.toString()
                        enums12?.get((titleList as ArrayList<String>)[groupPosition])
                    }
                    "批准单位" -> pizhun = enums12?.get((titleList as ArrayList<String>)[groupPosition])
                        ?.get(childPosition)?.keyName.toString()
                    "提出单位" -> tichu = enums12?.get((titleList as ArrayList<String>)[groupPosition])
                        ?.get(childPosition)?.keyName.toString()

                }
                false
            }
        }
        adapter2 = enums12?.let {
            CustomExpandableListAdapter(requireContext(),
                titleList as ArrayList<String>,
                it)
        }
        viewBind.expandableListView!!.setAdapter(adapter2)

        viewBind.expandableListView!!.setOnGroupExpandListener { groupPosition ->
            for (index in 0..titleList?.size!! - 1) {
                if (index == groupPosition) {

                } else {
                    viewBind.expandableListView?.collapseGroup(index)
                }
            }
        }

        viewBind.expandableListView!!.setOnGroupCollapseListener { groupPosition ->
        }

        viewBind?.btSearch?.setOnClickListener {
            keyName = viewBind?.etSearch?.text?.toString().toString()
            initData()
        }
        var chipId = R.id.bt_xiuding
        viewBind.chipGroup.setOnCheckedChangeListener { group, checkedId ->
            //            获取当前展示的fragment

            if (checkedId != -1) chipId = checkedId
            else {
                group.check(chipId)
                return@setOnCheckedChangeListener
            }
            when (checkedId) {
                R.id.bt_xiuding -> {

                }
                R.id.bt_feizhi -> {

                }
                R.id.bt_xiuding -> {

                }
                R.id.bt_xiuding -> {

                }
                R.id.bt_xiuding -> {

                }
                R.id.bt_xiuding -> {

                }
                R.id.bt_xiuding -> {

                }
                R.id.bt_xiuding -> {

                }
                else -> {
                    null
                }
            }

        }
    }

    private fun initDownView() {


    }

    private fun initApplyStatusView() {

    }

    private fun initStatusView() {

    }

    var searchStr = " "
    var processStatus = " "
    override fun initEvent() {
        when (businessType) {
            ApplyModel.BUSINESS_TYPE_APPLY -> {

            }
            ApplyModel.BUSINESS_TYPE_SUNSHINE_APPLY -> {

            }
        }
    }

    override fun initData() {


        var list =
            ToolApplication.singletonTest(requireContext()).getListStandardCollect("1", pager)
        viewBind.mRefreshLayout.finishRefresh()
        if (list != null) {
            viewBind.mRefreshLayout.finishRefresh()
            if (refreshState == Constants.RefreshState.STATE_REFRESH) {
                mAdapter?.setNewInstance(list = list as MutableList<Standard>?)
            } else {
                mAdapter?.addData(list)
            }
            if (!list.isNullOrEmpty()) {
                viewBind.mRefreshLayout.finishLoadMore()
                this.pager += 10
                currentPage++
            } else {
                viewBind.mRefreshLayout.finishLoadMoreWithNoMoreData()
            }

        } else {
            if (pager == 0) {
                mAdapter?.data?.clear()
                mAdapter?.notifyDataSetChanged()
            }
            viewBind.mRefreshLayout.finishLoadMoreWithNoMoreData()
        }

    }

    override fun refreshData(type: Int?) {
        viewBind.mRefreshLayout.autoRefresh()
    }

    override fun onRefresh(refreshLayout: RefreshLayout) {
        refreshState = Constants.RefreshState.STATE_REFRESH
        currentPage = 1
        viewBind.root.postDelayed({ initData() }, 0)
    }

    override fun onLoadMore(refreshLayout: RefreshLayout) {
        refreshState = Constants.RefreshState.STATE_LOAD_MORE
        initData()
    }

    private var mLastClickTime: Long = 0
    private val TIME_INTERVAL = 500L
    override fun onClick(v: View?) {


//        if (v == viewBind.btSearch) {
//            if (System.currentTimeMillis() - mLastClickTime > TIME_INTERVAL) {
//                refreshData()
//                mLastClickTime = System.currentTimeMillis()
//            }
//            return
//        }
        when (v) {
            viewBind.llPizhun -> {
                if (bean.type == 1) {
                    bean.type = 0
                    viewBind.dataType = bean
                } else {
                    bean.type = 1
                    viewBind.dataType = bean
                }
            }
        }

    }

    /**
    返回后刷新数据，
     */
    @Subscribe(thread = EventThread.MAIN_THREAD, tags = [Tag(Constants.BusAction.Bus_Refresh_List)])
    fun backRefresh(str: String) {
        pager=0
        viewBind.mRefreshLayout.autoRefresh()
    }


}