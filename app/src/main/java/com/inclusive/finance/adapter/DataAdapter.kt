package com.inclusive.finance.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.arouter.launcher.ARouter
import com.blankj.utilcode.util.SPUtils
import com.blankj.utilcode.util.ToastUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.flyco.roundview.RoundLinearLayout
import com.hwangjr.rxbus.RxBus
import com.inclusive.finance.DataCtrlClass
import com.inclusive.finance.R
import com.inclusive.finance.app.ToolApplication
import com.inclusive.finance.config.Constants
import com.inclusive.finance.databinding.SearchItemLayoutBinding
import com.inclusive.finance.room.Standard
import com.inclusive.finance.widget.MyWebActivity
import org.apache.commons.io.FileUtils
import org.apache.commons.io.IOUtils
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException


class DataAdapter<T: Standard> : BaseQuickAdapter<T, BaseViewHolder>(R.layout.search_item_layout, ArrayList())  {
    val DM_TARGET_FOLDER = File.separator + "download"   //下载目标文件夹


    override fun onCreateDefViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return BaseViewHolder(SearchItemLayoutBinding.inflate(LayoutInflater.from(context), parent, false).root)
    }
    override fun convert(helper: BaseViewHolder, item: T) {
        DataBindingUtil.getBinding<SearchItemLayoutBinding>(helper.itemView)?.apply {
            data=item
            setHelper(helper)
        }


        if(item.CollectType=="1") {
            helper.setText(R.id.tv_shoucang, "已收藏")
        }else{
            helper.setText(R.id.tv_shoucang, "收藏")
        }
        var mlist=ArrayList<String>()
        var mlist2=ArrayList<String>()

        var mAdapter: CaiYongDataAdapter<String>? = null
        var mAdapter2: YinYongDataAdapter<String>? = null

        if(item.referencesd!=""){
            var mm=item.referencesd.split(",")
            mm.forEach {
                if(it!=""){
                    Log.e("引用","****"+it)
                    mlist?.add(it)
                }
            }
        }
        if(item.standards!=""){
            var mm=item.standards.split(",")
            mm.forEach {
                if(it!=""){
                    Log.e("采用","****"+it)
                    mlist2?.add(it)
                }
            }
        }
        var recyCaiYong=helper.itemView.findViewById<RecyclerView>(R.id.recy_caiyong)
        mAdapter = CaiYongDataAdapter()
         recyCaiYong.layoutManager = LinearLayoutManager(context)
        var mlayout2=LinearLayoutManager(context)
        mlayout2.orientation=LinearLayoutManager.HORIZONTAL
        recyCaiYong.layoutManager = mlayout2

        recyCaiYong?.adapter = mAdapter
        mAdapter?.setNewInstance(mlist)
        var recyYinYong=helper.itemView.findViewById<RecyclerView>(R.id.recy_yinyong)
        mAdapter2 = YinYongDataAdapter()
        var mlayout=LinearLayoutManager(context)
        mlayout.orientation=LinearLayoutManager.HORIZONTAL
        recyYinYong.layoutManager = mlayout
        recyYinYong?.adapter = mAdapter2
        mAdapter2?.setNewInstance(mlist2)
        mAdapter?.setOnItemClickListener { adapter, view, position ->
            RxBus.get().post("searchcaiyong", ""+mAdapter?.data?.get(position))

        }
        mAdapter2?.setOnItemClickListener { adapter, view, position ->
            RxBus.get().post("searchyinyong", ""+mAdapter2?.data?.get(position))

        }
        helper.itemView.findViewById<RoundLinearLayout>(R.id.bt_shoucang).setOnClickListener {
            if(item.CollectType=="1"){
                var listBumen = ToolApplication.singletonTest(context)
                    .deleteCollect(SPUtils.getInstance().getInt(
                        Constants.SPUtilsConfig.SP_PHONE), item.id)
                if (listBumen == 0) {
                    ToolApplication.singletonTest(context)
                        .changeCollect("0", item.id)
                    helper.setText(R.id.tv_shoucang, "收藏")
                    RxBus.get().post(Constants.BusAction.Bus_Refresh_List, Constants.BusAction.Bus_Refresh_List)
                }
            }else {
                var listBumen = ToolApplication.singletonTest(context)
                    .intserCollect(SPUtils.getInstance().getInt(
                        Constants.SPUtilsConfig.SP_PHONE), item.id)
                if (listBumen == 0) {
                    helper.setText(R.id.tv_shoucang, "已收藏")
                    if(item.CollectType=="1"){
                        ToolApplication.singletonTest(context)
                            .changeCollect("0", item.id)
                        RxBus.get().post(Constants.BusAction.Bus_Refresh_List, Constants.BusAction.Bus_Refresh_List)
                    }else {
                        ToolApplication.singletonTest(context)
                            .changeCollect("1", item.id)
                        RxBus.get().post(Constants.BusAction.Bus_Refresh_List, Constants.BusAction.Bus_Refresh_List)
                    }
                }
            }
        }
        helper.itemView.findViewById<RoundLinearLayout>(R.id.bt_yulan).setOnClickListener {
            ARouter.getInstance()
                .build("/com/MyWebActivity") //                        .withString(Intent_WebUrl, "http://192.168.3.32:8081/onlinePreview?url=http%3A%2F%2F212.129.130.163%3A3000%2Ftscepdf.pdf&officePreviewType=pdf")
                //                        .withString(Intent_WebUrl, "http://debugtbs.qq.com")
                .withString(
                    MyWebActivity.Intent_WebUrl,
                    item?.filePath
                )
                .withBoolean("isPDF", true)
                .withString(MyWebActivity.Intent_WebTitle, "PDF查看").navigation()
        }
        helper.itemView.findViewById<RoundLinearLayout>(R.id.bt_download).setOnClickListener {
            if(item.filePath.contains("http")){
                DataCtrlClass.downloadPDF2(context, item.filePath) {

                    ToastUtils.showShort("下载成功")

                }
            }else {
                var fiel = File(item?.filePath)
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
               var m= IOUtils.copy(  FileInputStream(item?.filePath),
                      FileOutputStream(context?.applicationContext?.getExternalFilesDir(null)
                          .toString() + File.separator + "download/"+FILENAME));
            if(m!=-1){
                ToastUtils.showShort("下载成功")
            }else{
                ToastUtils.showShort("下载失败")

            }
            }
        }
    }



}