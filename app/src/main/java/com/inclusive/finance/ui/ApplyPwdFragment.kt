package com.inclusive.finance.ui

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatCheckedTextView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.ObjectUtils
import com.blankj.utilcode.util.SPUtils
import com.blankj.utilcode.util.ToastUtils
import com.dyhdyh.widget.loading.dialog.LoadingDialog
import com.hwangjr.rxbus.annotation.Subscribe
import com.hwangjr.rxbus.annotation.Tag
import com.hwangjr.rxbus.thread.EventThread
import com.inclusive.finance.DataCtrlClass
import com.inclusive.finance.R
import com.inclusive.finance.adapter.CustomExpandableListAdapter
import com.inclusive.finance.adapter.DepartDataAdapter
import com.inclusive.finance.adapter.UserDataAdapter
import com.inclusive.finance.app.ToolApplication
import com.inclusive.finance.base.MyBaseFragment
import com.inclusive.finance.bean.BaseTypeBean
import com.inclusive.finance.bean.SearchBean
import com.inclusive.finance.bean.TooBarBean
import com.inclusive.finance.bean.model.ApplyModel
import com.inclusive.finance.bean.model.MainActivityModel
import com.inclusive.finance.config.Constants
import com.inclusive.finance.config.Urls
import com.inclusive.finance.databinding.FragmentUserListBinding
import com.inclusive.finance.interfaces.PresenterClick
import com.inclusive.finance.pop.AddDepartPop
import com.inclusive.finance.pop.AddFenLeiPop
import com.inclusive.finance.pop.ChangePwdPop
import com.inclusive.finance.pop.DownPop
import com.inclusive.finance.room.StandardSystem
import com.inclusive.finance.room.SysLaiYuan
import com.inclusive.finance.room.User
import com.inclusive.finance.ui.login.ResgActivity
import com.inclusive.finance.utils.StatusBarUtil
import com.inclusive.finance.widget.SimpleDialogFactory
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener
import org.jetbrains.anko.support.v4.act
import org.jetbrains.anko.support.v4.startActivity


/**
 * 用户管理
 * */
class ApplyPwdFragment : MyBaseFragment(), PresenterClick, OnRefreshLoadMoreListener {
    private var listBiaoZhun: MutableList<StandardSystem>? = mutableListOf()
    private var groupName: String? = ""
    private var listBumen: MutableList<SysLaiYuan>? = arrayListOf()
    private var biaozhun: String = ""
    private var mAdapter2: DepartDataAdapter<SysLaiYuan>? = DepartDataAdapter()
    private var mbean: TooBarBean? = null
    private var mAdapter: UserDataAdapter<User>? = null
    var titleList: ArrayList<String>? = arrayListOf()
    lateinit var viewModel: MainActivityModel
    lateinit var viewBind: FragmentUserListBinding
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
        viewBind = FragmentUserListBinding.inflate(inflater, container, false).apply {
            data = viewModel
            presenterClick = this@ApplyPwdFragment
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
        mAdapter2 = DepartDataAdapter()
        viewBind.mRecyclerView2.layoutManager = LinearLayoutManager(activity)
        viewBind.mRecyclerView2.adapter = mAdapter2
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
        mAdapter = UserDataAdapter()
        viewBind.layoutBaseList.mRecyclerView.layoutManager = LinearLayoutManager(activity)
        viewBind.layoutBaseList.mRecyclerView.adapter = mAdapter
        viewBind.mRefreshLayout.setOnRefreshLoadMoreListener(this)
        mAdapter?.setEmptyView(LayoutInflater.from(context).inflate(R.layout.empty_view, null))
        mbean = TooBarBean()
        mbean?.type = 3
        mbean?.status = 1
        viewBind.actionBarCustom.dataType = mbean
        SearchBean?.add(SearchBean())
        SearchBean?.add(SearchBean())
        SearchBean?.add(SearchBean())
        mAdapter?.setOnItemClickListener { adapter, _, position ->
            mAdapter?.data?.forEachIndexed { index, recordsBean ->
                recordsBean.isClick = false
                adapter.notifyItemChanged(index)
            }
            mAdapter?.data?.get(position)?.isClick = true
            adapter.notifyItemChanged(position)
        }
        mAdapter2?.setOnItemClickListener { adapter, _, position ->
            mAdapter2?.data?.forEachIndexed { index, recordsBean ->
                recordsBean.isClick = false
                adapter.notifyItemChanged(index)
            }
            mAdapter2?.data?.get(position)?.isClick = true
            adapter.notifyItemChanged(position)
        }
        viewBind.dataType = mbean


        initApplyStatusView()
        var mulist = ArrayList<BaseTypeBean.Enum12>()
        var bean = BaseTypeBean.Enum12()
        bean.valueName = "标准一"
        bean.keyName = "标准一"
        mulist.add(bean)
        var bean2 = BaseTypeBean.Enum12()
        bean2.valueName = "标准二"
        bean2.keyName = "标准二"
        mulist.add(bean2)
        enums12?.put("标准体系", mulist)

        var list = ToolApplication.singletonTest(requireContext()).allDate
        if (list != null) {
            if (refreshState == Constants.RefreshState.STATE_REFRESH) {

                mAdapter?.setNewInstance(list = list)

            } else {
                mAdapter?.addData(list)

            }
            if (list.isNullOrEmpty()) {
                viewBind.mRefreshLayout.finishLoadMore()
                currentPage++
            } else {
                viewBind.mRefreshLayout.finishLoadMoreWithNoMoreData()
            }
        } else {

        }

    }

