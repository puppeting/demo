package com.inclusive.finance.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatCheckedTextView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.SPUtils
import com.hwangjr.rxbus.annotation.Subscribe
import com.hwangjr.rxbus.annotation.Tag
import com.hwangjr.rxbus.thread.EventThread
import com.inclusive.finance.IRouter
import com.inclusive.finance.R
import com.inclusive.finance.adapter.SysLogDataAdapter
import com.inclusive.finance.app.ToolApplication
import com.inclusive.finance.base.MyBaseFragment
import com.inclusive.finance.bean.BaseTypeBean
import com.inclusive.finance.bean.SearchBean
import com.inclusive.finance.bean.TooBarBean
import com.inclusive.finance.bean.model.ApplyModel
import com.inclusive.finance.bean.model.MainActivityModel
import com.inclusive.finance.config.Constants
import com.inclusive.finance.config.Urls
import com.inclusive.finance.databinding.FragmentLishiListBinding
import com.inclusive.finance.pop.*
import com.inclusive.finance.room.Standard
import com.inclusive.finance.room.SysLog
import com.inclusive.finance.utils.StatusBarUtil
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener
import org.jetbrains.anko.support.v4.act
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.LinkedHashMap


/**
 * 历史记录
 * */
class ApplyLishiFragment : MyBaseFragment(), View.OnClickListener, OnRefreshLoadMoreListener {


