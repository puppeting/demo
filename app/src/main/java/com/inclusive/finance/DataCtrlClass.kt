package com.inclusive.finance

import android.content.Context
import android.os.Environment
import android.provider.Settings
import android.util.Log
import android.view.View
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.FileUtils
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.inclusive.finance.app.MyApplication
import com.inclusive.finance.base.BaseActivity
import com.inclusive.finance.bean.*
import com.inclusive.finance.bean.model.ApplyModel
import com.inclusive.finance.config.Constants
import com.inclusive.finance.config.Urls
import com.inclusive.finance.pop.ConfirmPop
import com.inclusive.finance.utils.SZWUtils
import com.inclusive.finance.utils.auth.RSAUtils
import com.inclusive.finance.utils.net.NetEntity
import com.inclusive.finance.utils.net.callback.DialogCallback
import com.inclusive.finance.utils.net.callback.JsonCallback
import com.inclusive.finance.widget.CustomProgress
import com.lzy.okgo.OkGo
import com.lzy.okgo.callback.FileCallback
import com.lzy.okgo.model.Response
import java.io.File
import java.util.*
import kotlin.collections.ArrayList


object DataCtrlClass {
    /**
     * 下载pdf文件
     * */
    fun downloadPDF(context: Context, webUrl: String, listener: (it: String?) -> Unit) {
        CustomProgress.show(context, "加载中", false, null)
        lateinit var appDir: File
        appDir = File(
            context?.applicationContext?.getExternalFilesDir(null)
                .toString() + File.separator + "wenxian"
        )
        if (!appDir.exists()) {
            appDir.mkdirs()
        }
        val appName: String = webUrl.substring(webUrl.lastIndexOf("/") + 1, webUrl.length)
        context.let {
            OkGo.get<File>(webUrl).tag(this)
                .execute(object : FileCallback(appDir.absolutePath, appName) {
                    override fun onSuccess(response: Response<File>?) {
                        CustomProgress.disMiss()
                        listener.invoke(response?.body()?.absolutePath)
                    }

                    override fun onError(response: Response<File>?) {

                        CustomProgress.disMiss()
                    }

                })
        }
    }
    /**
     * 下载pdf文件
     * */
    fun downloadPDF2(context: Context, webUrl: String, listener: (it: String?) -> Unit) {
        CustomProgress.show(context, "加载中", false, null)
        lateinit var appDir: File
        appDir = File(
            context?.applicationContext?.getExternalFilesDir(null)
                .toString() + File.separator + "download"
        )
        if (!appDir.exists()) {
            appDir.mkdirs()
        }
        val appName: String = webUrl.substring(webUrl.lastIndexOf("/") + 1, webUrl.length)
        context.let {
            OkGo.get<File>(webUrl).tag(this)
                .execute(object : FileCallback(appDir.absolutePath, appName) {
                    override fun onSuccess(response: Response<File>?) {
                        CustomProgress.disMiss()
                        listener.invoke(response?.body()?.absolutePath)
                    }

                    override fun onError(response: Response<File>?) {

                        CustomProgress.disMiss()
                    }

                })
        }
    }
    /**
     * 获取标准文件同步
     * @param[phone] string
     * */
    fun getSyncPageList(
        context: Context?,
         pageNum: Int,
        listener: (errorMsg: ArrayList<TongBuStandard>?) -> Unit
    ) {
        val params = HashMap<String, String>()
        params["page"] = pageNum.toString() + ""
        params["pageSize"] = "200"
        params["auth"] = "smartSearch2022"
        context?.let {
            OkGo.get<NetEntity<ArrayList<TongBuStandard>>>(Urls.SyncPageList).params(params).tag(this)
                .execute(object : JsonCallback<NetEntity<ArrayList<TongBuStandard>>>() {
                    override fun onSuccess(response: Response<NetEntity<ArrayList<TongBuStandard>>>) {
                        if (response.body().statusCode == Constants.NetCode.SUCCESS) {
                            listener.invoke(response.body().data)
                         } else {
                            listener.invoke(null)
                         }
                    }

                    override fun onError(response: Response<NetEntity<ArrayList<TongBuStandard>>>) {
                        super.onError(response)
                        listener.invoke(null)
                    }

                })
        }
    }














}