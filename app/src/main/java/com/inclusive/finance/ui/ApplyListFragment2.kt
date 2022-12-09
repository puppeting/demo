package com.inclusive.finance.ui

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.TextView
import androidx.appcompat.widget.AppCompatCheckedTextView
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
import com.blankj.utilcode.util.ToastUtils
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
import com.inclusive.finance.databinding.FragmentApplyList2Binding
import com.inclusive.finance.pop.*
import com.inclusive.finance.room.Standard
import com.inclusive.finance.room.SysDepartment
import com.inclusive.finance.utils.StatusBarUtil
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener
import org.jetbrains.anko.support.v4.act
import java.text.SimpleDateFormat
import java.util.*


/**
 *
 * */
class ApplyListFragment2 : MyBaseFragment(), View.OnClickListener, OnRefreshLoadMoreListener {
    private   var searchyinyong: String = ""
    private   var searchcaiyong: String = ""

    private   var tiaojian_2: String = ""
    private   var tiaojian_1: String = ""
    private   var tiaojian_3: String = ""
    private   var tiaojian_4: String = ""
    private   var tiaojian_5: String = ""
    private   var tiaojian_6: String = ""
    private   var tiaojian_7: String = ""
    private   var tiaojian_8: String = ""
    private   var tiaojian_9: String = ""

    private   var tichubumenname_content: String = ""
    private   var pizhunmenname_content: String = ""
    private   var feileiHao_content: String = ""
    private var biaozhunhao_content: String=""
    private   var biaozhun_content: String = ""
    private var remark: String?=""
    private var RatifyDepartmentName: String?=""
    private var EditorDepartmentName: String?=""
    private var qicaoren: String?=""
    private var yinyongbiaozhun: String?=""
    private var caiyongbiaozhun: String?=""

    private   var STATE_downYu2: String = ""
    private   var STATE_downYu3: String = ""
    private   var STATE_downYu4: String = ""
    private   var STATE_downYu5: String = ""
    private   var STATE_downYu6: String = ""
    private   var STATE_downYu7: String = ""
    private   var STATE_downYu8: String = ""
    private   var STATE_downYu9: String = ""


    private var  biaozhunhao: String?=""
    private var tempRemove1: Boolean = true
    private var tempRemove2: Boolean = true
    private var tempRemove3: Boolean = true
    private var tempRemove4: Boolean = true
    private var tempRemove5: Boolean = true
    private var tempRemove6: Boolean = true
    private var tempRemove7: Boolean = true
    private var tempRemove8: Boolean = true
    private var tempRemove9: Boolean = true
    private var tempRemove10: Boolean = true

    private var dataListYUhuo: ArrayList<BaseTypeBean.Enum12>? = arrayListOf()
    private var dataList: ArrayList<BaseTypeBean.Enum12>? = arrayListOf()
    private var tichubumenname: String? = ""
    private var pizhunmenname: String? = ""

    private var listBumen: MutableList<SysDepartment>? = arrayListOf()
    private var departmAdapter2: DepartDataAdapter2<SysDepartment>? = null
    private var departmAdapter3: DepartDataAdapter2<SysDepartment>? = null
    var andsql=" 1=1 "
    var orsql=""

    private var accountPop: AccountPop2? = null
    private var totalCount: Int = 0
    private var bean: ClickBean = ClickBean()
    private var keyName: String = "Arc"
    private var endTime: String? = ""
    private var biaozhun: String = ""
    var pager = 0
    private var adapter2: CustomExpandableListAdapter? = null
    private var mAdapter: DataAdapter<Standard>? = null
    var titleList: ArrayList<String>? = arrayListOf()
    lateinit var viewModel: ApplyModel
    lateinit var viewBind: FragmentApplyList2Binding
    private var refreshState = Constants.RefreshState.STATE_REFRESH
    private var currentPage = 1
    var event: Lifecycle.Event? = null
    var getListUrl = ""
    var xdtbUrl = ""
    var SearchBean: ArrayList<SearchBean>? = arrayListOf()
    var rvBiaozhunTemp = true
    var pizhunTemp = true
    var tichuTemp = true