    private var pager: Int=0
    private var mAdapter: SysLogDataAdapter<SysLog>? = null
    var titleList: ArrayList<String>? = arrayListOf( )
    lateinit var viewModel: MainActivityModel
    lateinit var viewBind: FragmentLishiListBinding
    private var refreshState = Constants.RefreshState.STATE_REFRESH
    private var currentPage = 1
     var event: Lifecycle.Event? = null
    var getListUrl=""
    var xdtbUrl=""
    var SearchBean:ArrayList<SearchBean>?= arrayListOf()
    private var businessType:Int=ApplyModel.BUSINESS_TYPE_APPLY
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        viewModel = ViewModelProvider(act).get(MainActivityModel::class.java)
        viewBind = FragmentLishiListBinding.inflate(inflater, container, false).apply {
            data = viewModel
             lifecycleOwner = viewLifecycleOwner
        }
        return viewBind.root
    }
    var enums12: LinkedHashMap<String, ArrayList<BaseTypeBean.Enum12>>?= LinkedHashMap()
    override fun initView() {
        businessType=arguments?.getInt("businessType",ApplyModel.BUSINESS_TYPE_APPLY)?:businessType
        viewBind.actionBarCustom.toolbar.setNavigationOnClickListener {
            Navigation.findNavController(act, R.id.my_nav_host_fragment).navigateUp()
        }
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
        if(SPUtils.getInstance().getString(Constants.SPUtilsConfig.SP_LOGINNAME)!="admin"){
            viewBind?.actionBarCustom?.btSystem?.visibility=View.GONE
            viewBind?.actionBarCustom?.btFiel?.visibility=View.GONE

        }
        StatusBarUtil.setPaddingSmart(act, viewBind.actionBarCustom.appBar)
        mAdapter = SysLogDataAdapter()
        viewBind.layoutBaseList.mRecyclerView.layoutManager = LinearLayoutManager(activity)
        viewBind.layoutBaseList.mRecyclerView.adapter = mAdapter
        viewBind.mRefreshLayout.setOnRefreshLoadMoreListener(this)
//        mAdapter?.setEmptyView(LayoutInflater.from(context).inflate(R.layout.empty_view, null))
        var mbean= TooBarBean()
        mbean.type=1
        viewBind.actionBarCustom.dataType=mbean
        SearchBean?.add(SearchBean())
        SearchBean?.add(SearchBean())
        SearchBean?.add(SearchBean())
         mAdapter?.setOnItemClickListener { adapter, _, position ->
            mAdapter?.data?.forEachIndexed { index, recordsBean ->
                recordsBean.isClick=false
                adapter.notifyItemChanged(index)
            }
            mAdapter?.data?.get(position)?.isClick = true
            adapter.notifyItemChanged(position)
        }

        initDownView()
        initStatusView()
        initApplyStatusView()
        var mulist=ArrayList<BaseTypeBean.Enum12>()
        var bean=BaseTypeBean.Enum12()
        bean.valueName="标准一"
        bean.keyName="标准一"
        mulist.add(bean)
        var bean2=BaseTypeBean.Enum12()
        bean2.valueName="标准二"
        bean2.keyName="标准二"
        mulist.add(bean2)
        enums12?.put("标准体系",mulist)
        mAdapter?.setOnItemClickListener { adapter, view, position ->
            IRouter.goF(view, R.id.action_to_navActivity, "文件详情", "")
        }
        viewBind?.btSearch?.setOnClickListener {  Log.e("输出结果","****的")
            viewBind?.mRefreshLayout?.autoRefresh() }
        viewBind?.mRefreshLayout?.autoRefresh()
    }

    private fun initDownView() {


    }

    private fun initApplyStatusView() {
        val dataList = ArrayList<BaseTypeBean.Enum12>()
         dataList.clear()
        dataList.add(BaseTypeBean.Enum12().apply {
            valueName = " "
            keyName = ""
        })
        dataList.add(BaseTypeBean.Enum12().apply {
            valueName = "是"
            keyName = "1"
        })
        dataList.add(BaseTypeBean.Enum12().apply {
            valueName = "否"
            keyName = "0"
        })

//        viewBind.tvApplyStatus.setOnClickListener {
//            DownPop(context, enums12 = dataList, checkedTextView = it as AppCompatCheckedTextView, isSingleChecked = true) { k, v, p ->
//                applyProcessStatus = k
//            }.showPopupWindow(it)
//        }
    }


    var searchStr = " "
    var processStatus = " "
    override fun initEvent() {
        when (businessType) {
            ApplyModel.BUSINESS_TYPE_APPLY -> {
                getListUrl=Urls.sxsqList
                xdtbUrl=Urls.khglxdtb
                viewBind.actionBarCustom.mleft.text =getString(R.string.LITERATURE)
            }
            ApplyModel.BUSINESS_TYPE_SUNSHINE_APPLY -> {

            }
        }
    }

    override fun initData() {
        if(SPUtils.getInstance().getString(Constants.SPUtilsConfig.SP_LOGINNAME)=="admin") {
            var mtemp=false
            if(viewBind?.etLoginName?.text?.toString()!=""&&viewBind?.btDanwei?.text?.toString()!=""&&
                viewBind?.etUserName?.text?.toString()!=""){
                mtemp=true
            }
            var list = ToolApplication.singletonTest(requireContext())
                .getSelectDate(viewBind?.etLoginName?.text?.toString(),
                    viewBind?.etUserName?.text?.toString(),
                    viewBind?.btDanwei?.text?.toString(),
                    pager)
            viewBind.mRefreshLayout.finishRefresh()

            if (mtemp&&list != null) {
                var listBumen =
                    ToolApplication.singletonTest(requireContext()).getSyslogHis(list[0].id, pager)
                if (listBumen != null) {
                    if (refreshState == Constants.RefreshState.STATE_REFRESH) {

                        mAdapter?.setNewInstance(list = listBumen)

                    } else {
                        mAdapter?.addData(listBumen)

                    }
                    if (!listBumen.isNullOrEmpty()) {
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
            } else {
                viewBind.mRefreshLayout.finishRefresh()
                var listBumen =
                    ToolApplication.singletonTest(requireContext()).getSyslogHis( pager)
                if (listBumen != null) {
                    if (refreshState == Constants.RefreshState.STATE_REFRESH) {

                        mAdapter?.setNewInstance(list = listBumen)

                    } else {
                        mAdapter?.addData(listBumen)

                    }
                    if (!listBumen.isNullOrEmpty()) {
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

        }else{
            viewBind?.ll?.visibility=View.GONE
            var listBumen =
                ToolApplication.singletonTest(requireContext()).getSyslogHis(SPUtils.getInstance().getInt(Constants.SPUtilsConfig.SP_PHONE), pager)
            if (listBumen != null) {
                if (refreshState == Constants.RefreshState.STATE_REFRESH) {

                    mAdapter?.setNewInstance(list = listBumen)

                } else {
                    mAdapter?.addData(listBumen)

                }
                if (!listBumen.isNullOrEmpty()) {
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
     }

    override fun refreshData(type: Int?) {
     }
    private fun initStatusView() {
        var listBumen =
            ToolApplication.singletonTest(requireContext()).getListBuMenDate("")
//        val dataList = ArrayList<BaseTypeBean.Enum12>()
//        dataList.clear()
//        dataList.add(BaseTypeBean.Enum12().apply {
//            valueName = "全部"
//            keyName = ""
//        })
//        if(listBumen!=null&&listBumen.size>0) {
//            listBumen.forEachIndexed { index, sysDepartment ->
//                dataList.add(BaseTypeBean.Enum12().apply {
//                    valueName = sysDepartment.departmentName
//                    keyName = sysDepartment.departmentName
//                })
//            }
//        }
//        viewBind?.btDanwei?.setOnClickListener {
//            DownPop(context, enums12 = dataList, checkedTextView = it as AppCompatCheckedTextView, isSingleChecked = true) { k, v, p ->
//                processStatus = if(v=="全部"){
//                    ""
//                }else{
//                    k
//                }
//            }.showPopupWindow(it)
//        }

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
    val select: SysLog?
        get() {
            for (data in mAdapter?.data!!) {
                if (data.isClick) {
                    return data
                }
            }
            return null
        }
    private var mLastClickTime: Long = 0
    private val TIME_INTERVAL = 500L
    override fun onClick(v: View?) {


        if (v == viewBind.btSearch) {

        }


    }

    /**
    返回后刷新数据，
     */
    @Subscribe(thread = EventThread.MAIN_THREAD, tags = [Tag(Constants.BusAction.Bus_Refresh_List)])
    fun backRefresh(str: String) {
        viewBind?.mRefreshLayout?.autoRefresh()
     }


}