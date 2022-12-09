package com.inclusive.finance.ui

import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.ToastUtils
import com.hwangjr.rxbus.annotation.Subscribe
import com.hwangjr.rxbus.annotation.Tag
import com.hwangjr.rxbus.thread.EventThread
import com.inclusive.finance.DataCtrlClass
import com.inclusive.finance.R
import com.inclusive.finance.adapter.CaiYongDataAdapter
import com.inclusive.finance.adapter.CustomExpandableListAdapter
import com.inclusive.finance.adapter.DataAdapter
import com.inclusive.finance.adapter.YinYongDataAdapter
import com.inclusive.finance.app.ToolApplication
import com.inclusive.finance.base.MyBaseFragment
import com.inclusive.finance.bean.BaseTypeBean

import com.inclusive.finance.bean.model.ApplyModel
import com.inclusive.finance.config.Constants
import com.inclusive.finance.config.Urls
import com.inclusive.finance.databinding.FragmentFielinfoBinding
import com.inclusive.finance.pop.*
import com.inclusive.finance.room.Standard
import com.inclusive.finance.utils.StatusBarUtil
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener
import org.apache.commons.io.IOUtils
import org.jetbrains.anko.support.v4.act
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.LinkedHashMap


/**
 * 详情页
 * */
class FielInfoFragment : MyBaseFragment(), View.OnClickListener, OnRefreshLoadMoreListener {
    private var listRatifyDepartmentName: MutableList<Standard>? = arrayListOf()
    private var beand: Standard? = null
    private   var biaozhun: String = ""
    private   var pizhun: String = ""

    private   var tichu: String = ""

