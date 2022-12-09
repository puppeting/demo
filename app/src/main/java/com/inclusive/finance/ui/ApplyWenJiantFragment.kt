package com.inclusive.finance.ui

import android.app.Activity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatCheckedTextView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.bigkoo.pickerview.builder.TimePickerBuilder
import com.bigkoo.pickerview.view.TimePickerView
import com.blankj.utilcode.util.ObjectUtils
import com.blankj.utilcode.util.SPUtils
import com.blankj.utilcode.util.TimeUtils
import com.blankj.utilcode.util.ToastUtils
import com.hwangjr.rxbus.annotation.Subscribe
import com.hwangjr.rxbus.annotation.Tag
import com.hwangjr.rxbus.thread.EventThread
import com.inclusive.finance.IRouter
import com.inclusive.finance.R
import com.inclusive.finance.adapter.BiaoZhunDataAdapter
import com.inclusive.finance.app.ToolApplication
import com.inclusive.finance.base.MyBaseFragment
import com.inclusive.finance.bean.BaseTypeBean
import com.inclusive.finance.bean.TooBarBean

import com.inclusive.finance.bean.model.ApplyModel
import com.inclusive.finance.bean.model.MainActivityModel
import com.inclusive.finance.config.Constants
import com.inclusive.finance.config.Urls
import com.inclusive.finance.databinding.FragmentWenjianapplyListBinding
import com.inclusive.finance.interfaces.PresenterClick
import com.inclusive.finance.pop.*
import com.inclusive.finance.room.Standard
import com.inclusive.finance.utils.StatusBarUtil
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener
import com.zp.z_file.content.getZFileHelp
import com.zp.z_file.dsl.result
import org.jetbrains.anko.support.v4.act
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.LinkedHashMap


/**
 * 授信申请列表
 * */
class ApplyWenJiantFragment : MyBaseFragment(), PresenterClick, OnRefreshLoadMoreListener {
    private var dataEditorDepartmentName: ArrayList<BaseTypeBean.Enum12>? = arrayListOf()
    private var dataStartDepartmentName: ArrayList<BaseTypeBean.Enum12>?= arrayListOf()
    private var dataRatifyDepartmentName: ArrayList<BaseTypeBean.Enum12>?= ArrayList()
    private var dataDrafter: ArrayList<BaseTypeBean.Enum12>?= ArrayList()
    private var dataTypeNum: ArrayList<BaseTypeBean.Enum12>? = ArrayList()
    private   var sxStatus: String = ""
    private   var biaozhunStatus: String = ""
    private   var paixunStatus: String = ""
    private   var qicaorenStatus: String = ""
    private   var pizhunStatus: String = ""
    private   var yinyongStatus: String = ""
    private   var caiyongStatus: String = ""
    private   var tichuStatus: String = ""
    private   var zhubianStatus: String = ""
    private   var fenleiStatus: String = ""

    private   var tichu: String = ""
    private   var startTime: String = ""

     private var mAdapter: BiaoZhunDataAdapter<Standard>? = null
    var titleList: ArrayList<String>? = arrayListOf( )
    lateinit var viewModel: MainActivityModel
    lateinit var viewBind: FragmentWenjianapplyListBinding
    private var refreshState = Constants.RefreshState.STATE_REFRESH
    private var currentPage = 1
     var event: Lifecycle.Event? = null
    var getListUrl=""
    var xdtbUrl=""
    var Standard:ArrayList<Standard>?= arrayListOf()
    private var businessType:Int=ApplyModel.BUSINESS_TYPE_APPLY
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        viewModel = ViewModelProvider(act).get(MainActivityModel::class.java)
        viewBind = FragmentWenjianapplyListBinding.inflate(inflater, container, false).apply {
            data = viewModel
            presenterClick = this@ApplyWenJiantFragment
            lifecycleOwner = viewLifecycleOwner
        }