    private fun initDownView() {
        val dialog: Dialog = LoadingDialog.make(activity, SimpleDialogFactory())
            .setMessage("正在同步数据")
            .setCancelable(false)
            .create()
        dialog.show()

    }

    private var adapter2: CustomExpandableListAdapter? = null

    private fun initApplyStatusView() {
        val dataList = ArrayList<BaseTypeBean.Enum12>()
        dataList.clear()
        dataList.add(BaseTypeBean.Enum12().apply {
            valueName = "是否签批"
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

    private fun initStatusView() {
        titleList?.clear()
        enums12?.clear()
        if(listBiaoZhun!=null){
            listBiaoZhun?.forEachIndexed { index, standardSystem ->
                titleList?.add(standardSystem.StandardName)
                if(standardSystem.StandardsSecondName!=""){
                    var ml=standardSystem.StandardsSecondName?.split(",")
                    var mulist = ArrayList<BaseTypeBean.Enum12>()
                    ml?.forEachIndexed { index, s ->
                       if(s!=""){
                           mulist.add(BaseTypeBean.Enum12().apply {
                               valueName = s
                               keyName = s
                           })
                       }
                    }
                    enums12?.put(standardSystem.StandardName, mulist)
                }
            }
        }
//        titleList?.add("管理标准")//装备全寿命周期管理标准
//        var mulist = ArrayList<BaseTypeBean.Enum12>()
//        mulist.add(BaseTypeBean.Enum12().apply {
//            valueName = "装备全寿命周期管理标准"
//            keyName = "装备全寿命周期管理标准"
//        })
//
//        enums12?.put("管理标准", mulist)
//        titleList?.add("技术标准")
//        var mulist2 = ArrayList<BaseTypeBean.Enum12>()
//        mulist2.add(BaseTypeBean.Enum12().apply {
//            valueName = "基础标准"
//            keyName = "基础标准"
//        })
//        mulist2.add(BaseTypeBean.Enum12().apply {
//            valueName = "共性技术标准"
//            keyName = "共性技术标准"
//        })
//        mulist2.add(BaseTypeBean.Enum12().apply {
//            valueName = "专用装备标准"
//            keyName = "专用装备标准"
//        })
//
//        enums12?.put("技术标准", mulist2)
//        titleList?.add("工作标准")
//        var mulist3 = ArrayList<BaseTypeBean.Enum12>()
//        mulist3.add(BaseTypeBean.Enum12().apply {
//            valueName = "管理人员标准"
//            keyName = "管理人员标准"
//        })
//        mulist3.add(BaseTypeBean.Enum12().apply {
//            valueName = "操作人员标准"
//            keyName = "操作人员标准"
//        })
//        mulist3.add(BaseTypeBean.Enum12().apply {
//            valueName = "维护人员标准"
//            keyName = "维护人员标准"
//        })
//
//        enums12?.put("工作标准", mulist3)

        if (viewBind.expandableListView != null) {
            viewBind.expandableListView?.onItemLongClickListener =
                AdapterView.OnItemLongClickListener { parent, view, position, id ->
                    false
                }
            viewBind.expandableListView!!.setOnChildClickListener { parent, v, groupPosition, childPosition, id ->
                Log.e("yewuluoji","****fffffddddd"+childPosition)
                enums12?.get((titleList as ArrayList<String>)[groupPosition])
                    ?.forEachIndexed { index, enum12 ->
                        enum12.checked = index == childPosition
                    }
                enums12?.let { (adapter2 as CustomExpandableListAdapter?)?.setPoss(it) }
                when ((titleList as ArrayList<String>)[groupPosition]) {
                    "标准体系分类" -> {
                        biaozhun = enums12?.get((titleList as ArrayList<String>)[groupPosition])
                            ?.get(childPosition)?.keyName.toString()
                        enums12?.get((titleList as ArrayList<String>)[groupPosition])
                    }

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
            Log.e("yewuluoji","****fffff"+titleList?.get(groupPosition))
            groupName=titleList?.get(groupPosition)
            for (index in 0..titleList?.size!! - 1) {
                if (index == groupPosition) {

                } else {
                    viewBind.expandableListView?.collapseGroup(index)
                }
            }
        }

        viewBind.expandableListView!!.setOnGroupCollapseListener { groupPosition ->
            Log.e("yewuluoji","****"+groupPosition)
        }
    }

    var searchStr = " "
    var processStatus = ""
    override fun initEvent() {
        when (businessType) {
            ApplyModel.BUSINESS_TYPE_APPLY -> {
                getListUrl = Urls.sxsqList
                xdtbUrl = Urls.khglxdtb
                viewBind.actionBarCustom.mleft.text = getString(R.string.LITERATURE)
            }
            ApplyModel.BUSINESS_TYPE_SUNSHINE_APPLY -> {

            }
        }
    }

    override fun initData() {

        var list = ToolApplication.singletonTest(requireContext()).allDate
        if (list != null) {
            if (refreshState == Constants.RefreshState.STATE_REFRESH) {

                mAdapter?.setNewInstance(list = list)

            } else {
                mAdapter?.addData(list)

            }
            if (list.isNullOrEmpty()) {
                viewBind.mRefreshLayout.finishLoadMore()
                currentPage++
            } else {
                viewBind.mRefreshLayout.finishLoadMoreWithNoMoreData()
            }
        } else {

        }
        listBumen?.clear()
        listBumen =
            ToolApplication.singletonTest(requireContext()).getListLaiYuan("",0)
        listBiaoZhun =
            ToolApplication.singletonTest(requireContext()).getListBiaoZhunTiXi("","" )
        initStatusView()
        if (listBumen != null) {
            mAdapter2?.setNewInstance(listBumen)
        }
        val dataList = ArrayList<BaseTypeBean.Enum12>()
        dataList.clear()
        dataList.add(BaseTypeBean.Enum12().apply {
            valueName = "全部"
            keyName = ""
        })

        viewBind?.etdanwei?.setOnClickListener {
            DownPop(context,
                enums12 = dataList,
                checkedTextView = it as AppCompatCheckedTextView,
                isSingleChecked = true) { k, v, p ->
                processStatus = if (v == "全部") {
                    ""
                } else {
                    k
                }
            }.showPopupWindow(it)
        }
    }

    override fun refreshData(type: Int?) {
    }

    override fun onRefresh(refreshLayout: RefreshLayout) {
        refreshState = Constants.RefreshState.STATE_REFRESH
        currentPage = 1
        viewBind.root.postDelayed({ initData() }, 0)
    }

    override fun onResume() {
        super.onResume()
        initData()
    }

    override fun onLoadMore(refreshLayout: RefreshLayout) {
        refreshState = Constants.RefreshState.STATE_LOAD_MORE
        initData()
    }

    val select1: SysLaiYuan?
        get() {
            for (data in mAdapter2?.data!!) {
                if (data.isClick) {
                    return data
                }
            }
            return null
        }
    val select: User?
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
    var tongbupager=1
    override fun onClick(v: View?) {


        if (v == viewBind.btSearch) {
            var list = ToolApplication.singletonTest(requireContext())
                .getSelectDate(viewBind?.etUsername?.text?.toString(),
                    viewBind?.etRealname?.text?.toString(),
                    processStatus,
                    0)

            if (list != null) {
                if (refreshState == Constants.RefreshState.STATE_REFRESH) {

                    mAdapter?.setNewInstance(list = list)

                } else {
                    mAdapter?.addData(list)

                }
                if (list.isNullOrEmpty()) {
                    viewBind.mRefreshLayout.finishLoadMore()
                    currentPage++
                } else {
                    viewBind.mRefreshLayout.finishLoadMoreWithNoMoreData()
                }
            } else {
                mAdapter?.setNewInstance(list = list)
                Log.e("数据刷新", "list" + list)
            }

            return
        }
        when (v) {
            viewBind.btTongbu->{
                tongbupager=1
                initDownView()
                getData()
            }
            viewBind.tvAdd->{
                AddFenLeiPop(context, "1","","") {
                    ToastUtils.showShort("操作成功")
                    viewBind?.mRefreshLayout?.autoRefresh()
                }.show(this.childFragmentManager, this.javaClass.name)
            }
            viewBind.btUser -> {
                mbean = TooBarBean()
                mbean?.type = 3
                mbean?.status = 1
                viewBind.dataType = mbean
                viewBind.layoutBaseList?.mRecyclerView?.visibility = View.VISIBLE
            }
            viewBind.btDepart -> {
                mbean = TooBarBean()
                mbean?.type = 3
                mbean?.status = 2
                viewBind.dataType = mbean
                viewBind.layoutBaseList?.mRecyclerView?.visibility = View.GONE

            }
            viewBind.btType -> {
                mbean = TooBarBean()
                mbean?.type = 3
                mbean?.status = 3
                viewBind.dataType = mbean
                viewBind.layoutBaseList?.mRecyclerView?.visibility = View.GONE

            }
            viewBind.sbDiaocha -> {

                if (ObjectUtils.isNotEmpty(select)) {
                    ChangePwdPop(context, select?.id.toString()) {
                        ToastUtils.showShort("修改成功")
                    }.show(this.childFragmentManager, this.javaClass.name)
                } else {
                    ToastUtils.showLong("暂未选取数据")
                }

            }
            viewBind.sbUpdate -> {
                if (mbean?.status == 1)
                    if (ObjectUtils.isNotEmpty(select)) {
                        Log.e("ddddd", "dddd" + select?.id)
                        when (mbean?.status) {
                            1 -> startActivity<ResgActivity>("FROM" to "2",
                                "mid" to select?.id.toString())
                        }

                    } else {
                        ToastUtils.showLong("暂未选取数据")
                    }
                else if (mbean?.status == 2)
                    AddDepartPop(context, select1?.Name.toString(),select1?.id.toString()) {
                        ToastUtils.showShort("操作成功")
                        viewBind?.mRefreshLayout?.autoRefresh()
                    }.show(this.childFragmentManager, this.javaClass.name)
            }
            viewBind.sbShanchu -> {
                if (mbean?.status == 1)
                if (ObjectUtils.isNotEmpty(select)) {
                    ToolApplication.singletonTest(requireContext()).deleteOrder(select?.id)
                    ToastUtils.showShort("删除成功")
                    viewBind?.mRefreshLayout?.autoRefresh()
                } else {
                    ToastUtils.showLong("暂未选取数据")
                }
                else if (mbean?.status == 2)
                    if (ObjectUtils.isNotEmpty(select1)) {
                        ToolApplication.singletonTest(requireContext()).deleteDepart(select1?.id)
                        ToastUtils.showShort("删除成功")
                        viewBind?.mRefreshLayout?.autoRefresh()
                    } else {
                        ToastUtils.showLong("暂未选取数据")
                    }
            }
            viewBind.btAdd -> {
                if (mbean?.status == 1)
                    startActivity<ResgActivity>("FROM" to "3")
                else if (mbean?.status == 2)
                    AddDepartPop(context, "","") {
                        ToastUtils.showShort("操作成功")
                        viewBind?.mRefreshLayout?.autoRefresh()
                    }.show(this.childFragmentManager, this.javaClass.name)
                else {
                    AddFenLeiPop(context, "1","","") {
                        ToastUtils.showShort("操作成功")
                        viewBind?.mRefreshLayout?.autoRefresh()
                    }.show(this.childFragmentManager, this.javaClass.name)
                }
            }
        }


    }

    private fun getData() {

        DataCtrlClass.getSyncPageList(requireContext(), pageNum = tongbupager, {
            if(it!=null) {
                if (it?.size!!<1){
                    LoadingDialog.cancel()
                    return@getSyncPageList
                }
                it?.forEachIndexed { index, tongBuStandard ->
                    var mm = ToolApplication?.singletonTest(requireContext())?.getSearchListDate("BiaoNo",
                        tongBuStandard.no,
                        -1)
                    if (mm == null) {
                        var mr = ToolApplication.singletonTest(requireContext()).initStatandTable(
                            "" + tongBuStandard.baseCreateTime,
                            "" + tongBuStandard.baseModifyTime,
                            "" + tongBuStandard.baseCreatorId,
                            "" + tongBuStandard.baseModifierId,
                            "" + tongBuStandard.name,
                            "" + tongBuStandard.status,
                            "" + tongBuStandard.startTime,
                            "" + tongBuStandard.releaseTime,
                            "" + tongBuStandard.classNo,
                            "" + tongBuStandard.ratifyDepartmentName,
                            "" + tongBuStandard.ratifyDepartmentName,
                            "" + tongBuStandard.startDepartmentName,
                            "" + tongBuStandard.startDepartmentName,
                            "" + tongBuStandard.editorDepartmentName,
                            "" + tongBuStandard.editorDepartmentName,
                            "" + tongBuStandard.drafter,
                            "" + tongBuStandard.references,
                            "" + tongBuStandard.standards,
                            "" + tongBuStandard.filePath,
                            "" + tongBuStandard.remark,
                            "" + tongBuStandard.isCollect,
                            "" + tongBuStandard.no,
                            "" + tongBuStandard.startDepartmentName,
                            "" + tongBuStandard.sourceName,
                            "" + tongBuStandard.pages,
                            "" + tongBuStandard.sameClassNo,
                            "" + tongBuStandard.replaceNo,
                            "" + tongBuStandard.byReplaceNo,
                            "" + tongBuStandard.keywords,
                            "" + tongBuStandard.categoryName,
                            "" + tongBuStandard.sameTopicNo,
                        )

                    }
                     if (index == it?.size!! - 1) {
                        tongbupager++
                        getData()
                    }
                }
            }else{
                try {
                    LoadingDialog.cancel()
                } catch (e: Exception) {
                }
            }
        })
    }


    @Subscribe(thread = EventThread.MAIN_THREAD, tags = [Tag(Constants.BusAction.Bus_Refresh_List)])
    fun backRefresh(str: String) {
        viewBind?.mRefreshLayout?.autoRefresh()
    }
    @Subscribe(thread = EventThread.MAIN_THREAD, tags = [Tag("addsys")])
    fun addsys(str: String) {
        AddFenLeiPop(context, "2",""+str,"") {
            ToastUtils.showShort("操作成功")
            viewBind?.mRefreshLayout?.autoRefresh()
        }.show(this.childFragmentManager, this.javaClass.name)
    }
    @Subscribe(thread = EventThread.MAIN_THREAD, tags = [Tag("delsys")])
    fun delsys(str: String) {
        var temp=ToolApplication.singletonTest(requireContext()).deleteFenLei(str,"")
        if(temp) {
           viewBind?.mRefreshLayout?.autoRefresh()
        }
    }
    @Subscribe(thread = EventThread.MAIN_THREAD, tags = [Tag("delsys2")])
    fun delsys2(str: String) {
        var mlist=str.split(",")
        var mu =
            ToolApplication.singletonTest(requireContext()).getListBiaoZhunTiXi(mlist[0],"")
        var list2=mu.get(0).StandardsSecondName.split(",")
        var sb=StringBuffer()
         list2.forEachIndexed { index, s ->
             if(s==mlist[1]){

             }else{
                 sb.append(s+",")
             }
         }
        if (ToolApplication.singletonTest(requireContext()).updateBiaoZhunTiXi(
                sb.toString(),"StandardsSecondName",mlist[0]) == 0
        ) {
            viewBind?.mRefreshLayout?.autoRefresh()
        } else {
            ToastUtils.showShort("操作失败")
        }

    }
}