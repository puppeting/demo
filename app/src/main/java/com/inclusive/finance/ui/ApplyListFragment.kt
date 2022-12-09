package com.inclusive.finance.ui

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.TextView
import androidx.appcompat.widget.PopupMenu
import androidx.core.content.ContextCompat
import androidx.core.view.setPadding
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.android.arouter.launcher.ARouter
import com.bigkoo.pickerview.builder.TimePickerBuilder
import com.bigkoo.pickerview.view.TimePickerView
import com.blankj.utilcode.util.SPUtils
import com.blankj.utilcode.util.TimeUtils
import com.hwangjr.rxbus.RxBus
import com.hwangjr.rxbus.annotation.Subscribe
import com.hwangjr.rxbus.annotation.Tag
import com.hwangjr.rxbus.thread.EventThread
import com.inclusive.finance.IRouter
import com.inclusive.finance.R
import com.inclusive.finance.adapter.CustomExpandableListAdapter
import com.inclusive.finance.adapter.DataAdapter
import com.inclusive.finance.adapter.DepartDataAdapter2
import com.inclusive.finance.app.ToolApplication
import com.inclusive.finance.base.MyBaseFragment
import com.inclusive.finance.bean.*
import com.inclusive.finance.bean.model.ApplyModel
import com.inclusive.finance.bean.model.MainActivityModel
import com.inclusive.finance.config.Constants
import com.inclusive.finance.config.Urls
import com.inclusive.finance.databinding.FragmentApplyListBinding
import com.inclusive.finance.interfaces.PresenterClick
import com.inclusive.finance.pop.*
import com.inclusive.finance.room.Standard
import com.inclusive.finance.room.SysDepartment
import com.inclusive.finance.utils.StatusBarUtil
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener
import org.jetbrains.anko.support.v4.act
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.LinkedHashMap


/**
 * 授信申请列表
 * */
class ApplyListFragment : MyBaseFragment(), PresenterClick, OnRefreshLoadMoreListener {
    private var departpositon2: Int = -1
    private var departpositon1: Int = -1

    private var departpositon3: Int = -1
    private var departpositon4: Int = -1

    private var biaozhuntixiname: String? = ""
    private var biaozhunlaiyuanname: String = ""
    private var searchcaiyong: String = ""
    private var searchyinyong: String = ""
    private var tichubumenname: String? = ""
    private var pizhunmenname: String? = ""
    private var listpizhunBumen: MutableList<SysDepartment>? = arrayListOf()
    private var listBiaoZhunLaiYuan: MutableList<SysDepartment>? = arrayListOf()
    private var listTiXi: MutableList<SysDepartment>? = arrayListOf()

    private var listBumen: MutableList<SysDepartment>? = arrayListOf()
    private var departmAdapter2: DepartDataAdapter2<SysDepartment>? = null
    private var departmAdapter3: DepartDataAdapter2<SysDepartment>? = null
    private var departmAdapter4: DepartDataAdapter2<SysDepartment>? = null
    private var departmAdapter1: DepartDataAdapter2<SysDepartment>? = null

    private var accountPop: AccountPop2? = null
    private var totalCount: Int = 0
    private var bean: ClickBean = ClickBean()
    private var keyName: String = ""
    private var endTime: String? = ""
    private var biaozhun: String = ""
    var pager = 0
    private var adapter2: CustomExpandableListAdapter? = null
    private var mAdapter: DataAdapter<Standard>? = null
    var titleList: ArrayList<String>? = arrayListOf()
    lateinit var viewModel: MainActivityModel
    lateinit var viewBind: FragmentApplyListBinding
    private var refreshState = Constants.RefreshState.STATE_REFRESH
    private var currentPage = 1
    var event: Lifecycle.Event? = null
    var getListUrl = ""
    var xdtbUrl = ""
    var SearchBean: ArrayList<SearchBean>? = arrayListOf()
    var rvBiaozhunTemp = true
    var pizhunTemp = true
    var tichuTemp = true
    var laiyuanTemp = true

