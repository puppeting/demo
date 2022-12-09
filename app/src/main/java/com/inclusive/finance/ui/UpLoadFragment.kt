package com.inclusive.finance.ui

import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatCheckedTextView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.bigkoo.pickerview.builder.TimePickerBuilder
import com.bigkoo.pickerview.view.TimePickerView
import com.blankj.utilcode.util.TimeUtils
import com.blankj.utilcode.util.ToastUtils
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.hwangjr.rxbus.annotation.Subscribe
import com.hwangjr.rxbus.annotation.Tag
import com.hwangjr.rxbus.thread.EventThread
import com.inclusive.finance.R
import com.inclusive.finance.adapter.*
import com.inclusive.finance.app.ToolApplication
import com.inclusive.finance.base.MyBaseFragment
import com.inclusive.finance.bean.AddBean
import com.inclusive.finance.bean.BaseTypeBean
import com.inclusive.finance.bean.model.ApplyModel
import com.inclusive.finance.config.Constants
import com.inclusive.finance.config.Urls
import com.inclusive.finance.databinding.FragmentUploadBinding
import com.inclusive.finance.pop.DownPop
import com.inclusive.finance.room.Standard
import com.inclusive.finance.room.SysLaiYuan
import com.inclusive.finance.utils.StatusBarUtil
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener
import com.zp.z_file.content.getZFileHelp
import com.zp.z_file.dsl.result
import com.zp.z_file.listener.ZFileImageListener
import org.apache.commons.io.IOUtils
import org.jetbrains.anko.support.v4.act
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


/**
 * 详情页
 * */
class UpLoadFragment : MyBaseFragment(), View.OnClickListener, OnRefreshLoadMoreListener {
    private var mulist2: ArrayList<BaseTypeBean.Enum12>? = arrayListOf()
    private var listLaiYuan: MutableList<SysLaiYuan>? = null
    private var mAdapter2: CustomAdapter2 ? = null
    private var mAdapter1: CustomAdapter?= null
    private var Referencesd: String?=""
    private var Standards: String?=""


    private var beand: Standard? = null
    private   var mpath: String = ""
    private   var biaozhun: String = ""
    private   var pizhun: String = ""

    private   var tichu: String = ""
    var yinyongList: ArrayList<String>? = arrayListOf( )
    var caiyongList: ArrayList<String>? = arrayListOf( )