    private var adapter2: CustomExpandableListAdapter? = null
    private var mAdapter: DataAdapter<Standard>? = null
    var titleList: ArrayList<String>? = arrayListOf( )
    lateinit var viewModel: ApplyModel
    lateinit var viewBind: FragmentFielinfoBinding
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
        viewBind = FragmentFielinfoBinding.inflate(inflater, container, false).apply {
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
        StatusBarUtil.setPaddingSmart(act, viewBind.actionBarCustom.appBar)
        mAdapter = DataAdapter()
        mAdapter?.setEmptyView(LayoutInflater.from(context).inflate(R.layout.empty_view, null))
        mAdapter?.setNewInstance(Standard)

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
        viewBind?.btDownload?.setOnClickListener {
            Log.e("返回数据","****"+beand?.name)
            if(beand?.filePath?.contains("http") == true){
                DataCtrlClass.downloadPDF2(requireContext(), beand?.filePath.toString()) {

                    ToastUtils.showShort("下载成功")

                }
            }else {
                var fiel = File(beand?.filePath)
                var FILENAME = fiel.name
                var  appDir = File(
                    context?.applicationContext?.getExternalFilesDir(null)
                        .toString() + File.separator + "download"
                )
                if (!appDir.exists()) {
                    appDir.mkdirs()
                }
                 if (!fiel.exists()) {
                     ToastUtils.showShort("文件不存在")

                     return@setOnClickListener
                }
                var m= IOUtils.copy(  FileInputStream(beand?.filePath),
                    FileOutputStream(context?.applicationContext?.getExternalFilesDir(null)
                        .toString() + File.separator + "download/"+FILENAME));
                if(m!=-1){
                    ToastUtils.showShort("下载成功")
                }else{
                    ToastUtils.showShort("下载失败")

                }
            }

//            var fiel=File(beand?.filePath)
//            var FILENAME=fiel.name
//            var appDir = File(
//                Environment.getExternalStorageDirectory()
//                    .toString() + File.separator + "download"+  FILENAME
//            )
//            if (!appDir.exists()) {
//                appDir.mkdirs()
//            }
//
//            copyfile(fiel,appDir,true)
        }

         listRatifyDepartmentName = ToolApplication.singletonTest(requireContext()).getInfo(viewModel.keyId)

        if(listRatifyDepartmentName!=null&&listRatifyDepartmentName?.size!!>0) {
            beand = Standard()
            beand?.releaseTime = "发布日期：" + listRatifyDepartmentName?.get(0)?.releaseTime
            beand?.startTime = "实施日期：" + listRatifyDepartmentName?.get(0)?.startTime
            beand?.typeNum = "分类号：" + listRatifyDepartmentName?.get(0)?.typeNum
            beand?.RatifyDepartmentName =
                "批准单位：" + listRatifyDepartmentName?.get(0)?.RatifyDepartmentName
            beand?.StartDepartmentName =
                "提出单位：" + listRatifyDepartmentName?.get(0)?.StartDepartmentName
            beand?.EditorDepartmentName =
                "主编单位：" + listRatifyDepartmentName?.get(0)?.EditorDepartmentName
            beand?.drafter = "起草人：" + listRatifyDepartmentName?.get(0)?.drafter
            beand?.name = "" + listRatifyDepartmentName?.get(0)?.Name
            beand?.filePath = "" + listRatifyDepartmentName?.get(0)?.filePath
            beand?.Remark = "适用范围：" + listRatifyDepartmentName?.get(0)?.Remark
            beand?.biaoNo = "标准号：" + listRatifyDepartmentName?.get(0)?.biaoNo
            beand?.sourceName = "标准来源：" + listRatifyDepartmentName?.get(0)?.sourceName
            beand?.sameClassNo = "同一分类号标准：" + listRatifyDepartmentName?.get(0)?.sameClassNo
            beand?.tiChuDepartmentName = "提出单位：" + listRatifyDepartmentName?.get(0)?.tiChuDepartmentName
            beand?.pages = "页数：" + listRatifyDepartmentName?.get(0)?.pages
            beand?.replaceNo = "替代号：" + listRatifyDepartmentName?.get(0)?.replaceNo
            beand?.byReplaceNo = "被替代号：" + listRatifyDepartmentName?.get(0)?.byReplaceNo
            beand?.bzKeywords = "关键词：" + listRatifyDepartmentName?.get(0)?.bzKeywords
            beand?.SameTopicNo = "同一专题标准：" + listRatifyDepartmentName?.get(0)?.SameTopicNo
            beand?.name =   listRatifyDepartmentName?.get(0)?.name
            beand?.status =   listRatifyDepartmentName?.get(0)?.status
            beand?.cagegoryName =   listRatifyDepartmentName?.get(0)?.CagegoryName
            beand?.bzKeywords =   listRatifyDepartmentName?.get(0)?.bzKeywords



//        beand.releaseTime="发布日期："+listRatifyDepartmentName?.get(0)?.releaseTime

            viewBind?.datanum = beand
        }
        initDownView()
    }
    var mAdapter1: CaiYongDataAdapter<String>? = CaiYongDataAdapter()
    var mAdapter2: YinYongDataAdapter<String>? = YinYongDataAdapter()
    private fun initDownView() {
        var mlist= java.util.ArrayList<String>()
        var mlist2= java.util.ArrayList<String>()



        if(listRatifyDepartmentName?.get(0)?.referencesd!=""){
            var mm=listRatifyDepartmentName?.get(0)?.referencesd?.split(",")
            mm?.forEach {
                if(it!=""){
                    Log.e("引用","****"+it)
                    mlist?.add(it)
                }
            }
        }
        if(listRatifyDepartmentName?.get(0)?.standards!=""){
            var mm=listRatifyDepartmentName?.get(0)?.standards?.split(",")
            mm?.forEach {
                if(it!=""){
                    Log.e("采用","****"+it)
                    mlist2?.add(it)
                }
            }
        }

        mAdapter1 = CaiYongDataAdapter()
        viewBind?.recyCaiyong.layoutManager = LinearLayoutManager(context)
        var mlayout2=LinearLayoutManager(context)
        mlayout2.orientation=LinearLayoutManager.HORIZONTAL
        viewBind?.recyCaiyong.layoutManager = mlayout2
        viewBind?.recyCaiyong?.adapter = mAdapter1
        mAdapter1?.setNewInstance(mlist)
          mAdapter2 = YinYongDataAdapter()
        var mlayout=LinearLayoutManager(context)
        mlayout.orientation=LinearLayoutManager.HORIZONTAL
        viewBind?.recyYinyong?.layoutManager = mlayout
        viewBind?.recyYinyong?.adapter = mAdapter2
        mAdapter2?.setNewInstance(mlist2)

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
            ToastUtils.showShort("下载成功")
            fosfrom.close()
            fosto.close()
        } catch (ex: Exception) {
            Log.e("readfile", ex.message!!)
        }
    }

    override fun onClick(v: View?) {


//        if (v == viewBind.btDownload) {
//
//                mLastClickTime = System.currentTimeMillis()
//
//
//        }



    }

    /**
    返回后刷新数据，
     */
    @Subscribe(thread = EventThread.MAIN_THREAD, tags = [Tag(Constants.BusAction.Bus_Refresh_List)])
    fun backRefresh(str: String) {
     }


}