    private var businessType: Int = ApplyModel.BUSINESS_TYPE_APPLY
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        viewModel = ViewModelProvider(act).get(MainActivityModel::class.java)
        viewBind = FragmentApplyListBinding.inflate(inflater, container, false).apply {
            data = viewModel
            presenterClick = this@ApplyListFragment
            lifecycleOwner = viewLifecycleOwner
        }
        return viewBind.root
    }

    var enums12: LinkedHashMap<String, ArrayList<BaseTypeBean.Enum12>>? =
        LinkedHashMap<String, ArrayList<BaseTypeBean.Enum12>>()

    override fun initView() {
        businessType =
            arguments?.getInt("businessType", ApplyModel.BUSINESS_TYPE_APPLY) ?: businessType
        viewBind.actionBarCustom.toolbar.setNavigationOnClickListener {
            Navigation.findNavController(act, R.id.my_nav_host_fragment).navigateUp()
        }
        StatusBarUtil.setPaddingSmart(act, viewBind.actionBarCustom.appBar)
        mAdapter = DataAdapter()
        mAdapter?.setEmptyView(LayoutInflater.from(context).inflate(R.layout.empty_view, null))
        viewBind.layoutBaseList.mRecyclerView.layoutManager = LinearLayoutManager(activity)
        viewBind.layoutBaseList.mRecyclerView.adapter = mAdapter
        SearchBean?.add(SearchBean())
        SearchBean?.add(SearchBean())
        SearchBean?.add(SearchBean())
        viewBind?.actionBarCustom?.toolbar?.menu?.add(getString(R.string.userhelp))?.apply {
            setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
            actionView = TextView(context).apply {
                setPadding(15)
                setTextColor(resources.getColor(R.color.white))
                SPUtils.getInstance().getString(Constants.SPUtilsConfig.SP_REALNAME)
                    .also { text = it }
                if (isAdded)
                    setCompoundDrawablesRelativeWithIntrinsicBounds(null,
                        null,
                        ContextCompat.getDrawable(context, R.mipmap.xial),
                        null)
                setOnClickListener {
                    val popup = PopupMenu(act, this)
                    // Inflating the Popup using xml file
                    popup.menuInflater.inflate(R.menu.main_menu_toolbar_overflow, popup.menu)
                    popup.show()
                    popup.setOnMenuItemClickListener {
                        when (it.itemId) {
//                            R.id.item_user -> {
//                                UserInfoPop(context).showPopupWindow()
//                            }
                            R.id.item_account -> {
                                if (accountPop == null) {
                                    accountPop = AccountPop2(act as ApplyActivity)
//                                    viewModel.accountPop = accountPop
                                }
                                accountPop?.showPopupWindow()
                            }
                            R.id.item_askForLeave -> {
                                RxBus.get().post("dismiss", "dismiss")
                                ARouter.getInstance().build("/com/LoginPasswordActivity")
                                    .withTransition(R.anim.slide_in_bottom, android.R.anim.fade_out)
                                    .navigation(context)
                            }
                        }
                        true
                    }
                }
            }
        }
//        viewBind?.actionBarCustom?.toolbar?.menu?.add(getString(R.string.houqin))?.apply {
//            setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
//            actionView = TextView(context).apply {
//                setPadding(15)
//                setTextColor(resources.getColor(R.color.white))
//                SPUtils.getInstance().getString(Constants.SPUtilsConfig.SP_DepartmentName)
//                    .also { text = it }
//                if (isAdded)
//                    setOnClickListener {
//
//
//                    }
//            }
//        }
        bean = ClickBean()
        bean.type = 0
        viewBind.dataType = bean
        var mbean = TooBarBean()
        mbean.type = 0
        viewBind.actionBarCustom.dataType = mbean
        viewBind.mRefreshLayout.setOnRefreshLoadMoreListener(this)
        viewBind.actionBarCustom.btSystem.setOnClickListener {

            IRouter.goF(it, R.id.action_to_applyActivity, "", ApplyModel.BUSINESS_TYPE_APPLY, false)
        }
        viewBind.actionBarCustom.btSearch.setOnClickListener {
            IRouter.goF(it, R.id.action_to_applyActivity, "", ApplyModel.BUSINESS_TYPE_LISHI, false)

        }
        viewBind.actionBarCustom.btShoucang.setOnClickListener {
            IRouter.goF(it,
                R.id.action_to_applyActivity,
                "",
                ApplyModel.BUSINESS_TYPE_SHOUCANG,
                false)

        }
        viewBind.actionBarCustom.btFiel.setOnClickListener {
            IRouter.goF(it,
                R.id.action_to_applyActivity,
                "",
                ApplyModel.BUSINESS_TYPE_WENJIAN,
                false)

        }
        if (SPUtils.getInstance().getString(Constants.SPUtilsConfig.SP_LOGINNAME) != "admin") {
            viewBind?.actionBarCustom?.btShoucang?.visibility = View.GONE
            viewBind?.actionBarCustom?.btFiel?.visibility = View.GONE

        }
        initDownView()
        initStatusView()
        initApplyStatusView()
        departmAdapter1 = DepartDataAdapter2()
        viewBind.recyBiaozhuntixi.layoutManager = LinearLayoutManager(activity)
        viewBind.recyBiaozhuntixi.adapter = departmAdapter1
        departmAdapter1?.setNewInstance(listTiXi)
        departmAdapter2 = DepartDataAdapter2()
        viewBind.recyPizhun.layoutManager = LinearLayoutManager(activity)
        viewBind.recyPizhun.adapter = departmAdapter2
        departmAdapter2?.setNewInstance(listpizhunBumen)
        departmAdapter4 = DepartDataAdapter2()
        viewBind.recyLaiyuan.layoutManager = LinearLayoutManager(activity)
        viewBind.recyLaiyuan.adapter = departmAdapter4
        departmAdapter4?.setNewInstance(listBiaoZhunLaiYuan)
        departmAdapter3 = DepartDataAdapter2()
        viewBind.recyTichu.layoutManager = LinearLayoutManager(activity)
        viewBind.recyTichu.adapter = departmAdapter3
        departmAdapter3?.setNewInstance(listBumen)
        viewBind?.tvFabuChongzhi?.setOnClickListener {
            viewBind.downStartDate1.text = ""
            viewBind.downEndDate1.text = ""

        }
        departmAdapter4?.setOnItemClickListener { adapter, view, position ->


            pager = 0

            if (departpositon4 != position) {
                departpositon4 = position
                departmAdapter4?.pos = position
                biaozhunlaiyuanname = listBiaoZhunLaiYuan?.get(position)?.departmentName.toString()

            } else {
                departpositon4 = -1
                departmAdapter4?.pos = -1
                biaozhunlaiyuanname = ""

            }

            departmAdapter4?.notifyDataSetChanged()

            viewBind.mRefreshLayout.autoRefresh()

        }
        departmAdapter1?.setOnItemClickListener { adapter, view, position ->
            pager = 0
            if (departpositon1 != position) {
                departpositon1 = position
                departmAdapter1?.pos = position
                biaozhuntixiname = listTiXi?.get(position)?.departmentName

            } else {
                departpositon1 = -1
                departmAdapter1?.pos = -1
                biaozhuntixiname = ""

            }

            departmAdapter1?.notifyDataSetChanged()

            viewBind.mRefreshLayout.autoRefresh()

        }
        departmAdapter3?.setOnItemClickListener { adapter, view, position ->


            pager = 0
            if (departpositon3 != position) {
                departpositon3 = position
                departmAdapter3?.pos = position
                tichubumenname = listBumen?.get(position)?.departmentName

            } else {
                departpositon3 = -1
                departmAdapter3?.pos = -1
                tichubumenname = ""

            }

            departmAdapter3?.notifyDataSetChanged()

            viewBind.mRefreshLayout.autoRefresh()

        }
        departmAdapter2?.setOnItemClickListener { adapter, view, position ->
            pager = 0
            if (departpositon2 != position) {
                departpositon2 = position
                departmAdapter2?.pos = position
                pizhunmenname = listpizhunBumen?.get(position)?.departmentName

            } else {
                departpositon2 = -1
                departmAdapter2?.pos = -1
                pizhunmenname = ""

            }

            departmAdapter2?.notifyDataSetChanged()

            viewBind.mRefreshLayout.autoRefresh()

        }
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
        viewBind?.rvBiaozhuntixi?.setOnClickListener {
            if (rvBiaozhunTemp) {
                rvBiaozhunTemp = false
                viewBind?.recyBiaozhuntixi?.visibility = View.VISIBLE
            } else {
                rvBiaozhunTemp = true
                viewBind?.recyBiaozhuntixi?.visibility = View.GONE
            }
        }
        viewBind?.llPizhun?.setOnClickListener {
            if (pizhunTemp) {
                pizhunTemp = false

                viewBind?.recyPizhun?.visibility = View.VISIBLE
                viewBind?.tvPizhun?.text = "收起"

            } else {
                pizhunTemp = true
                viewBind?.recyPizhun?.visibility = View.GONE
                viewBind?.tvPizhun?.text = "展开"

            }
        }
        viewBind?.llTichu?.setOnClickListener {
            if (tichuTemp) {
                tichuTemp = false
                viewBind?.tvTichu?.text = "收起"
                viewBind?.recyTichu?.visibility = View.VISIBLE
            } else {
                tichuTemp = true
                viewBind?.tvTichu?.text = "展开"
                viewBind?.recyTichu?.visibility = View.GONE
            }
        }
        viewBind?.llLaiyuan?.setOnClickListener {
            if (laiyuanTemp) {
                laiyuanTemp = false
                viewBind?.tvLaiyuan?.text = "收起"
                viewBind?.recyLaiyuan?.visibility = View.VISIBLE
            } else {
                laiyuanTemp = true
                viewBind?.tvLaiyuan?.text = "展开"
                viewBind?.recyLaiyuan?.visibility = View.GONE
            }
        }
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

        //检索
        viewBind?.btSearch?.setOnClickListener {
            pager = 0
            keyName = viewBind?.etSearch?.text?.toString().toString()
            viewBind.mRefreshLayout.autoRefresh();
        }
        viewBind.checkbox1.setOnCheckedChangeListener { buttonView, isChecked ->
            processStatus = if (isChecked) {
                viewBind.checkbox2.isChecked = false
                "有效"
            } else {
                ""
            }
        }
        viewBind.checkbox2.setOnCheckedChangeListener { buttonView, isChecked ->
            processStatus = if (isChecked) {
                viewBind.checkbox1.isChecked = false
                "废止"
            } else {
                ""
            }
        }
        var chipId = R.id.bt_xiuding
        viewBind.chipGroup.setOnCheckedChangeListener { group, checkedId ->
            if (checkedId != -1) chipId = checkedId
            else {
                group.check(chipId)
                return@setOnCheckedChangeListener
            }
            when (checkedId) {// 1修订  2废止 3总后废止 4总装废止  5有效  6有效新版本   7有效老版本   8科工局废止  9限用
                R.id.bt_xiuding -> {
                    processStatus = "有效"
                }
                R.id.bt_feizhi -> {
                    processStatus = "废止"

                }
                R.id.bt_zongfeizhi -> {
                    processStatus = "3"

                }
                R.id.bt_zongzhuangfeizhi -> {
                    processStatus = "4"

                }
                R.id.bt_youxiao -> {
                    processStatus = "5"

                }
                R.id.bt_youxiao_xin -> {
                    processStatus = "6"

                }
                R.id.bt_youxiao_lao -> {
                    processStatus = "7"

                }
                R.id.bt_kegongju -> {
                    processStatus = "8"

                }
                R.id.bt_xianyong -> {
                    processStatus = "9"

                }
                else -> {
                    null
                }
            }

        }
        var list = ToolApplication.singletonTest(requireContext()).getListBiaoZhunDate3(true,
            viewBind?.downStartDate1?.text?.toString(),
            "",
            "",
            pizhunmenname,
            tichubumenname,
            viewBind?.tvZhubiandanwei?.text?.toString(),
            viewBind?.tvQicaoren?.text?.toString(),
            pager,
            keyName,
            processStatus, "", "", "ASC", "", "")

        //根据查询数据库返回进行分类
        list?.forEachIndexed { index2, standard ->

            if (listTiXi != null && listTiXi?.size!! > 0) {
                var mbean =
                    listTiXi?.indexOfFirst { it.departmentName == list.get(index2).CagegoryName }
                if (mbean != -1) {
                    var indexd = listTiXi?.get(mbean!!)?.departmentTwoName?.toInt()
                    var mindex = indexd!! + 1
                    listTiXi?.get(mbean!!)?.departmentTwoName = "" + mindex
                } else {
                    listTiXi?.add(SysDepartment().apply {
                        departmentName = list.get(index2).CagegoryName
                        departmentTwoName = "1"
                    })
                }

            } else {
                listTiXi?.add(SysDepartment().apply {
                    Log.e("ddddd", "uuuu")
                    departmentName = standard.CagegoryName
                    departmentTwoName = "1"
                })
            }

            if (listBumen != null && listBumen?.size!! > 0) {
                var m =
                    listBumen?.indexOfFirst { it.departmentName == list.get(index2).TiChuDepartmentName }
                if (m != -1) {
                    var index = listBumen?.get(m!!)?.departmentTwoName?.toInt()
                    var mindex = index!! + 1
                    listBumen?.get(m!!)?.departmentTwoName = "" + mindex
                } else {
                    listBumen?.add(SysDepartment().apply {
                        departmentName = standard.TiChuDepartmentName
                        departmentTwoName = "1"
                    })
                }

            } else {
                listBumen?.add(SysDepartment().apply {
                    departmentName = standard.TiChuDepartmentName
                    departmentTwoName = "1"
                })
            }
             if (listpizhunBumen != null && listpizhunBumen?.size!! > 0) {
                var mtemp2 =listpizhunBumen?.indexOfFirst { it.departmentName == list.get(index2).RatifyDepartmentName }
                if (mtemp2!=-1) {
                    var index = listpizhunBumen?.get(mtemp2!!)?.departmentTwoName?.toInt()
                    var mindex = index!! + 1
                    listpizhunBumen?.get(mtemp2!!)?.departmentTwoName = "" + mindex
                } else {
                    listpizhunBumen?.add(SysDepartment().apply {
                        departmentName = standard.RatifyDepartmentName
                        departmentTwoName = "1"
                    })
                }
             }else {
                listpizhunBumen?.add(SysDepartment().apply {
                    departmentName = standard.RatifyDepartmentName
                    departmentTwoName = "1"
                })
            }

            if (listBiaoZhunLaiYuan != null && listBiaoZhunLaiYuan?.size!! > 0) {
                var mtemp2 =listBiaoZhunLaiYuan?.indexOfFirst { it.departmentName == list.get(index2).RatifyDepartmentName }
                if (mtemp2!=-1) {
                    var index = listBiaoZhunLaiYuan?.get(mtemp2!!)?.departmentTwoName?.toInt()
                    var mindex = index!! + 1
                    listBiaoZhunLaiYuan?.get(mtemp2!!)?.departmentTwoName = "" + mindex
                } else {
                    listBiaoZhunLaiYuan?.add(SysDepartment().apply {
                        departmentName = standard.SourceName
                        departmentTwoName = "1"
                    })
                }

            }else {
                listBiaoZhunLaiYuan?.add(SysDepartment().apply {
                    departmentName = standard.SourceName
                    departmentTwoName = "1"
                })
            }
        }


    }

    private fun initDownView() {


    }

    private fun initStatusView() {

        titleList?.add("管理标准")
        var mulist = ArrayList<BaseTypeBean.Enum12>()
        mulist.add(BaseTypeBean.Enum12().apply {
            valueName = "装备全寿命周期管理标准"
            keyName = "装备全寿命周期管理标准"
        })

        enums12?.put("管理标准", mulist)
        titleList?.add("技术标准")
        var mulist2 = ArrayList<BaseTypeBean.Enum12>()
        mulist2.add(BaseTypeBean.Enum12().apply {
            valueName = "基础标准"
            keyName = "基础标准"
        })
        mulist2.add(BaseTypeBean.Enum12().apply {
            valueName = "共性技术标准"
            keyName = "共性技术标准"
        })
        mulist2.add(BaseTypeBean.Enum12().apply {
            valueName = "专用装备标准"
            keyName = "专用装备标准"
        })

        enums12?.put("技术标准", mulist2)
        titleList?.add("工作标准")
        var mulist3 = ArrayList<BaseTypeBean.Enum12>()
        mulist3.add(BaseTypeBean.Enum12().apply {
            valueName = "管理人员标准"
            keyName = "管理人员标准"
        })
        mulist3.add(BaseTypeBean.Enum12().apply {
            valueName = "操作人员标准"
            keyName = "操作人员标准"
        })
        mulist3.add(BaseTypeBean.Enum12().apply {
            valueName = "维护人员标准"
            keyName = "维护人员标准"
        })

        enums12?.put("工作标准", mulist3)

        if (viewBind.expandableListView != null) {
            viewBind.expandableListView?.setOnChildClickListener { parent, v, groupPosition, childPosition, id ->

                enums12?.get((titleList as ArrayList<String>)[groupPosition])
                    ?.forEachIndexed { index, enum12 ->
                        enum12.checked = index == childPosition
                    }
                enums12?.let { (adapter2 as CustomExpandableListAdapter?)?.setPoss(it) }

                false
            }
        }
        adapter2 = CustomExpandableListAdapter(requireContext(), titleList!!, enums12!!)

        viewBind.expandableListView?.setAdapter(adapter2)

        viewBind.expandableListView?.setOnGroupExpandListener { groupPosition ->
            for (index in 0..titleList?.size!! - 1) {
                if (index == groupPosition) {

                } else {
                    viewBind.expandableListView?.collapseGroup(index)
                }
            }
        }

        viewBind.expandableListView!!.setOnGroupCollapseListener { groupPosition ->
        }
    }

    private fun initApplyStatusView() {

    }


    var searchStr = " "
    var processStatus = " "
    override fun initEvent() {
        when (businessType) {
            ApplyModel.BUSINESS_TYPE_APPLY -> {
                getListUrl = Urls.sxsqList
                xdtbUrl = Urls.khglxdtb
//                viewBind.actionBarCustom.mleft.text =getString(R.string.LITERATURE)
            }
            ApplyModel.BUSINESS_TYPE_SUNSHINE_APPLY -> {

            }
        }
    }

    override fun initData() {

        viewBind?.etSearch?.setText(viewModel?.idCardNum)

        var temp =
            viewBind?.downStartDate1?.text?.toString() == "" && viewBind?.tvZhubiandanwei.text?.toString() == "" && viewBind?.tvQicaoren.text?.toString() == "" &&
                    pizhunmenname == "" && tichubumenname == "" && keyName == "" && processStatus == ""

        var list = getData()
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
        searchcaiyong = ""
        searchyinyong = ""
    }

    private fun getData(): List<Standard>? {
        var list = ToolApplication.singletonTest(requireContext()).getListBiaoZhunDate2(true,
            viewBind?.downStartDate1?.text?.toString(),
            "" + viewBind?.downEndDate1?.text?.toString(),
            "" + searchyinyong,
            "" + pizhunmenname,
            "" + tichubumenname,
            viewBind?.tvZhubiandanwei?.text?.toString(),
            viewBind?.tvQicaoren?.text?.toString(),
            pager,
            keyName,
            processStatus, "" + searchcaiyong, biaozhunlaiyuanname, biaozhuntixiname)
        var list2 = ToolApplication.singletonTest(requireContext()).getListCount(true,
            viewBind?.downStartDate1?.text?.toString(),
            "" + viewBind?.downEndDate1?.text?.toString(),
            "" + searchyinyong,
            "" + pizhunmenname,
            "" + tichubumenname,
            viewBind?.tvZhubiandanwei?.text?.toString(),
            viewBind?.tvQicaoren?.text?.toString(),
            pager,
            keyName,
            processStatus, "" + searchcaiyong, biaozhunlaiyuanname, biaozhuntixiname)

        viewBind.tvResult.text = "找到" + list2 + "条相关搜索结果"


        return list
    }

    override fun refreshData(type: Int?) {
        viewBind.mRefreshLayout.autoRefresh()
    }

    override fun onRefresh(refreshLayout: RefreshLayout) {
        refreshState = Constants.RefreshState.STATE_REFRESH
        currentPage = 1
        initData()
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
//            viewBind.llPizhun -> {
//                if (bean.type == 1) {
//                    bean.type = 0
//                    viewBind.dataType = bean
//                    viewBind?.recyPizhun?.visibility = View.VISIBLE
//
//                } else {
//                    bean.type = 1
//                    viewBind.dataType = bean
//                    viewBind?.recyPizhun?.visibility = View.GONE
//
//                }
//            }
        }

    }

    /**
    返回后刷新数据，
     */
    @Subscribe(thread = EventThread.MAIN_THREAD, tags = [Tag(Constants.BusAction.Bus_Refresh_List)])
    fun backRefresh(str: String) {
        pager = 0
        viewBind.mRefreshLayout.autoRefresh()
    }

    /**
    根据选中引用标准更新
     */
    @Subscribe(thread = EventThread.MAIN_THREAD, tags = [Tag("searchyinyong")])
    fun backRefresh2(str: String) {
        pager = 0
        searchyinyong = str
        searchcaiyong = ""
        viewBind.mRefreshLayout.autoRefresh()
    }

    /**
    根据选中采用标准更新
     */
    @Subscribe(thread = EventThread.MAIN_THREAD, tags = [Tag("searchcaiyong")])
    fun backRefresh3(str: String) {
        pager = 0
        searchcaiyong = str
        searchyinyong = ""
        viewBind.mRefreshLayout.autoRefresh()
    }
}