    private var adapter2: CustomExpandableListAdapter? = null
    private var mAdapter: DataAdapter<Standard>? = null
    var titleList: ArrayList<String>? = arrayListOf( )
    lateinit var viewModel: ApplyModel
    lateinit var viewBind: FragmentUploadBinding
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
        viewModel = ViewModelProvider(act).get(ApplyModel::class.java)
        viewBind = FragmentUploadBinding.inflate(inflater, container, false).apply {
            data = viewModel
             lifecycleOwner = viewLifecycleOwner
        }
        return viewBind.root
    }
    private var dataEditorDepartmentName: java.util.ArrayList<BaseTypeBean.Enum12>? = arrayListOf()
    private var dataStartDepartmentName: java.util.ArrayList<BaseTypeBean.Enum12>?= arrayListOf()
    private var dataRatifyDepartmentName: java.util.ArrayList<BaseTypeBean.Enum12>?=
        java.util.ArrayList()
    private var dataDrafter: java.util.ArrayList<BaseTypeBean.Enum12>?= java.util.ArrayList()
    private var dataTypeNum: java.util.ArrayList<BaseTypeBean.Enum12>? = java.util.ArrayList()
    private var dataList: java.util.ArrayList<BaseTypeBean.Enum12>? = java.util.ArrayList()
    var mbean2=AddBean()
    private fun getData( ) {
        dataList = java.util.ArrayList<BaseTypeBean.Enum12>()

        dataList?.clear()
        dataList?.add(BaseTypeBean.Enum12().apply {
            valueName = "有效"
            keyName = "有效"
        })
        dataList?.add(BaseTypeBean.Enum12().apply {
            valueName = "废止"
            keyName = "废止"
        })
        mAdapter2 = CustomAdapter2(caiyongList,requireContext())
        var mlayout3= LinearLayoutManager(context)
        mlayout3.orientation= LinearLayoutManager.HORIZONTAL
        viewBind?.recyCaiyong?.layoutManager = mlayout3
        viewBind?.recyCaiyong?.adapter = mAdapter2

        mAdapter1 = CustomAdapter(yinyongList,requireContext())
        var mlayout2= LinearLayoutManager(context)
        mlayout2.orientation= LinearLayoutManager.HORIZONTAL
        viewBind?.recyYinyong?.layoutManager = mlayout2
        viewBind?.recyYinyong?.adapter = mAdapter1

        var list = ToolApplication.singletonTest(requireContext()).getListFenLeiNum("TypeNum")
        dataTypeNum = java.util.ArrayList<BaseTypeBean.Enum12>()
        if(list!=null) {
            list.forEachIndexed { index, s ->
                var bean = BaseTypeBean.Enum12()
                bean.valueName = s
                bean.keyName = s
                dataTypeNum?.add(bean)
                if (index == list.size - 1) {

                }
            }
        }
        var listStartDepartmentName = ToolApplication.singletonTest(requireContext()).getListFenLeiNum("StartDepartmentName")
        dataStartDepartmentName = java.util.ArrayList<BaseTypeBean.Enum12>()
        if (viewModel.keyId!=""){
            viewBind?.actionBarCustom?.toolbar?.title="修改"

            viewBind?.tvMyInfo?.setText("修改")
            var listRatifyDepartmentName = ToolApplication.singletonTest(requireContext()).getInfo(viewModel.keyId)
             beand=Standard()
            beand=listRatifyDepartmentName.get(0)
            if (beand?.filePath!=""&&beand?.filePath!=null){
                var mfiel = File(beand?.filePath)
                var FILENAME = mfiel.name
                viewBind?.tvWenjianName?.setText(FILENAME)
                viewBind?.sbShangchuan?.setText("重新上传")
                viewBind?.tvWenjianName?.visibility=View.VISIBLE
            }


            if(beand?.referencesd!=null&&beand?.referencesd!=""){
                var mlist=beand?.referencesd?.split(",")
                mlist?.forEachIndexed { index, s ->
                    if(s!=""){
                        yinyongList?.add(s)
                    }
                    if (index==mlist?.size!!-1){
                        mAdapter1?.setUsers(yinyongList)
                    }
                }
             }
            if(beand?.standards!=null&&beand?.standards!=""){
                var mlist=beand?.standards?.split(",")
                mlist?.forEachIndexed { index, s ->
                    if(s!=""){
                        caiyongList?.add(s)
                    }
                    if (index==mlist?.size!!-1){
                        mAdapter2?.setUsers(caiyongList)
                    }
                }
            }
//            beand?.releaseTime=""+listRatifyDepartmentName.get(0).releaseTime
//            beand?.name=""+listRatifyDepartmentName.get(0).name
//            beand?.Status=""+listRatifyDepartmentName.get(0).Status
//
//            beand?.startTime=""+listRatifyDepartmentName.get(0).startTime
//            beand?.typeNum=""+listRatifyDepartmentName.get(0).typeNum
//            beand?.RatifyDepartmentName=""+listRatifyDepartmentName.get(0).RatifyDepartmentName
//            beand?.StartDepartmentName=""+listRatifyDepartmentName.get(0).StartDepartmentName
//            beand?.EditorDepartmentName=""+listRatifyDepartmentName.get(0).EditorDepartmentName
//            beand?.drafter=""+listRatifyDepartmentName.get(0).drafter
//            beand?.Remark=""+listRatifyDepartmentName.get(0).Remark
//            beand?.BiaoNo=""+listRatifyDepartmentName.get(0).BiaoNo
//            beand?.SameClassNo=""+listRatifyDepartmentName.get(0).SameClassNo//同一分类号
//            beand?.pages=""+listRatifyDepartmentName.get(0).pages
//            beand?.replaceNo=""+listRatifyDepartmentName.get(0).replaceNo
//            beand?.byReplaceNo=""+listRatifyDepartmentName.get(0).byReplaceNo
//            beand?.Remark=""+listRatifyDepartmentName.get(0).Remark
//            beand?.Remark=""+listRatifyDepartmentName.get(0).Remark
//            beand?.Remark=""+listRatifyDepartmentName.get(0).Remark
//            beand?.Remark=""+listRatifyDepartmentName.get(0).Remark
//            beand?.Remark=""+listRatifyDepartmentName.get(0).Remark
//            beand?.Remark=""+listRatifyDepartmentName.get(0).Remark
//            beand?.Remark=""+listRatifyDepartmentName.get(0).Remark
//            beand?.Remark=""+listRatifyDepartmentName.get(0).Remark

//        beand.releaseTime="发布日期："+listRatifyDepartmentName.get(0).releaseTime
            mpath=""+listRatifyDepartmentName.get(0).FilePath
            viewBind?.datanum= beand
        }else{
            viewBind?.actionBarCustom?.toolbar?.title="新增"

        }

        listStartDepartmentName?.forEachIndexed { index, s ->
            var bean = BaseTypeBean.Enum12()
            bean.valueName = s
            bean.keyName = s
            dataStartDepartmentName?.add(bean)
        }
        var listEditorDepartmentName = ToolApplication.singletonTest(requireContext()).getListFenLeiNum("EditorDepartmentName")
        dataEditorDepartmentName = java.util.ArrayList<BaseTypeBean.Enum12>()

        listEditorDepartmentName?.forEachIndexed { index, s ->
            var bean = BaseTypeBean.Enum12()
            bean.valueName = s
            bean.keyName = s
            dataEditorDepartmentName?.add(bean)


        }
        var listDrafter = ToolApplication.singletonTest(requireContext()).getListFenLeiNum("Drafter")
        dataDrafter = java.util.ArrayList<BaseTypeBean.Enum12>()

        listDrafter?.forEachIndexed { index, s ->
            var bean = BaseTypeBean.Enum12()
            bean.valueName = s
            bean.keyName = s
            dataDrafter?.add(bean)
        }
        var listRatifyDepartmentName = ToolApplication.singletonTest(requireContext()).getListFenLeiNum("RatifyDepartmentName")
        dataRatifyDepartmentName = java.util.ArrayList<BaseTypeBean.Enum12>()

        listRatifyDepartmentName?.forEachIndexed { index, s ->
            var bean = BaseTypeBean.Enum12()
            bean.valueName = s
            bean.keyName = s
            dataRatifyDepartmentName?.add(bean)
        }
    }
    var enums12: LinkedHashMap<String, ArrayList<BaseTypeBean.Enum12>>?= LinkedHashMap()
    override fun initView() {
        businessType=arguments?.getInt("businessType",ApplyModel.BUSINESS_TYPE_APPLY)?:businessType
        viewBind.actionBarCustom.toolbar.setNavigationOnClickListener {
            Navigation.findNavController(act, R.id.my_nav_host_fragment).navigateUp()
        }
        viewBind?.actionBarCustom?.btShoucang?.visibility=View.GONE
        viewBind?.actionBarCustom?.btSearch?.visibility=View.GONE
        viewBind?.actionBarCustom?.btFiel?.visibility=View.GONE
        viewBind?.actionBarCustom?.btSystem?.visibility=View.GONE
        StatusBarUtil.setPaddingSmart(act, viewBind.actionBarCustom.appBar)
        mAdapter = DataAdapter()
        mAdapter?.setEmptyView(LayoutInflater.from(context).inflate(R.layout.empty_view, null))
        viewBind?.sbAdd?.setOnClickListener (this)
        viewBind?.roundBiaozhuangtai?.setOnClickListener (this)
        viewBind?.roundFenleihao?.setOnClickListener (this)
        viewBind?.roundPizhundanwei?.setOnClickListener (this)
        viewBind?.roundQicaoren?.setOnClickListener (this)
        viewBind?.roundShishidanwei?.setOnClickListener (this)
        viewBind?.roundShishishijian?.setOnClickListener (this)
        viewBind?.roundStarttime?.setOnClickListener (this)
        viewBind?.roundZhubiandanwei?.setOnClickListener (this)
        viewBind?.sbShangchuan?.setOnClickListener (this)
        viewBind?.sbCaiyongAdd?.setOnClickListener (this)
        viewBind?.sbYinyongAdd?.setOnClickListener (this)
        viewBind?.sbYinyongRemove1?.setOnClickListener (this)
        viewBind?.sbYinyongRemove2?.setOnClickListener (this)
        viewBind?.sbYinyongRemove3?.setOnClickListener (this)
        viewBind?.sbYinyongRemove4?.setOnClickListener (this)
        viewBind?.sbYinyongRemove5?.setOnClickListener (this)
        viewBind?.sbYinyongRemove6?.setOnClickListener (this)
        viewBind?.sbYinyongRemove7?.setOnClickListener (this)
        viewBind?.sbYinyongRemove8?.setOnClickListener (this)
        viewBind?.roundSourceName?.setOnClickListener (this)
// 在调用前初始化
        getZFileHelp().init(MyFileImageListener())
        mAdapter?.setNewInstance(Standard)
        getData( )
         initDownView()
        initStatusView()
        initApplyStatusView()
          mulist2=ArrayList<BaseTypeBean.Enum12>()

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
        viewBind?.addbean=mbean2
        listLaiYuan =
            ToolApplication.singletonTest(requireContext()).getListLaiYuan("",0)
        listLaiYuan?.forEachIndexed { index, sysLaiYuan ->
            var bean=BaseTypeBean.Enum12()
            bean.valueName=sysLaiYuan.name
            bean.keyName=sysLaiYuan.name
            mulist2?.add(bean)
        }
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

    var searchStr = " "
    var processStatus = " "
    override fun initEvent() {
        viewBind?.actionBarCustom?.btShoucang?.visibility=View.GONE
        viewBind?.actionBarCustom?.btSearch?.visibility=View.GONE
        viewBind?.actionBarCustom?.btFiel?.visibility=View.GONE
        viewBind?.actionBarCustom?.btSystem?.visibility=View.GONE

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

     }

    override fun refreshData(type: Int?) {
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
      var mtemp=1
    private var tempRemove1: Boolean = true
    private var tempRemove2: Boolean = true
    private var tempRemove3: Boolean = true
    private var tempRemove4: Boolean = true
    private var tempRemove5: Boolean = true
    private var tempRemove6: Boolean = true
    private var tempRemove7: Boolean = true
    private var tempRemove8: Boolean = true
    private var tempRemove9: Boolean = true

      override fun onClick(v: View?) {

        when (v) {
            viewBind.sbYinyongRemove1->{
                tempRemove1=false
                viewBind?.tvYinyongbiaozhu1?.setText("")
                viewBind?.sbYinyongRemove1?.visibility=View.GONE
                viewBind?.rvYinyongbiaozhu1?.visibility=View.GONE
            }
            viewBind.sbYinyongRemove2->{
                tempRemove2=false
                viewBind?.tvYinyongbiaozhu2?.setText("")
                viewBind?.sbYinyongRemove2?.visibility=View.GONE
                viewBind?.rvYinyongbiaozhu2?.visibility=View.GONE
            }
            viewBind.sbYinyongRemove3->{
                tempRemove3=false
                viewBind?.tvYinyongbiaozhu3?.setText("")
                viewBind?.sbYinyongRemove3?.visibility=View.GONE
                viewBind?.rvYinyongbiaozhu3?.visibility=View.GONE
            }
            viewBind.sbYinyongRemove4->{
                tempRemove4=false
                viewBind?.tvYinyongbiaozhu4?.setText("")
                viewBind?.sbYinyongRemove4?.visibility=View.GONE
                viewBind?.rvYinyongbiaozhu4?.visibility=View.GONE
            }
            viewBind.sbYinyongRemove5->{
                tempRemove5=false
                viewBind?.tvYinyongbiaozhu5?.setText("")
                viewBind?.sbYinyongRemove5?.visibility=View.GONE
                viewBind?.rvYinyongbiaozhu5?.visibility=View.GONE
            }
            viewBind.sbYinyongRemove6->{
                tempRemove6=false
                viewBind?.tvYinyongbiaozhu6?.setText("")
                viewBind?.sbYinyongRemove6?.visibility=View.GONE
                viewBind?.rvYinyongbiaozhu6?.visibility=View.GONE
            }
            viewBind.sbYinyongRemove7->{
                tempRemove7=false
                viewBind?.tvYinyongbiaozhu7?.setText("")
                viewBind?.sbYinyongRemove7?.visibility=View.GONE
                viewBind?.rvYinyongbiaozhu7?.visibility=View.GONE
            }
            viewBind.sbYinyongRemove8->{
                tempRemove8=false
                viewBind?.tvYinyongbiaozhu8?.setText("")
                viewBind?.sbYinyongRemove8?.visibility=View.GONE
                viewBind?.rvYinyongbiaozhu8?.visibility=View.GONE
            }
            viewBind.sbYinyongAdd->{
                yinyongList?.add("")
                if(yinyongList!=null&&yinyongList?.size!!>0){
                    mAdapter1?.notifyItemChanged(yinyongList?.size!!)

                }else{
                    mAdapter1?.notifyDataSetChanged()
                }
 //                if(!tempRemove1){
//                    tempRemove1=true
//                    viewBind?.sbYinyongRemove1?.visibility=View.VISIBLE
//                    viewBind?.rvYinyongbiaozhu1?.visibility=View.VISIBLE
//                }else if(!tempRemove2){
//                    tempRemove2=true
//                    viewBind?.sbYinyongRemove2?.visibility=View.VISIBLE
//                    viewBind?.rvYinyongbiaozhu2?.visibility=View.VISIBLE
//                }else if(!tempRemove3){
//                    tempRemove3=true
//                    viewBind?.sbYinyongRemove3?.visibility=View.VISIBLE
//                    viewBind?.rvYinyongbiaozhu3?.visibility=View.VISIBLE
//                }else if(!tempRemove4){
//                    tempRemove4=true
//                    viewBind?.sbYinyongRemove4?.visibility=View.VISIBLE
//                    viewBind?.rvYinyongbiaozhu4?.visibility=View.VISIBLE
//                }else if(!tempRemove5){
//                    tempRemove5=true
//                    viewBind?.sbYinyongRemove5?.visibility=View.VISIBLE
//                    viewBind?.rvYinyongbiaozhu5?.visibility=View.VISIBLE
//                }else if(!tempRemove6){
//                    tempRemove6=true
//                    viewBind?.sbYinyongRemove6?.visibility=View.VISIBLE
//                    viewBind?.rvYinyongbiaozhu6?.visibility=View.VISIBLE
//                }else if(!tempRemove7){
//                    tempRemove7=true
//                    viewBind?.sbYinyongRemove7?.visibility=View.VISIBLE
//                    viewBind?.rvYinyongbiaozhu7?.visibility=View.VISIBLE
//                }else if(!tempRemove8){
//                    tempRemove8=true
//                    viewBind?.sbYinyongRemove8?.visibility=View.VISIBLE
//                    viewBind?.rvYinyongbiaozhu8?.visibility=View.VISIBLE
//                }
//
//                else
//                if(mbean2.num==1){
//
//                    mbean2.num=2
//                    viewBind.addbean=mbean2
//                    viewBind?.sbYinyongRemove1?.visibility=View.VISIBLE
//                    viewBind?.sbYinyongRemove2?.visibility=View.VISIBLE
//                    viewBind?.rvYinyongbiaozhu2?.visibility=View.VISIBLE
//
//                }else if(mbean2.num==2){
//                    mbean2.num=3
//                    viewBind.addbean=mbean2
//                    viewBind?.sbYinyongRemove3?.visibility=View.VISIBLE
//                    viewBind?.rvYinyongbiaozhu3?.visibility=View.VISIBLE
//
//                }else if(mbean2.num==3){
//                    mbean2.num=4
//                    viewBind.addbean=mbean2
//                    viewBind?.sbYinyongRemove4?.visibility=View.VISIBLE
//                    viewBind?.rvYinyongbiaozhu4?.visibility=View.VISIBLE
//
//                }else if(mbean2.num==4){
//                    mbean2.num=5
//                    viewBind.addbean=mbean2
//                    viewBind?.sbYinyongRemove5?.visibility=View.VISIBLE
//                    viewBind?.rvYinyongbiaozhu5?.visibility=View.VISIBLE
//
//                }else if(mbean2.num==5){
//                    mbean2.num=6
//                    viewBind.addbean=mbean2
//                    viewBind?.sbYinyongRemove6?.visibility=View.VISIBLE
//                    viewBind?.rvYinyongbiaozhu6?.visibility=View.VISIBLE
//                }else if(mbean2.num==6){
//                    mbean2.num=7
//                    viewBind.addbean=mbean2
//                    viewBind?.sbYinyongRemove7?.visibility=View.VISIBLE
//                    viewBind?.rvYinyongbiaozhu7?.visibility=View.VISIBLE
//                }else if(mbean2.num==7){
//                    mbean2.num=8
//                    viewBind.addbean=mbean2
//                    viewBind?.sbYinyongRemove8?.visibility=View.VISIBLE
//                    viewBind?.rvYinyongbiaozhu8?.visibility=View.VISIBLE
//                }

            }
            viewBind.sbCaiyongAdd->{
                caiyongList?.add("")
                 if(caiyongList!=null&&caiyongList?.size!!>0){
                    mAdapter2?.notifyItemChanged(caiyongList?.size!!)

                }else{
                    mAdapter2?.notifyDataSetChanged()
                }
            }
            viewBind.ivClose ->{

            }
            viewBind.tvMyInfo -> {

            }
            viewBind.sbAdd -> {
                val dateFormat = SimpleDateFormat("yyyy/MM/dd  HH:mm:ss")
                val selectedDate = Calendar.getInstance() //系统当前时间

                val mdate = selectedDate.time
//                val BiaoNo //标准号
//                        : String? = null
//                val TypeName //分类名称
//                        : String? = null
//                val TiChuDepartmentName //提出单位
//                        : String? = null
//                val SourceId //标准来源
//                        : String? = null
//                val SourceName //标准来源名称
//                        : String? = null
//                val Pages //页数
//                        : String? = null
//                val SameClassNo //同一分类号标准
//                        : String? = null
//                val ReplaceNo //代替好
//                        : String? = null
//                val ByReplaceNo //被代替号
//                        : String? = null
//                val BzKeywords //关键字
//                        : String? = null
//                val CagegoryId //标准体系分类id
//                        : String? = null
//                val CagegoryName //标准体系分类id
//                        : String? = null
//                val SameTopicNo //同一专题标准
//                        : String? = null
                if (viewModel.keyId=="") {
                    if(viewBind.roundBiaoNo.text.toString().trim()==""){
                        ToastUtils.showShort("标准号不能为空")
                        return
                    }
                    var mlist=ToolApplication.singletonTest(requireContext()).getBiaozhunHao(viewBind.roundBiaoNo.text.toString().trim())
                    if (mlist!=null){
                        ToastUtils.showShort("标准号已存在")
                        return
                    }
                    setData()
                    ToolApplication.singletonTest(requireContext()).initStatandTable(
                        dateFormat.format(mdate),dateFormat.format(mdate),dateFormat.format(mdate),dateFormat.format(mdate),
                        viewBind?.etName?.text.toString(),
                        viewBind?.roundBiaozhuangtai?.text.toString(),
                        viewBind.roundShishishijian.text.toString(),
                        viewBind.roundStarttime.text.toString(),
                        viewBind.roundFenleihao.text.toString(),
                        "",
                        viewBind.roundPizhundanwei.text.toString(),
                        "",
                        viewBind.roundShishidanwei.text.toString(),
                        "",
                        viewBind.roundZhubiandanwei.text.toString(),
                        viewBind.roundQicaoren.text.toString(),
                        Referencesd,
                        Standards,
                        mpath,
                        viewBind.etContent.text.toString(),
                        "收藏",
                        ""+viewBind.roundBiaoNo.text.toString(),
                        ""+viewBind.tvTiChuDepartmentName.text.toString(),
                        ""+viewBind.roundSourceName.text.toString(),
                        ""+viewBind.roundPages.text.toString(),
                        ""+viewBind.roundSameClassNo.text.toString(),
                        ""+viewBind.roundReplaceNo.text.toString(),
                        ""+viewBind.roundByReplaceNo.text.toString(),
                        ""+viewBind.roundBzKeywords.text.toString(),
                        ""+viewBind.roundCagegoryName.text.toString(),
                        ""+viewBind.roundSameTopicNo.text.toString(),
                        )
                    ToastUtils.showShort("提交成功")
                    act?.finish()
                }else{
                    if(viewBind.roundBiaoNo.text.toString().trim()==""){
                        ToastUtils.showShort("标准号不能为空")
                        return
                    }

                    setData()

                    ToolApplication.singletonTest(requireContext()).updateStatandData(
                        viewBind?.etName?.text.toString(),
                        viewBind?.roundBiaozhuangtai?.text.toString(),
                        viewBind.roundShishishijian.text.toString(),
                        viewBind.roundStarttime.text.toString(),
                        viewBind.roundFenleihao.text.toString(),
                        "",
                        viewBind.roundPizhundanwei.text.toString(),
                        "",
                        viewBind.roundShishidanwei.text.toString(),
                        "",
                        viewBind.roundZhubiandanwei.text.toString(),
                        viewBind.roundQicaoren.text.toString(),
                        ""+Referencesd,
                        ""+Standards,
                        mpath,
                        viewBind.etContent.text.toString(),
                        "收藏",
                        ""+viewBind.roundBiaoNo.text.toString(),
                        ""+viewBind.tvTiChuDepartmentName.text.toString(),
                        ""+viewBind.roundSourceName.text.toString(),
                        ""+viewBind.roundPages.text.toString(),
                        ""+viewBind.roundSameClassNo.text.toString(),
                        ""+viewBind.roundReplaceNo.text.toString(),
                        ""+viewBind.roundByReplaceNo.text.toString(),
                        ""+viewBind.roundBzKeywords.text.toString(),
                        ""+viewBind.roundCagegoryName.text.toString(),
                        ""+viewBind.roundSameTopicNo.text.toString(),
                        viewModel.keyId?.toInt()
                    )
                    ToastUtils.showShort("修改成功")
                    act?.finish()
                }
            }
//            viewBind?.roundFenleihao->{
//                DownPop(context, enums12 = dataTypeNum, checkedTextView = viewBind.roundFenleihao as AppCompatCheckedTextView, isSingleChecked = true) { k, v, p ->
//
//                    viewBind?.roundFenleihao?.text = v
//                }.showPopupWindow(viewBind.roundFenleihao)
//            }
//            viewBind?.roundPizhundanwei->{
//                DownPop(context, enums12 = dataRatifyDepartmentName, checkedTextView = viewBind.roundPizhundanwei as AppCompatCheckedTextView, isSingleChecked = true) { k, v, p ->
//
//                    viewBind?.roundPizhundanwei?.text = v
//                }.showPopupWindow(viewBind.roundPizhundanwei)
//            }
//            viewBind?.roundQicaoren->{
//                DownPop(context, enums12 = dataDrafter, checkedTextView = viewBind.roundQicaoren as AppCompatCheckedTextView, isSingleChecked = true) { k, v, p ->
//
//                    viewBind?.roundQicaoren?.text = v
//                }.showPopupWindow(viewBind.roundQicaoren)
//            }
//
//            viewBind?.roundZhubiandanwei->{
//                DownPop(context, enums12 = dataEditorDepartmentName, checkedTextView = viewBind.roundZhubiandanwei as AppCompatCheckedTextView, isSingleChecked = true) { k, v, p ->
//
//                    viewBind?.roundZhubiandanwei?.text = v
//                }.showPopupWindow(viewBind.roundZhubiandanwei)
//            }
            viewBind?.roundSourceName->{
                DownPop(context, enums12 = mulist2, checkedTextView = viewBind.roundSourceName as AppCompatCheckedTextView, isSingleChecked = true) { k, v, p ->

                    viewBind?.roundSourceName?.text = v
                }.showPopupWindow(viewBind.roundSourceName)
            }
            viewBind?.roundShishishijian->{
                val pvTime: TimePickerView = TimePickerBuilder(context) { date, _ ->
                    var startTime =
                        TimeUtils.date2String(date, SimpleDateFormat("yyyy-MM-dd", Locale.CHINA))
                    viewBind.roundShishishijian.text =startTime
                }.isDialog(true).build()
                pvTime.dialog.window?.setGravity(Gravity.BOTTOM)
                pvTime.dialog.window?.setWindowAnimations(R.style.picker_view_slide_anim)
                pvTime.setOnDismissListener { viewBind.roundShishishijian.isChecked = false }
                viewBind.roundShishishijian.isChecked = true
                pvTime.show()
            }
            viewBind?.roundStarttime->{
                val pvTime: TimePickerView = TimePickerBuilder(context) { date, _ ->
                    var startTime =
                        TimeUtils.date2String(date, SimpleDateFormat("yyyy-MM-dd", Locale.CHINA))
                    viewBind.roundStarttime.text =startTime
                }.isDialog(true).build()
                pvTime.dialog.window?.setGravity(Gravity.BOTTOM)
                pvTime.dialog.window?.setWindowAnimations(R.style.picker_view_slide_anim)
                pvTime.setOnDismissListener { viewBind.roundStarttime.isChecked = false }
                viewBind.roundStarttime.isChecked = true
                pvTime.show()
            }
            viewBind?.roundBiaozhuangtai->{
                DownPop(context, enums12 = dataList, checkedTextView = viewBind?.roundBiaozhuangtai as AppCompatCheckedTextView, isSingleChecked = true) { k, v, p ->
                    (viewBind?.roundBiaozhuangtai as AppCompatCheckedTextView)?.text=v
                }.showPopupWindow(viewBind?.roundBiaozhuangtai)
            }
            viewBind?.sbShangchuan->{


                try {
                    // 普通 方式
                    getZFileHelp().result(this) {
                        if(this!=null&&this.size>0) {
                            var fiel = File(beand?.filePath)
                            var FILENAME = fiel.name
//                            var  appDir = File(
//                                context?.applicationContext?.getExternalFilesDir(null)
//                                    .toString() + File.separator + "download"
//                            )

                            var appDir = File(
                                Environment.getExternalStorageDirectory()
                                    .toString() + File.separator + "wenxian"
                            )
                            if (!appDir.exists()) {
                                appDir.mkdirs()
                            }
                            if (!fiel.exists()) {
                                ToastUtils.showShort("文件不存在")

                                return@result
                            }
                            var m= IOUtils.copy(  FileInputStream(beand?.filePath),
                                FileOutputStream(context?.applicationContext?.getExternalFilesDir(null)
                                    .toString() + File.separator + "wenxian/"+FILENAME));
                            if(m!=-1){
                                ToastUtils.showShort("上传成功")
                            }else{
                                ToastUtils.showShort("上传失败")

                            }

                            mpath = Environment.getExternalStorageDirectory()
                                .toString() + File.separator + "wenxian"   + FILENAME
                            Log.e("dddddd", "dd" + mpath)

                            viewBind?.tvWenjianName?.setText(FILENAME)
                            viewBind?.tvWenjianName?.visibility=View.VISIBLE
                             beand?.filePath=mpath

                        }
                    }
                } catch (e: IOException) {
                    // TODO Auto-generated catch block
                    e.printStackTrace()
                }
            }
            else -> {

            }
        }


    }

    private fun setData() {
        var sbyinyong = StringBuffer()
        var sbciayong = StringBuffer()

        yinyongList?.forEachIndexed { index, s ->
            if(s!="") {
                sbyinyong?.append(yinyongList?.get(index) + ",")
            }
            Log.e("引用","引用"+s)
        }
        caiyongList?.forEachIndexed { index, s ->
            if(s!=""){
                sbciayong?.append(caiyongList?.get(index)+",")
            }
        }
//        if (viewBind.tvYinyongbiaozhu1?.text.toString() != "") {
//            sbyinyong.append(viewBind.tvYinyongbiaozhu1?.text.toString() + "|")
//        } else if (viewBind.tvYinyongbiaozhu2?.text.toString() != "") {
//            sbyinyong.append(viewBind.tvYinyongbiaozhu2?.text.toString() + "|")
//        } else if (viewBind.tvYinyongbiaozhu3?.text.toString() != "") {
//            sbyinyong.append(viewBind.tvYinyongbiaozhu3?.text.toString() + "|")
//        } else if (viewBind.tvYinyongbiaozhu4?.text.toString() != "") {
//            sbyinyong.append(viewBind.tvYinyongbiaozhu4?.text.toString() + "|")
//        } else if (viewBind.tvYinyongbiaozhu5?.text.toString() != "") {
//            sbyinyong.append(viewBind.tvYinyongbiaozhu5?.text.toString() + "|")
//        } else if (viewBind.tvYinyongbiaozhu6?.text.toString() != "") {
//            sbyinyong.append(viewBind.tvYinyongbiaozhu6?.text.toString() + "|")
//        } else if (viewBind.tvYinyongbiaozhu7?.text.toString() != "") {
//            sbyinyong.append(viewBind.tvYinyongbiaozhu7?.text.toString() + "|")
//        } else if (viewBind.tvYinyongbiaozhu8?.text.toString() != "") {
//            sbyinyong.append(viewBind.tvYinyongbiaozhu8?.text.toString() + "|")
//        }
//        var sbCaiyong = StringBuffer()
//        if (viewBind.tvCaiyongbiaozhu1?.text.toString() != "") {
//            sbCaiyong.append(viewBind.tvYinyongbiaozhu1?.text.toString() + "|")
//        } else if (viewBind.tvCaiyongbiaozhu2?.text.toString() != "") {
//            sbCaiyong.append(viewBind.tvCaiyongbiaozhu2?.text.toString() + "|")
//        } else if (viewBind.tvCaiyongbiaozhu3?.text.toString() != "") {
//            sbCaiyong.append(viewBind.tvCaiyongbiaozhu3?.text.toString() + "|")
//        } else if (viewBind.tvCaiyongbiaozhu4?.text.toString() != "") {
//            sbCaiyong.append(viewBind.tvCaiyongbiaozhu4?.text.toString() + "|")
//        } else if (viewBind.tvCaiyongbiaozhu5?.text.toString() != "") {
//            sbCaiyong.append(viewBind.tvCaiyongbiaozhu5?.text.toString() + "|")
//        } else if (viewBind.tvCaiyongbiaozhu6?.text.toString() != "") {
//            sbCaiyong.append(viewBind.tvCaiyongbiaozhu6?.text.toString() + "|")
//        } else if (viewBind.tvCaiyongbiaozhu7?.text.toString() != "") {
//            sbCaiyong.append(viewBind.tvCaiyongbiaozhu7?.text.toString() + "|")
//        } else if (viewBind.tvCaiyongbiaozhu8?.text.toString() != "") {
//            sbCaiyong.append(viewBind.tvCaiyongbiaozhu8?.text.toString() + "|")
//        }
        Referencesd = sbyinyong.toString()
        Standards = sbciayong.toString()
        Log.e("fanhui数据","****"+Referencesd)
    }

    class MyFileImageListener : ZFileImageListener() {

        override fun loadImage(imageView: ImageView, file: File) {
            // 以Glide为例
            Glide.with(imageView.context)
                .load(file)
                .apply(RequestOptions().apply {
                    placeholder(R.drawable.ic_zfile_other)
                    error(R.drawable.ic_zfile_other)
                })
                .into(imageView)
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
            ToastUtils.showShort("上传成功")
            fosfrom.close()
            fosto.close()
        } catch (ex: Exception) {
            Log.e("readfile", ex.message!!)
        }
    }
    /**
    返回后刷新数据，
     */
    @Subscribe(thread = EventThread.MAIN_THREAD, tags = [Tag(Constants.BusAction.Bus_Refresh_List)])
    fun backRefresh(str: String) {
     }
    /**
    返回后刷新数据，
     */
    @Subscribe(thread = EventThread.MAIN_THREAD, tags = [Tag("delyin")])
    fun delyin(str: String) {
        Log.e("dddd111","eeee"+str)
        yinyongList?.removeAt(str.toInt())
        mAdapter1?.notifyItemChanged(str.toInt())
    }
    /**
    返回后刷新数据，
     */
    @Subscribe(thread = EventThread.MAIN_THREAD, tags = [Tag("delcai")])
    fun delcai(str: String) {
        Log.e("dddd111","eeee"+str)
        caiyongList?.removeAt(str.toInt())
        mAdapter2?.notifyItemChanged(str.toInt())
    }
}