        return viewBind.root
    }
    var enums12: LinkedHashMap<String, ArrayList<BaseTypeBean.Enum12>>?= LinkedHashMap()
    override fun initView() {
        businessType =
            arguments?.getInt("businessType", ApplyModel.BUSINESS_TYPE_APPLY) ?: businessType
        viewBind.actionBarCustom.toolbar.setNavigationOnClickListener {
            Navigation.findNavController(act, R.id.my_nav_host_fragment).navigateUp()
        }
        StatusBarUtil.setPaddingSmart(act, viewBind.actionBarCustom.appBar)
        mAdapter = BiaoZhunDataAdapter()
//        mAdapter?.setEmptyView(LayoutInflater.from(context).inflate(R.layout.empty_view, null))
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
        viewBind.layoutBaseList.mRecyclerView.layoutManager = LinearLayoutManager(activity)
        viewBind.layoutBaseList.mRecyclerView.adapter = mAdapter
        if(SPUtils.getInstance().getString(Constants.SPUtilsConfig.SP_LOGINNAME)!="admin"){
            viewBind?.actionBarCustom?.btSystem?.visibility=View.GONE
            viewBind?.actionBarCustom?.btFiel?.visibility=View.GONE

        }
        viewBind.mRefreshLayout.setOnRefreshLoadMoreListener(this)
        mAdapter?.setOnItemClickListener { adapter, _, position ->
            mAdapter?.data?.forEachIndexed { index, recordsBean ->
                recordsBean.isClick = false
                adapter.notifyItemChanged(index)
            }
            mAdapter?.data?.get(position)?.isClick = true
            adapter.notifyItemChanged(position)
        }


        val dataList = ArrayList<BaseTypeBean.Enum12>()

        processStatus = "有效"
        dataList.clear()
        dataList.add(BaseTypeBean.Enum12().apply {
            valueName = "有效"
            keyName = "有效"
        })
        dataList.add(BaseTypeBean.Enum12().apply {
            valueName = "废止"
            keyName = "废止"
        })

        val dataList2 = ArrayList<BaseTypeBean.Enum12>()

        sxStatus = "ASC"
        dataList2.clear()
        dataList2.add(BaseTypeBean.Enum12().apply {
            valueName = "降序"
            keyName = "DESC"
        })
        dataList2.add(BaseTypeBean.Enum12().apply {
            valueName = "升序"
            keyName = "ASC"
        })
        var mbean = TooBarBean()
        mbean.type = 2
        viewBind.actionBarCustom.dataType = mbean
        viewBind.tvSendDate.setOnClickListener {

            val pvTime: TimePickerView = TimePickerBuilder(context) { date, _ ->
                startTime =
                    TimeUtils.date2String(date, SimpleDateFormat("yyyy-MM-dd", Locale.CHINA))
                viewBind.tvSendDate.text = startTime
            }.isDialog(true).build()
            pvTime.dialog.window?.setGravity(Gravity.BOTTOM)
            pvTime.dialog.window?.setWindowAnimations(R.style.picker_view_slide_anim)
            pvTime.setOnDismissListener { viewBind.tvSendDate.isChecked = false }
            viewBind.tvSendDate.isChecked = true
            pvTime.show()

        }
        viewBind?.tvFabuChongzhi?.setOnClickListener {
            viewBind.tvSendDate.text=""
            viewBind.tvSendDateend.text=""
            viewBind.tvShishiriqi.text=""
            viewBind.tvShishiDateend.text=""
            viewBind.roundZhuangtai.text=""
            viewBind.tvConfirm.text=""
            viewBind.tvQicaoren.text=""
            viewBind.tvFenleihao.setText("")
            viewBind.tvPizhundanwei.setText("")
            viewBind.tvYinyongbiaozhun.setText("")
            viewBind.tvCaiyongbiaozhun.setText("")
            viewBind.tvTichudanwei.setText("")
            pager=0
            viewBind?.mRefreshLayout?.autoRefresh()
        }
        viewBind.tvSendDateend.setOnClickListener {

            val pvTime: TimePickerView = TimePickerBuilder(context) { date, _ ->

                 viewBind.tvSendDateend.text = TimeUtils.date2String(date, SimpleDateFormat("yyyy-MM-dd", Locale.CHINA))
            }.isDialog(true).build()
            pvTime.dialog.window?.setGravity(Gravity.BOTTOM)
            pvTime.dialog.window?.setWindowAnimations(R.style.picker_view_slide_anim)
            pvTime.setOnDismissListener { viewBind.tvSendDateend.isChecked = false }
            viewBind.tvSendDateend.isChecked = true
            pvTime.show()

        }
//        viewBind.tvShishiriqi.setOnClickListener {
//
//            val pvTime: TimePickerView = TimePickerBuilder(context) { date, _ ->
//                startTime =
//                    TimeUtils.date2String(date, SimpleDateFormat("yyyy-MM-dd", Locale.CHINA))
//                viewBind.tvShishiriqi.text = startTime
//            }.isDialog(true).build()
//            pvTime.dialog.window?.setGravity(Gravity.BOTTOM)
//            pvTime.dialog.window?.setWindowAnimations(R.style.picker_view_slide_anim)
//            pvTime.setOnDismissListener { viewBind.tvShishiriqi.isChecked = false }
//            viewBind.tvShishiriqi.isChecked = true
//            pvTime.show()
//
//        }
//        viewBind.tvFenleihao.setOnClickListener {
//
//
//        }
        viewBind?.roundZhuangtai?.setOnClickListener {
            DownPop(context,
                enums12 = dataList,
                checkedTextView = it as AppCompatCheckedTextView,
                isSingleChecked = true) { k, v, p ->
                biaozhunStatus = if (v == "全部") {
                    ""
                } else {
                    k
                }
            }.showPopupWindow(it)
        }
        viewBind?.tvConfirm?.setOnClickListener {
            DownPop(context,
                enums12 = dataList2,
                checkedTextView = it as AppCompatCheckedTextView,
                isSingleChecked = true) { k, v, p ->
                sxStatus = if (v == "全部") {
                    ""
                } else {
                    k
                }
            }.showPopupWindow(it)
        }
        initDownView()
        initStatusView()
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
//        mAdapter?.setOnItemClickListener { adapter, view, position ->
//            IRouter.goF(view, R.id.action_to_navActivity, "文件详情", "")
//        }
        var list = ToolApplication.singletonTest(requireContext()).getListFenLeiNum("TypeNum")
        dataTypeNum = ArrayList<BaseTypeBean.Enum12>()
        if (list != null) {
            list.forEachIndexed { index, s ->
                var bean = BaseTypeBean.Enum12()
                bean.valueName = s
                bean.keyName = s
                dataTypeNum?.add(bean)
                if (index == list.size - 1) {

                }

            }
        }
        var listStartDepartmentName =
            ToolApplication.singletonTest(requireContext()).getListFenLeiNum("StartDepartmentName")
        dataStartDepartmentName = ArrayList<BaseTypeBean.Enum12>()
        if (listStartDepartmentName != null)
            listStartDepartmentName.forEachIndexed { index, s ->
                var bean = BaseTypeBean.Enum12()
                bean.valueName = s
                bean.keyName = s
                dataStartDepartmentName?.add(bean)
            }
        dataEditorDepartmentName = ArrayList<BaseTypeBean.Enum12>()

        var listEditorDepartmentName =
            ToolApplication.singletonTest(requireContext()).getListFenLeiNum("EditorDepartmentName")
         if (listStartDepartmentName != null)
            listEditorDepartmentName.forEachIndexed { index, s ->
                var bean = BaseTypeBean.Enum12()
                bean.valueName = s
                bean.keyName = s
                dataEditorDepartmentName?.add(bean)
            }

        dataDrafter = ArrayList<BaseTypeBean.Enum12>()
        ToolApplication.singletonTest(requireContext()).getListFenLeiNum("Drafter")
            ?.forEachIndexed { index, s ->
                var bean = BaseTypeBean.Enum12()
                bean.valueName = s
                bean.keyName = s
                dataDrafter?.add(bean)
            }
        dataRatifyDepartmentName = ArrayList<BaseTypeBean.Enum12>()
        ToolApplication.singletonTest(requireContext()).getListFenLeiNum("RatifyDepartmentName")
            ?.forEachIndexed { index, s ->
                var bean = BaseTypeBean.Enum12()
                bean.valueName = s
                bean.keyName = s
                dataRatifyDepartmentName?.add(bean)
            }
        onRefresh(viewBind?.mRefreshLayout as RefreshLayout)

    }

    private fun initDownView() {


    }

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


    }
    private fun initStatusView() {

    }

    var searchStr = ""
    var processStatus = ""
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
    var pager=0;
    var temp=true
    override fun initData() {
         var list =ToolApplication.singletonTest(requireContext()).getListBiaoZhunDate(temp,
                viewBind?.tvShishiriqi?.text?.toString(),viewBind?.tvSendDate?.text?.toString(),viewBind?.tvFenleihao?.text?.toString(),
             viewBind?.tvPizhundanwei?.text?.toString(),viewBind?.tvTichudanwei?.text?.toString(),viewBind?.tvZhubiandanwei?.text?.toString(),
             viewBind?.tvQicaoren?.text?.toString(),pager,"",""+viewBind?.roundZhuangtai?.text?.toString(),
             viewBind?.tvShishiDateend?.text.toString(),viewBind?.tvSendDateend?.text.toString(),sxStatus,""+viewBind?.tvYinyongbiaozhun?.text?.toString(),""+viewBind?.tvCaiyongbiaozhun?.text?.toString())

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
        pager=0
        initData()
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
    val select: Standard?
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
            pager=0

            viewBind.mRefreshLayout.autoRefresh()

            return
        }
        when(v){
            viewBind.sbDiaocha->{
                if (ObjectUtils.isNotEmpty(select)) {

                    IRouter.goF(viewBind.sbDiaocha, R.id.action_to_navActivity, "文件详情", ""+select?.id)

                } else {
                    ToastUtils.showLong("暂未选取数据")
                }
            }
            viewBind.sbUpdate->{
                if (ObjectUtils.isNotEmpty(select)) {

                    IRouter.goF(viewBind.sbAdd, R.id.action_to_navActivity, "文件上传", ""+select?.id)

                } else {
                    ToastUtils.showLong("暂未选取数据")
                }
            }
            viewBind.sbShanchu->{
                if (ObjectUtils.isNotEmpty(select)) {
                    ToolApplication.singletonTest(requireContext()).deleteStatandData(select?.id?.toInt())
                    ToastUtils.showShort("删除成功")
                    pager=0
                    viewBind?.mRefreshLayout?.autoRefresh()
                } else {
                    ToastUtils.showLong("暂未选取数据")
                }
            }
            viewBind.sbAdd->{

                IRouter.goF(viewBind.sbAdd, R.id.action_to_navActivity, "文件上传", "")

            }
            viewBind.sbAdd->{
//                AddPop(requireContext(),"","","","") {
//
//                }.showPopupWindow()
                IRouter.goF(viewBind.sbAdd, R.id.action_to_navActivity, "文件上传", "")

            }
            viewBind?.tvFenleihao->{
//                DownPop(context, enums12 = dataTypeNum, checkedTextView = viewBind.tvFenleihao as AppCompatCheckedTextView, isSingleChecked = true) { k, v, p ->
//                    fenleiStatus = if(v=="全部"){
//                        ""
//                    }else{
//                        k
//                    }
//                    viewBind?.tvFenleihao?.text = v
//                }.showPopupWindow(viewBind.tvFenleihao)
            }
            viewBind?.tvPizhundanwei->{
//                DownPop(context, enums12 = dataRatifyDepartmentName, checkedTextView = viewBind.tvPizhundanwei as AppCompatCheckedTextView, isSingleChecked = true) { k, v, p ->
//                    pizhunStatus = if(v=="全部"){
//                        ""
//                    }else{
//                        k
//                    }
//                    viewBind?.tvPizhundanwei?.text = v
//                }.showPopupWindow(viewBind.tvPizhundanwei)
            }
            viewBind?.tvQicaoren->{
//                DownPop(context, enums12 = dataDrafter, checkedTextView = viewBind.tvQicaoren as AppCompatCheckedTextView, isSingleChecked = true) { k, v, p ->
//                    qicaorenStatus = if(v=="全部"){
//                        ""
//                    }else{
//                        k
//                    }
//                    viewBind?.tvQicaoren?.text = v
//                }.showPopupWindow(viewBind.tvQicaoren)
            }
            viewBind?.tvTichudanwei->{
//                DownPop(context, enums12 = dataStartDepartmentName, checkedTextView = viewBind.tvTichudanwei as AppCompatCheckedTextView, isSingleChecked = true) { k, v, p ->
//                    tichuStatus = if(v=="全部"){
//                        ""
//                    }else{
//                        k
//                    }
//                    viewBind?.tvTichudanwei?.text = v
//                }.showPopupWindow(viewBind.tvTichudanwei)
            }
            viewBind?.tvZhubiandanwei->{
//                DownPop(context, enums12 = dataEditorDepartmentName, checkedTextView = viewBind.tvZhubiandanwei as AppCompatCheckedTextView, isSingleChecked = true) { k, v, p ->
//                    zhubianStatus = if(v=="全部"){
//                        ""
//                    }else{
//                        k
//                    }
//                    viewBind?.tvZhubiandanwei?.text = v
//                }.showPopupWindow(viewBind.tvZhubiandanwei)
            }

            viewBind?.tvShishiriqi->{
                val pvTime: TimePickerView = TimePickerBuilder(context) { date, _ ->
                    startTime = TimeUtils.date2String(date, SimpleDateFormat("yyyy-MM-dd", Locale.CHINA))
                    viewBind.tvShishiriqi.text =startTime
                }.isDialog(true).build()
                pvTime.dialog.window?.setGravity(Gravity.BOTTOM)
                pvTime.dialog.window?.setWindowAnimations(R.style.picker_view_slide_anim)
                pvTime.setOnDismissListener { viewBind.tvShishiriqi.isChecked = false }
                viewBind.tvShishiriqi.isChecked = true
                pvTime.show()
            }
            viewBind?.tvShishiDateend->{
                val pvTime: TimePickerView = TimePickerBuilder(context) { date, _ ->
                     viewBind.tvShishiDateend.text =TimeUtils.date2String(date, SimpleDateFormat("yyyy-MM-dd", Locale.CHINA))
                }.isDialog(true).build()
                pvTime.dialog.window?.setGravity(Gravity.BOTTOM)
                pvTime.dialog.window?.setWindowAnimations(R.style.picker_view_slide_anim)
                pvTime.setOnDismissListener { viewBind.tvShishiDateend.isChecked = false }
                viewBind.tvShishiDateend.isChecked = true
                pvTime.show()
            }

        }


    }

    /**
    返回后刷新数据，
     */
    @Subscribe(thread = EventThread.MAIN_THREAD, tags = [Tag(Constants.BusAction.Bus_Refresh_List)])
    fun backRefresh(str: String) {
        pager=0
        viewBind?.mRefreshLayout?.autoRefresh()
     }
    /**
    返回后刷新数据，
     */
    @Subscribe(thread = EventThread.MAIN_THREAD, tags = [Tag("upwenjian")])
    fun backRefresh2(str: String) {
        Log.e("ddddd","ddddaa")
        val appDir = File(
            Environment.getExternalStorageDirectory()
                .toString() + File.separator + "download"
        )
        if (!appDir.exists()) {
            appDir.mkdirs()
        }
        try {
            // 普通 方式
            getZFileHelp().result(context as Activity) {
                copyfile(File(this!![0].filePath),appDir,true)
            }
        } catch (e: IOException) {
            // TODO Auto-generated catch block
            e.printStackTrace()
        }
    }
    fun copyfile(fromFile: File, toFile: File, rewrite: Boolean) {
        if (!fromFile.exists()) {
            return
        }
        if (!fromFile.isFile) {
            return
        }
        if (!fromFile.canRead()) {
            return
        }
        if (!toFile.parentFile.exists()) {
            toFile.parentFile.mkdirs()
        }
        if (toFile.exists() && rewrite) {
            toFile.delete()
        }


        // if (!toFile.canWrite()) {

        // MessageDialog.openError(new Shell(),"错误信息","不能够写将要复制的目标文件" + toFile.getPath());

        // Toast.makeText(this,"不能够写将要复制的目标文件", Toast.LENGTH_SHORT);

        // return ;

        // }
        try {
            val fosfrom = FileInputStream(fromFile)
            val fosto: FileOutputStream = FileOutputStream(toFile)
            val bt = ByteArray(1024)
            var c: Int
            while (fosfrom.read(bt).also { c = it } > 0) {
                fosto.write(bt, 0, c) //将内容写到新文件当中
            }
            fosfrom.close()
            fosto.close()
        } catch (ex: Exception) {
            Log.e("readfile", ex.message!!)
        }
    }

}