    private var businessType: Int = ApplyModel.BUSINESS_TYPE_APPLY
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        viewModel = ViewModelProvider(act).get(ApplyModel::class.java)
        viewBind = FragmentApplyList2Binding.inflate(inflater, container, false).apply {
            data = viewModel
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
        Log.e("返回的数据","*****"+viewModel?.keyId)
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

                            R.id.item_account -> {
                                if (accountPop == null) {
                                    accountPop = AccountPop2(act as ApplyActivity)
                                 }
                                accountPop?.showPopupWindow()
                            }
                            R.id.item_askForLeave -> {
                                RxBus.get().post("dismiss","dismiss")
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

        initDownView()
        initStatusView()
        initApplyStatusView()
        departmAdapter2 = DepartDataAdapter2()

        departmAdapter2?.setNewInstance(listBumen)
        departmAdapter3 = DepartDataAdapter2()

        departmAdapter3?.setNewInstance(listBumen)
        departmAdapter3?.setOnItemClickListener { adapter, view, position ->
            tichubumenname = listBumen?.get(position)?.departmentName
            viewBind.mRefreshLayout.autoRefresh()
        }
        departmAdapter2?.setOnItemClickListener { adapter, view, position ->
            pizhunmenname = listBumen?.get(position)?.departmentName
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
        if(SPUtils.getInstance().getString(Constants.SPUtilsConfig.SP_LOGINNAME)!="admin"){
            viewBind?.actionBarCustom?.btSystem?.visibility=View.GONE
            viewBind?.actionBarCustom?.btFiel?.visibility=View.GONE

        }
        mAdapter?.setOnItemClickListener { adapter, view, position ->
            IRouter.goF(view, R.id.action_to_navActivity, "文件详情", mAdapter?.data?.get(position)?.id)
        }

        viewBind.downShishiDate2.setOnClickListener {

            val pvTime: TimePickerView = TimePickerBuilder(context) { date, _ ->
                endTime = TimeUtils.date2String(date, SimpleDateFormat("yyyy-MM-dd", Locale.CHINA))
                viewBind.downShishiDate2.text = endTime
            }.isDialog(true).build()
            pvTime.dialog.window?.setGravity(Gravity.BOTTOM)
            pvTime.dialog.window?.setWindowAnimations(R.style.picker_view_slide_anim)
            pvTime.setOnDismissListener { viewBind.downShishiDate2.isChecked = false }
            viewBind.downShishiDate2.isChecked = true
            pvTime.show()

        }

        viewBind.downShishiendDate2.setOnClickListener {
            val pvTime: TimePickerView = TimePickerBuilder(context) { date, _ ->
                endTime = TimeUtils.date2String(date, SimpleDateFormat("yyyy-MM-dd", Locale.CHINA))
                viewBind.downShishiendDate2.text = endTime
            }.isDialog(true).build()
            pvTime.dialog.window?.setGravity(Gravity.BOTTOM)
            pvTime.dialog.window?.setWindowAnimations(R.style.picker_view_slide_anim)
            pvTime.setOnDismissListener { viewBind.downShishiendDate2.isChecked = false }
            viewBind.downShishiendDate2.isChecked = true
            pvTime.show()

        }
        viewBind.downStartDate2.setOnClickListener {
            val pvTime: TimePickerView = TimePickerBuilder(context) { date, _ ->
                endTime = TimeUtils.date2String(date, SimpleDateFormat("yyyy-MM-dd", Locale.CHINA))
                viewBind.downStartDate2.text = endTime
            }.isDialog(true).build()
            pvTime.dialog.window?.setGravity(Gravity.BOTTOM)
            pvTime.dialog.window?.setWindowAnimations(R.style.picker_view_slide_anim)
            pvTime.setOnDismissListener { viewBind.downStartDate2.isChecked = false }
            viewBind.downStartDate2.isChecked = true
            pvTime.show()

        }
        viewBind.downEndDate2.setOnClickListener {
            val pvTime: TimePickerView = TimePickerBuilder(context) { date, _ ->
                endTime = TimeUtils.date2String(date, SimpleDateFormat("yyyy-MM-dd", Locale.CHINA))
                viewBind.downEndDate2.text = endTime
            }.isDialog(true).build()
            pvTime.dialog.window?.setGravity(Gravity.BOTTOM)
            pvTime.dialog.window?.setWindowAnimations(R.style.picker_view_slide_anim)
            pvTime.setOnDismissListener { viewBind.downEndDate2.isChecked = false }
            viewBind.downEndDate2.isChecked = true
            pvTime.show()

        }
        viewBind?.tvShishiChongzhi?.setOnClickListener {
            viewBind.downShishiendDate2.text=""
            viewBind.downShishiDate2.text=""
        }
        viewBind?.tvFabuChongzhi?.setOnClickListener {
            viewBind.downStartDate2.text=""
            viewBind.downEndDate2.text=""

        }
         //检索
        viewBind?.btSearch?.setOnClickListener {
            pager = 0
            andsql = " 1=1 "
            orsql = ""
            if (viewBind.downStartDate2.text.toString()!=""&&viewBind.downEndDate2.text.toString()!=""){
                var status_sql =
                    "and  ReleaseTime BETWEEN '" + viewBind.downStartDate2.text.toString() + "' AND '"+viewBind.downEndDate2.text.toString()+"'"
                andsql += status_sql
            }
            if (viewBind.downShishiDate2.text.toString()!=""&&viewBind.downShishiendDate2.text.toString()!=""){
                var status_sql =
                    "and   StartTime BETWEEN '" + viewBind.downShishiDate2.text.toString() + "' AND '"+viewBind.downShishiendDate2.text.toString()+"'"
                andsql += status_sql
            }
            if (viewBind.searchContent.etCustname.text.toString() != "") {
                if(tiaojian_1=="Name"){
                    var status_sql =
                        "and " + tiaojian_1 + " like '%" + viewBind.searchContent.etCustname.text.toString() + "%'"
                    andsql += status_sql
                }else {
                    var status_sql =
                        "and " + tiaojian_1 + " = '" + viewBind.searchContent.etCustname.text.toString() + "'"
                    andsql += status_sql
                }
            }
            if (viewBind.searchContent.etCustname2.text.toString() != "") {
                if (STATE_downYu2 == "与") {
                    if(tiaojian_2=="Name"){
                        var status_sql =
                            "and " + tiaojian_2 + " like '%" + viewBind.searchContent.etCustname2.text.toString() + "%'"
                        andsql += status_sql
                    } else {
                        var status_sql =
                            "and " + tiaojian_2 + " = '" + viewBind.searchContent.etCustname2.text.toString() + "'"
                        andsql += status_sql
                    }
                } else if (STATE_downYu2 == "或") {
                    if(tiaojian_2=="Name") {
                        var status_sql =
                            "or " + tiaojian_2 + " like '%" + viewBind.searchContent.etCustname2.text.toString() + "%'"
                        orsql += status_sql
                    }else {
                        var status_sql =
                            "or " + tiaojian_2 + " = '" + viewBind.searchContent.etCustname2.text.toString() + "'"
                        orsql += status_sql
                    }
                }
            }
            if (viewBind.searchContent.etCustname3.text.toString() != "") {
                if (STATE_downYu3 == "与") {
                    if(tiaojian_3=="Name") {
                        var status_sql =
                            "and " + tiaojian_3 + " like '%" + viewBind.searchContent.etCustname3.text.toString() + "%'"
                        andsql = andsql + status_sql
                    }else {
                        var status_sql =
                            "and " + tiaojian_3 + " = '" + viewBind.searchContent.etCustname3.text.toString() + "'"
                        andsql = andsql + status_sql
                    }
                } else if (STATE_downYu3 == "或") {
                    if(tiaojian_3=="Name") {
                        var status_sql =
                            "or " + tiaojian_3 + " like '%" + viewBind.searchContent.etCustname3.text.toString() + "%'"
                        orsql = orsql + status_sql
                    }else {
                        var status_sql =
                            "or " + tiaojian_3 + " = '" + viewBind.searchContent.etCustname3.text.toString() + "'"
                        orsql = orsql + status_sql
                    }
                }
            }
            //select * from SysStandard where  1 = 1  and  (  1=1 and Name = '文献 '  )  Limit 10 Offset 0
            if (viewBind.searchContent.etCustname4.text.toString() != "") {
                if (STATE_downYu4 == "与") {
                    if(tiaojian_4=="Name") {
                        var status_sql =
                            "and " + tiaojian_4 + " like '%" + viewBind.searchContent.etCustname4.text.toString() + "%'"
                        andsql = andsql + status_sql
                    }else {
                        var status_sql =
                            "and " + tiaojian_4 + " = '" + viewBind.searchContent.etCustname4.text.toString() + "'"
                        andsql = andsql + status_sql
                    }
                } else if (STATE_downYu4 == "或") {
                    if(tiaojian_4=="Name") {
                        var status_sql =
                            "or " + tiaojian_4 + " like '%" + viewBind.searchContent.etCustname4.text.toString() + "%'"
                        orsql = orsql + status_sql
                    }else{
                        var status_sql =
                            "or " + tiaojian_4 + " = '" + viewBind.searchContent.etCustname4.text.toString() + "'"
                        orsql = orsql + status_sql
                    }
                }
            }
            if (viewBind.searchContent.etCustname5.text.toString() != "") {
                if (STATE_downYu5 == "与") {
                    if(tiaojian_5=="Name") {
                        var status_sql =
                            "and " + tiaojian_5 + " like '%" + viewBind.searchContent.etCustname5.text.toString() + "%'"
                        andsql = andsql + status_sql
                    }else {
                        var status_sql =
                            "and " + tiaojian_5 + " = '" + viewBind.searchContent.etCustname5.text.toString() + "'"
                        andsql = andsql + status_sql
                    }
                } else if (STATE_downYu5 == "或") {
                    if(tiaojian_5=="Name") {
                        var status_sql =
                            "or " + tiaojian_5 + " like '%" + viewBind.searchContent.etCustname5.text.toString() + "%'"
                        orsql = orsql + status_sql
                    }else {
                        var status_sql =
                            "or " + tiaojian_5 + " = '" + viewBind.searchContent.etCustname5.text.toString() + "'"
                        orsql = orsql + status_sql
                    }
                }
            }
            if (viewBind.searchContent.etCustname6.text.toString() != "") {
                if (STATE_downYu6 == "与") {
                    if(tiaojian_6=="Name") {
                        var status_sql =
                            "and " + tiaojian_6 + " like '%" + viewBind.searchContent.etCustname6.text.toString() + "%'"
                        andsql = andsql + status_sql
                    }else{
                        var status_sql =
                            "and " + tiaojian_6 + " = '" + viewBind.searchContent.etCustname6.text.toString() + "'"
                        andsql = andsql + status_sql
                    }
                } else if (STATE_downYu6 == "或") {
                    if(tiaojian_6=="Name") {
                        var status_sql =
                            "or " + tiaojian_6 + " like '%" + viewBind.searchContent.etCustname6.text.toString() + "%'"
                        andsql = andsql + status_sql
                    }else {
                        var status_sql =
                            "or " + tiaojian_6 + " = '" + viewBind.searchContent.etCustname6.text.toString() + "'"
                        orsql = orsql + status_sql
                    }
                }
            }

            if (viewBind.searchContent.etCustname7.text.toString() != "") {
                if (STATE_downYu7 == "与") {
                    if(tiaojian_7=="Name") {
                        var status_sql =
                            "and " + tiaojian_7 + " like '%" + viewBind.searchContent.etCustname7.text.toString() + "%'"
                        andsql = andsql + status_sql
                    }else {
                        var status_sql =
                            "and " + tiaojian_7 + " = '" + viewBind.searchContent.etCustname7.text.toString() + "'"
                        andsql = andsql + status_sql
                    }
                } else if (STATE_downYu7 == "或") {
                    if(tiaojian_7=="Name") {
                        var status_sql =
                            "or " + tiaojian_7 + " like '%" + viewBind.searchContent.etCustname7.text.toString() + "%'"
                        andsql = andsql + status_sql
                    }else {
                        var status_sql =
                            "or " + tiaojian_7 + " = '" + viewBind.searchContent.etCustname7.text.toString() + "'"
                        orsql = orsql + status_sql
                    }
                }
            }
            if (viewBind.searchContent.etCustname8.text.toString() != "") {
                if (STATE_downYu8 == "与") {
                    if(tiaojian_8=="Name") {
                        var status_sql =
                            "and " + tiaojian_8 + " like '%" + viewBind.searchContent.etCustname8.text.toString() + "%'"
                        andsql = andsql + status_sql
                    }else {
                        var status_sql =
                            "and " + tiaojian_8 + " = '" + viewBind.searchContent.etCustname8.text.toString() + "'"
                        andsql = andsql + status_sql
                    }
                } else if (STATE_downYu8 == "或") {
                    if(tiaojian_8=="Name") {
                        var status_sql =
                            "or " + tiaojian_8 + " like '%" + viewBind.searchContent.etCustname8.text.toString() + "%'"
                        andsql = andsql + status_sql
                    }else {
                        var status_sql =
                            "or " + tiaojian_8 + " = '" + viewBind.searchContent.etCustname8.text.toString() + "'"
                        orsql = orsql + status_sql
                    }
                }
            }
            if (viewBind.searchContent.etCustname9.text.toString() != "") {
                if (STATE_downYu9 == "与") {
                    if(tiaojian_9=="Name") {
                        var status_sql =
                            "and " + tiaojian_9 + " like '%" + viewBind.searchContent.etCustname9.text.toString() + "%'"
                        andsql = andsql + status_sql
                    }else {
                        var status_sql =
                            "and " + tiaojian_9 + " = '" + viewBind.searchContent.etCustname9.text.toString() + "'"
                        andsql += status_sql
                    }

                } else if (STATE_downYu9 == "或") {
                    if(tiaojian_9=="Name") {
                        var status_sql =
                            "or " + tiaojian_9 + " like '%" + viewBind.searchContent.etCustname9.text.toString() + "%'"
                        andsql = andsql + status_sql
                    }else {
                        var status_sql =
                            "or " + tiaojian_9 + " = '" + viewBind.searchContent.etCustname9.text.toString() + "'"
                        andsql += status_sql
                    }

                }
            }
            if(!processStatus.trim().equals("")){
                var status_sql =
                    "and Status = '" + processStatus + "'"
                andsql += status_sql
            }
            viewBind.mRefreshLayout.autoRefresh()
        }
        viewBind.searchHead.ivAdd?.setOnClickListener(this)
        viewBind.searchContent.ivRemove1?.setOnClickListener(this)
        viewBind.searchContent.ivRemove2?.setOnClickListener(this)
        viewBind.searchContent.ivRemove3?.setOnClickListener(this)
        viewBind.searchContent.ivRemove4?.setOnClickListener(this)
        viewBind.searchContent.ivRemove5?.setOnClickListener(this)
        viewBind.searchContent.ivRemove6?.setOnClickListener(this)
        viewBind.searchContent.ivRemove7?.setOnClickListener(this)
        viewBind.searchContent.ivRemove8?.setOnClickListener(this)
        viewBind.searchContent.ivRemove9?.setOnClickListener(this)
        viewBind.searchContent.downJczt?.setOnClickListener(this)
        viewBind.searchContent.downJczt2?.setOnClickListener(this)
        viewBind.searchContent.downJczt3?.setOnClickListener(this)
        viewBind.searchContent.downJczt4?.setOnClickListener(this)
        viewBind.searchContent.downJczt5?.setOnClickListener(this)
        viewBind.searchContent.downJczt6?.setOnClickListener(this)
        viewBind.searchContent.downJczt7?.setOnClickListener(this)
        viewBind.searchContent.downJczt8?.setOnClickListener(this)
        viewBind.searchContent.downJczt9?.setOnClickListener(this)
        viewBind.searchContent.downYu2?.setOnClickListener(this)
        viewBind.searchContent.downYu3?.setOnClickListener(this)
        viewBind.searchContent.downYu4?.setOnClickListener(this)
        viewBind.searchContent.downYu5?.setOnClickListener(this)
        viewBind.searchContent.downYu6?.setOnClickListener(this)
        viewBind.searchContent.downYu7?.setOnClickListener(this)
        viewBind.searchContent.downYu8?.setOnClickListener(this)
        viewBind.searchContent.downYu9?.setOnClickListener(this)

        viewBind.mRefreshLayout.finishRefresh()
        viewBind.checkbox1.setOnCheckedChangeListener { buttonView, isChecked ->
            processStatus = if (isChecked){
                viewBind.checkbox2.isChecked=false
                "有效"
            }else{
                ""
            }
        }
        viewBind.checkbox2.setOnCheckedChangeListener { buttonView, isChecked ->
            processStatus = if (isChecked){
                viewBind.checkbox1.isChecked=false
                "废止"
            }else{
                ""
            }
        }
    }

    private fun getFielList() {
        var mcount = ToolApplication.singletonTest(requireContext())
            .getListBiaoZhunGaoJiCount(andsql, orsql,
                pager)
        viewBind.tvResult.text = "找到" + mcount+ "条相关搜索结果"

        var list = ToolApplication.singletonTest(requireContext())
            .getListBiaoZhunGaoJi(andsql, orsql,
                pager)

        if (list == null) {
            Log.e("dddd", "没有查到数据")
        } else {
            Log.e("dddd", "查到数据***" + list?.size!!)
        }
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
            viewBind.mRefreshLayout.finishRefresh()
            if(pager==0) {
                mAdapter?.data?.clear()
                mAdapter?.notifyDataSetChanged()
            }
            viewBind.mRefreshLayout.finishLoadMoreWithNoMoreData()
        }
        searchcaiyong=""
        searchyinyong=""
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


    }

    private fun initApplyStatusView() {
        dataListYUhuo = ArrayList<BaseTypeBean.Enum12>()
        dataListYUhuo?.clear()
        dataListYUhuo?.add(BaseTypeBean.Enum12().apply {
            valueName = "与"
            keyName = "与"
        })
        dataListYUhuo?.add(BaseTypeBean.Enum12().apply {
            valueName = "或"
            keyName = "或"
        })
        dataList = ArrayList<BaseTypeBean.Enum12>()
        dataList?.clear()
        dataList?.add(BaseTypeBean.Enum12().apply {
            valueName = "标准名称"
            keyName = "标准名称"
        })
        dataList?.add(BaseTypeBean.Enum12().apply {
            valueName = "标准号"
            keyName = "标准号"
        })
        dataList?.add(BaseTypeBean.Enum12().apply {
            valueName = "分类号"
            keyName = "分类号"
        })
        dataList?.add(BaseTypeBean.Enum12().apply {
            valueName = "批准单位"
            keyName = "批准单位"
        })
        dataList?.add(BaseTypeBean.Enum12().apply {
            valueName = "提出单位"
            keyName = "提出单位"
        })
        dataList?.add(BaseTypeBean.Enum12().apply {
            valueName = "主编单位"
            keyName = "主编单位"
        })
        dataList?.add(BaseTypeBean.Enum12().apply {
            valueName = "起草人"
            keyName = "起草人"
        })
        dataList?.add(BaseTypeBean.Enum12().apply {
            valueName = "适用范围"
            keyName = "适用范围"
        })
        dataList?.add(BaseTypeBean.Enum12().apply {
            valueName = "引用标准"
            keyName = "引用标准"
        })
        dataList?.add(BaseTypeBean.Enum12().apply {
            valueName = "采用标准"
            keyName = "采用标准"
        })

    }


    var searchStr = " "
    var processStatus = " "
    var processStatus2 = " "

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


        listBumen =
            ToolApplication.singletonTest(requireContext()).getListBuMenDate("")

        departmAdapter2?.setNewInstance(listBumen)
        departmAdapter3?.setNewInstance(listBumen)
         getFielList()
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

    var temp = 1
    override fun onClick(v: View?) {


//        if (v == viewBind.btSearch) {
//            if (System.currentTimeMillis() - mLastClickTime > TIME_INTERVAL) {
//                refreshData()
//                mLastClickTime = System.currentTimeMillis()
//            }
//            return
//        }
        when (v) {
            viewBind.searchContent.downYu2 -> {
                DownPop(context,
                    enums12 = dataListYUhuo,
                    checkedTextView = viewBind.searchContent.downYu2 as AppCompatCheckedTextView,
                    isSingleChecked = true) { k, v, p ->
                    STATE_downYu2=v
                }.showPopupWindow(viewBind.searchContent.downYu2)
            }
            viewBind.searchContent.downYu3 -> {
                DownPop(context,
                    enums12 = dataListYUhuo,
                    checkedTextView = viewBind.searchContent.downYu3 as AppCompatCheckedTextView,
                    isSingleChecked = true) { k, v, p ->
                    STATE_downYu3=v

                }.showPopupWindow(viewBind.searchContent.downYu3)
            }
            viewBind.searchContent.downYu4-> {
                DownPop(context,
                    enums12 = dataListYUhuo,
                    checkedTextView = viewBind.searchContent.downYu4 as AppCompatCheckedTextView,
                    isSingleChecked = true) { k, v, p ->
                    STATE_downYu4=v

                }.showPopupWindow(viewBind.searchContent.downYu4)
            }
            viewBind.searchContent.downYu5-> {
                DownPop(context,
                    enums12 = dataListYUhuo,
                    checkedTextView = viewBind.searchContent.downYu5 as AppCompatCheckedTextView,
                    isSingleChecked = true) { k, v, p ->
                    STATE_downYu5=v

                }.showPopupWindow(viewBind.searchContent.downYu5)
            }
            viewBind.searchContent.downYu6-> {
                DownPop(context,
                    enums12 = dataListYUhuo,
                    checkedTextView = viewBind.searchContent.downYu6 as AppCompatCheckedTextView,
                    isSingleChecked = true) { k, v, p ->
                    STATE_downYu6=v

                }.showPopupWindow(viewBind.searchContent.downYu6)
            }
            viewBind.searchContent.downYu7-> {
                DownPop(context,
                    enums12 = dataListYUhuo,
                    checkedTextView = viewBind.searchContent.downYu7 as AppCompatCheckedTextView,
                    isSingleChecked = true) { k, v, p ->
                    STATE_downYu7=v

                }.showPopupWindow(viewBind.searchContent.downYu7)
            }
            viewBind.searchContent.downYu8-> {
                DownPop(context,
                    enums12 = dataListYUhuo,
                    checkedTextView = viewBind.searchContent.downYu8 as AppCompatCheckedTextView,
                    isSingleChecked = true) { k, v, p ->
                    STATE_downYu8=v

                }.showPopupWindow(viewBind.searchContent.downYu8)
            }
            viewBind.searchContent.downYu9-> {
                DownPop(context,
                    enums12 = dataListYUhuo,
                    checkedTextView = viewBind.searchContent.downYu9 as AppCompatCheckedTextView,
                    isSingleChecked = true) { k, v, p ->
                    STATE_downYu9=v

                }.showPopupWindow(viewBind.searchContent.downYu9)
            }

            viewBind.searchContent.downJczt -> {


                dataList?.forEachIndexed { index, enum12 ->

                    if (index==dataList?.size!!-1){

                        DownPop(context,
                            enums12 = dataList,
                            checkedTextView = viewBind.searchContent.downJczt as AppCompatCheckedTextView,
                            isSingleChecked = true) { k, v, p ->
                            if(v=="标准名称"){
                                tiaojian_1="Name";
                            }else if(v=="标准号"){
                                tiaojian_1="BiaoNo";
                            }else if(v=="分类号"){
                                tiaojian_1="TypeNum";
                            }else if(v=="批准单位"){
                                tiaojian_1="RatifyDepartmentName";
                            }else if(v=="提出单位"){
                                tiaojian_1="TiChuDepartmentName";
                            }else if(v=="主编单位"){
                                tiaojian_1="EditorDepartmentName";
                            }else if(v=="起草人"){
                                tiaojian_1="Drafter";
                            }else if(v=="适用范围"){
                                tiaojian_1="Remark";
                            }else if(v=="引用标准"){
                                tiaojian_1="Referencesd";
                            }else if(v=="采用标准"){
                                tiaojian_1="Standards";
                            }

                        }.showPopupWindow(viewBind.searchContent.downJczt)
                    }
                }
            }
            viewBind.searchContent.downJczt2 -> {

                var removeList=ArrayList<BaseTypeBean.Enum12>()
                dataList?.forEachIndexed { index, enum12 ->

                    if (index==dataList?.size!!-1){

                        DownPop(context,
                            enums12 = dataList,
                            checkedTextView = viewBind.searchContent.downJczt2 as AppCompatCheckedTextView,
                            isSingleChecked = true) { k, v, p ->
//                            tiaojian_2=v
                            if(v=="标准名称"){
                                tiaojian_2="Name";
                            }else if(v=="标准号"){
                                tiaojian_2="BiaoNo";
                            }else if(v=="分类号"){
                                tiaojian_2="TypeNum";
                            }else if(v=="批准单位"){
                                tiaojian_2="RatifyDepartmentName";
                            }else if(v=="提出单位"){
                                tiaojian_2="TiChuDepartmentName";
                            }else if(v=="主编单位"){
                                tiaojian_2="EditorDepartmentName";
                            }else if(v=="起草人"){
                                tiaojian_2="Drafter";
                            }else if(v=="适用范围"){
                                tiaojian_2="Remark";
                            }else if(v=="引用标准"){
                                tiaojian_2="Referencesd";
                            }else if(v=="采用标准"){
                                tiaojian_2="Standards";
                            }
                        }.showPopupWindow(viewBind.searchContent.downJczt2)
                    }
                }

            }
            viewBind.searchContent.downJczt3 -> {

                dataList?.forEachIndexed { index, enum12 ->

                    if (index==dataList?.size!!-1){

                        DownPop(context,
                            enums12 = dataList,
                            checkedTextView = viewBind.searchContent.downJczt3 as AppCompatCheckedTextView,
                            isSingleChecked = true) { k, v, p ->
//                            tiaojian_3=v
                            if(v=="标准名称"){
                                tiaojian_3="Name";
                            }else if(v=="标准号"){
                                tiaojian_3="BiaoNo";
                            }else if(v=="分类号"){
                                tiaojian_3="TypeNum";
                            }else if(v=="批准单位"){
                                tiaojian_3="RatifyDepartmentName";
                            }else if(v=="提出单位"){
                                tiaojian_3="TiChuDepartmentName";
                            }else if(v=="主编单位"){
                                tiaojian_3="EditorDepartmentName";
                            }else if(v=="起草人"){
                                tiaojian_3="Drafter";
                            }else if(v=="适用范围"){
                                tiaojian_3="Remark";
                            }else if(v=="引用标准"){
                                tiaojian_3="Referencesd";
                            }else if(v=="采用标准"){
                                tiaojian_3="Standards";
                            }
                        }.showPopupWindow(viewBind.searchContent.downJczt3)
                    }
                }
            }
            viewBind.searchContent.downJczt4 -> {
                var mdata=ArrayList<BaseTypeBean.Enum12>()

                dataList?.forEachIndexed { index, enum12 ->
                    if (enum12.valueName==tiaojian_1){

                    }else if (enum12.valueName==tiaojian_2){

                    }else if (enum12.valueName==tiaojian_3){
                    }else if (enum12.valueName==tiaojian_4){
                    }else if (enum12.valueName==tiaojian_5){
                    }else if (enum12.valueName==tiaojian_6){
                    }else if (enum12.valueName==tiaojian_7){
                    }else if (enum12.valueName==tiaojian_8){
                    }else if (enum12.valueName==tiaojian_9){
                    }else {
                        mdata?.add(enum12)
                    }
                    if (index==dataList?.size!!-1){
                        DownPop(context,
                            enums12 = dataList,
                            checkedTextView = viewBind.searchContent.downJczt4 as AppCompatCheckedTextView,
                            isSingleChecked = true) { k, v, p ->
                            tiaojian_4=v
                            if(v=="标准名称"){
                                tiaojian_4="Name";
                            }else if(v=="标准号"){
                                tiaojian_4="BiaoNo";
                            }else if(v=="分类号"){
                                tiaojian_4="TypeNum";
                            }else if(v=="批准单位"){
                                tiaojian_4="RatifyDepartmentName";
                            }else if(v=="提出单位"){
                                tiaojian_4="TiChuDepartmentName";
                            }else if(v=="主编单位"){
                                tiaojian_4="EditorDepartmentName";
                            }else if(v=="起草人"){
                                tiaojian_4="Drafter";
                            }else if(v=="适用范围"){
                                tiaojian_4="Remark";
                            }else if(v=="引用标准"){
                                tiaojian_4="Referencesd";
                            }else if(v=="采用标准"){
                                tiaojian_4="Standards";
                            }
                        }.showPopupWindow(viewBind.searchContent.downJczt4)
                    }
                }
            }
            viewBind.searchContent.downJczt5 -> {

                DownPop(context,
                    enums12 = dataList,
                    checkedTextView = viewBind.searchContent.downJczt5 as AppCompatCheckedTextView,
                    isSingleChecked = true) { k, v, p ->
                    tiaojian_5=v
                    if(v=="标准名称"){
                        tiaojian_5="Name";
                    }else if(v=="标准号"){
                        tiaojian_5="BiaoNo";
                    }else if(v=="分类号"){
                        tiaojian_5="TypeNum";
                    }else if(v=="批准单位"){
                        tiaojian_5="RatifyDepartmentName";
                    }else if(v=="提出单位"){
                        tiaojian_5="TiChuDepartmentName";
                    }else if(v=="主编单位"){
                        tiaojian_5="EditorDepartmentName";
                    }else if(v=="起草人"){
                        tiaojian_5="Drafter";
                    }else if(v=="适用范围"){
                        tiaojian_5="Remark";
                    }else if(v=="引用标准"){
                        tiaojian_5="Referencesd";
                    }else if(v=="采用标准"){
                        tiaojian_5="Standards";
                    }
                }.showPopupWindow(viewBind.searchContent.downJczt5)
            }
            viewBind.searchContent.downJczt6 -> {

                dataList?.forEachIndexed { index, enum12 ->

                    if (index==dataList?.size!!-1){

                        DownPop(context,
                            enums12 = dataList,
                            checkedTextView = viewBind.searchContent.downJczt6 as AppCompatCheckedTextView,
                            isSingleChecked = true) { k, v, p ->
                            tiaojian_6=v
                            if(v=="标准名称"){
                                tiaojian_6="Name";
                            }else if(v=="标准号"){
                                tiaojian_6="BiaoNo";
                            }else if(v=="分类号"){
                                tiaojian_6="TypeNum";
                            }else if(v=="批准单位"){
                                tiaojian_6="RatifyDepartmentName";
                            }else if(v=="提出单位"){
                                tiaojian_6="TiChuDepartmentName";
                            }else if(v=="主编单位"){
                                tiaojian_6="EditorDepartmentName";
                            }else if(v=="起草人"){
                                tiaojian_6="Drafter";
                            }else if(v=="适用范围"){
                                tiaojian_6="Remark";
                            }else if(v=="引用标准"){
                                tiaojian_6="Referencesd";
                            }else if(v=="采用标准"){
                                tiaojian_6="Standards";
                            }
                        }.showPopupWindow(viewBind.searchContent.downJczt6)
                    }
                }
            }
            viewBind.searchContent.downJczt7 -> {

                dataList?.forEachIndexed { index, enum12 ->

                    if (index==dataList?.size!!-1){

                        DownPop(context,
                            enums12 = dataList,
                            checkedTextView = viewBind.searchContent.downJczt7 as AppCompatCheckedTextView,
                            isSingleChecked = true) { k, v, p ->
                            tiaojian_7=v

                            if(v=="标准名称"){
                                tiaojian_7="Name";
                            }else if(v=="标准号"){
                                tiaojian_7="BiaoNo";
                            }else if(v=="分类号"){
                                tiaojian_7="TypeNum";
                            }else if(v=="批准单位"){
                                tiaojian_7="RatifyDepartmentName";
                            }else if(v=="提出单位"){
                                tiaojian_7="TiChuDepartmentName";
                            }else if(v=="主编单位"){
                                tiaojian_7="EditorDepartmentName";
                            }else if(v=="起草人"){
                                tiaojian_7="Drafter";
                            }else if(v=="适用范围"){
                                tiaojian_7="Remark";
                            }else if(v=="引用标准"){
                                tiaojian_7="Referencesd";
                            }else if(v=="采用标准"){
                                tiaojian_7="Standards";
                            }
                        }.showPopupWindow(viewBind.searchContent.downJczt7)
                    }
                }
            }
            viewBind.searchContent.downJczt8 -> {

                dataList?.forEachIndexed { index, enum12 ->

                    if (index==dataList?.size!!-1){

                        DownPop(context,
                            enums12 = dataList,
                            checkedTextView = viewBind.searchContent.downJczt8 as AppCompatCheckedTextView,
                            isSingleChecked = true) { k, v, p ->
                            tiaojian_8=v
                            if(v=="标准名称"){
                                tiaojian_8="Name";
                            }else if(v=="标准号"){
                                tiaojian_8="BiaoNo";
                            }else if(v=="分类号"){
                                tiaojian_8="TypeNum";
                            }else if(v=="批准单位"){
                                tiaojian_8="RatifyDepartmentName";
                            }else if(v=="提出单位"){
                                tiaojian_8="TiChuDepartmentName";
                            }else if(v=="主编单位"){
                                tiaojian_8="EditorDepartmentName";
                            }else if(v=="起草人"){
                                tiaojian_8="Drafter";
                            }else if(v=="适用范围"){
                                tiaojian_8="Remark";
                            }else if(v=="引用标准"){
                                tiaojian_8="Referencesd";
                            }else if(v=="采用标准"){
                                tiaojian_8="Standards";
                            }

                        }.showPopupWindow(viewBind.searchContent.downJczt8)
                    }
                }
            }
            viewBind.searchContent.downJczt9 -> {

                dataList?.forEachIndexed { index, enum12 ->

                    if (index==dataList?.size!!-1){

                        DownPop(context,
                            enums12 = dataList,
                            checkedTextView = viewBind.searchContent.downJczt9 as AppCompatCheckedTextView,
                            isSingleChecked = true) { k, v, p ->
                            tiaojian_9=v
                            if(v=="标准名称"){
                                tiaojian_9="Name";
                            }else if(v=="标准号"){
                                tiaojian_9="BiaoNo";
                            }else if(v=="分类号"){
                                tiaojian_9="TypeNum";
                            }else if(v=="批准单位"){
                                tiaojian_9="RatifyDepartmentName";
                            }else if(v=="提出单位"){
                                tiaojian_9="TiChuDepartmentName";
                            }else if(v=="主编单位"){
                                tiaojian_9="EditorDepartmentName";
                            }else if(v=="起草人"){
                                tiaojian_9="Drafter";
                            }else if(v=="适用范围"){
                                tiaojian_9="Remark";
                            }else if(v=="引用标准"){
                                tiaojian_9="Referencesd";
                            }else if(v=="采用标准"){
                                tiaojian_9="Standards";
                            }

                        }.showPopupWindow(viewBind.searchContent.downJczt9)
                    }
                }
            }

            viewBind.searchHead.ivAdd -> {
                if(!tempRemove1){
                    tempRemove1=true
                    viewBind.searchContent.llSearch1.visibility = View.VISIBLE
                }else if(!tempRemove2){
                    tempRemove2=true
                    viewBind.searchContent.llSearch2.visibility = View.VISIBLE
                }else if(!tempRemove3){
                    tempRemove3=true
                    viewBind.searchContent.llSearch3.visibility = View.VISIBLE
                }else if(!tempRemove4){
                    tempRemove4=true
                    viewBind.searchContent.llSearch4.visibility = View.VISIBLE
                }else if(!tempRemove5){
                    tempRemove5=true
                    viewBind.searchContent.llSearch5.visibility = View.VISIBLE
                }else if(!tempRemove6){
                    tempRemove6=true
                    viewBind.searchContent.llSearch6.visibility = View.VISIBLE
                }
                else if(!tempRemove7){
                    tempRemove7=true
                    viewBind.searchContent.llSearch7.visibility = View.VISIBLE
                }else if(!tempRemove8){
                    tempRemove8=true
                    viewBind.searchContent.llSearch8.visibility = View.VISIBLE
                }else if(!tempRemove9){
                    tempRemove9=true
                    viewBind.searchContent.llSearch9.visibility = View.VISIBLE
                }
                else {
                    when (temp) {
                        0 -> {
                            temp=1
                            viewBind.searchContent?.llSearch1?.visibility = View.VISIBLE
                        }
                        1 -> {
                            temp=2
//                          var inflateView = LayoutInflater.from(requireContext()).inflate(R.layout.search_content_layout, viewBind.layoutManger, false);
//                        viewBind.layoutManger.addView(inflateView);

                            viewBind.searchContent?.llSearch2?.visibility = View.VISIBLE
                        }
                        2 -> {
                            temp=3

//                        var inflateView = LayoutInflater.from(requireContext()).inflate(R.layout.search_content_layout, viewBind.layoutManger, false);
//                        viewBind.layoutManger.addView(inflateView);
                            viewBind.searchContent?.llSearch3?.visibility = View.VISIBLE
                        }
                        3 -> {
                            temp=4

                            viewBind.searchContent?.llSearch4?.visibility = View.VISIBLE
                        }
                        4 -> {
                            temp=5

                            viewBind.searchContent?.llSearch5?.visibility = View.VISIBLE
                        }
                        5 -> {
                            temp=6

                            viewBind.searchContent?.llSearch6?.visibility = View.VISIBLE
                        }
                        6 -> {
                            temp=7

                            viewBind.searchContent?.llSearch7?.visibility = View.VISIBLE
                        }
                        7 -> {
                            temp=8

                            viewBind.searchContent?.llSearch8?.visibility = View.VISIBLE
                        }
                        8 -> {
                            temp=9

                            viewBind.searchContent?.llSearch9?.visibility = View.VISIBLE
                        }
                        else -> {
                            ToastUtils.showShort("条件筛选已上限")
                        }
                    }
                }
            }

            viewBind.searchContent.ivRemove1 -> {
                tempRemove1=false
                tiaojian_1=""
                viewBind.searchContent.etCustname.setText("")

                viewBind.searchContent.llSearch1.visibility = View.GONE
            }
            viewBind.searchContent.ivRemove2 -> {
                tempRemove2=false
                viewBind.searchContent.llSearch2.visibility = View.GONE
                tiaojian_2=""
                viewBind.searchContent.etCustname2.setText("")

            }
            viewBind.searchContent.ivRemove3 -> {
                tempRemove3=false
                viewBind.searchContent.llSearch3.visibility = View.GONE
                tiaojian_3=""

                viewBind.searchContent.etCustname3.setText("")

            }
            viewBind.searchContent.ivRemove4 -> {
                tempRemove4=false
                viewBind.searchContent.llSearch4.visibility = View.GONE
                tiaojian_4=""
                viewBind.searchContent.etCustname4.setText("")

            }
            viewBind.searchContent.ivRemove5 -> {
                tempRemove5=false
                viewBind.searchContent.llSearch5.visibility = View.GONE
                tiaojian_5=""
                viewBind.searchContent.etCustname5.setText("")

            }
            viewBind.searchContent.ivRemove6 -> {
                tempRemove6=false
                viewBind.searchContent.llSearch6.visibility = View.GONE
                tiaojian_6=""
                viewBind.searchContent.etCustname6.setText("")

            }
            viewBind.searchContent.ivRemove7 -> {
                tempRemove7=false
                viewBind.searchContent.llSearch7.visibility = View.GONE
                tiaojian_7=""
                viewBind.searchContent.etCustname7.setText("")

            }
            viewBind.searchContent.ivRemove8 -> {
                tempRemove8=false
                viewBind.searchContent.llSearch8.visibility = View.GONE
                tiaojian_8=""
                viewBind.searchContent.etCustname8.setText("")

            }
            viewBind.searchContent.ivRemove9 -> {
                tempRemove9=false
                viewBind.searchContent.llSearch9.visibility = View.GONE
                tiaojian_9=""
                viewBind.searchContent.etCustname9.setText("")

            }
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
        searchyinyong=str
        searchcaiyong=""
        viewBind.mRefreshLayout.autoRefresh()

    }
    /**
    根据选中采用标准更新
     */
    @Subscribe(thread = EventThread.MAIN_THREAD, tags = [Tag("searchcaiyong")])
    fun backRefresh3(str: String) {
        pager = 0
        searchcaiyong=str
        searchyinyong=""
        viewBind.mRefreshLayout.autoRefresh()

    }

}