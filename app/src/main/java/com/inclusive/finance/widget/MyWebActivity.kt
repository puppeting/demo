package com.inclusive.finance.widget

import android.Manifest
import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.text.TextUtils
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.webkit.ValueCallback
import android.widget.RelativeLayout
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.blankj.utilcode.util.*
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener
import com.github.barteksc.pdfviewer.listener.OnPageErrorListener
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.hwangjr.rxbus.RxBus
import com.inclusive.finance.DataCtrlClass
import com.inclusive.finance.R
import com.inclusive.finance.base.BaseActivity
import com.inclusive.finance.base.permissionWAndRWithPermissionCheck
import com.inclusive.finance.databinding.ActivityMyWebBinding
import com.inclusive.finance.utils.SZWUtils
import com.inclusive.finance.utils.StatusBarUtil
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.config.PictureConfig.CHOOSE_REQUEST
import com.luck.picture.lib.config.PictureMimeType

import permissions.dispatcher.NeedsPermission
import permissions.dispatcher.RuntimePermissions
import java.io.File


@RuntimePermissions
@Route(path = "/com/MyWebActivity")
open class MyWebActivity : BaseActivity(), OnPageChangeListener, OnPageErrorListener,
    OnLoadCompleteListener {
    @Autowired
    @JvmField
    var webUrl = ""

    @Autowired
    @JvmField
    var webTitle = ""


    @Autowired
    @JvmField
    var screen_orientation = ""

    @Autowired
    @JvmField
    var isPDF = false

    @Autowired
    @JvmField
    var type = 0 //0正常，1 PDF, 2 图片 ,3 完整html,4 本地pdf
    private var isAnimStart = false
    private var currentProgress: Int = 0
    private var mUploadMessage: ValueCallback<Uri>? = null

    private var mUploadMessageForAndroid5: ValueCallback<Array<Uri>>? = null


    override fun initToolbar() {
        if (screen_orientation == "portrait") {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }
        viewBind?.actionBarCustom?.btShoucang?.visibility=View.GONE
        viewBind?.actionBarCustom?.btSearch?.visibility=View.GONE
        viewBind?.actionBarCustom?.btFiel?.visibility=View.GONE
        viewBind?.actionBarCustom?.btSystem?.visibility=View.GONE
        StatusBarUtil.darkMode(this)
        StatusBarUtil.setPaddingSmart(this, viewBind.actionBarCustom.toolbar)
         viewBind.actionBarCustom.toolbar.title = webTitle
        viewBind.actionBarCustom.toolbar.setNavigationOnClickListener {
                finish()
        }
    }

    //
    //    override fun setInflateId(): Int {
    //        return R.layout.activity_my_web
    //    }
    lateinit var viewBind: ActivityMyWebBinding
    override fun setInflateBinding() {
        viewBind = DataBindingUtil.setContentView<ActivityMyWebBinding>(this, R.layout.activity_my_web)
            .apply {
                lifecycleOwner = this@MyWebActivity
            }

    }



    override fun onDestroy() {

        super.onDestroy()
    }

    @NeedsPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
    fun permissionWAndRAndS(listener: Runnable) {
        listener.run()
    }

//    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        onRequestPermissionsResult(requestCode, grantResults)
//    }


    override fun init() {
        if (isPDF){
            if (type==0) {
                type=1
            }
        }

        permissionWAndRAndSWithPermissionCheck() {
            val pdfFile = File(webUrl)
            displayFromFile(pdfFile)
                    DataCtrlClass.downloadPDF(this, webUrl) {

                         viewBind.pdfView.visibility=View.VISIBLE
                        val pdfFile = File(it)
                        displayFromFile(pdfFile)

                    }



//                viewBind.mWebView.loadUrl("file:///android_asset/previewIndex.html?http://www.cztouch.com/upfiles/soft/testpdf.pdf")
//                viewBind.mWebView?.visibility=View.GONE
//                viewBind.pdfView?.visibility=View.VISIBLE
//                viewBind.pdfViewPager.fromFile(File("http://www.cztouch.com/upfiles/soft/testpdf.pdf")).enableSwipe(true) // allows to block changing pages using swipe
//                    .swipeHorizontal(false)
//                    .enableDoubletap(true)
//                    .defaultPage(0).load()


        }

    }
    private fun displayFromFile(file: File) {
        try {
            viewBind.pdfView.fromFile(file) //设置pdf文件地址
                .defaultPage(0)
                .onPageChange(this)
                .enableSwipe(true)
                .enableAnnotationRendering(true)
                .onLoad(this)
//                .scrollHandle(DefaultScrollHandle(this))
                .spacing(10) // in dp
                .onPageError(this)
                .load()
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }
    private fun getQueryStr(url: String, str: String): String {
        val matches = RegexUtils.getMatches("(^|)$str=([^&]*)(&|$)", url)
        return try {
            if (matches.size > 0) {
                EncodeUtils.urlDecode(matches[0].split("=")[1].replace("&", ""))
            } else ""
        } catch (e: Exception) {
            "地址选取错误。请重试"
        }
    }


    private fun startProgressAnimation(newProgress: Int) {
        val animator = ObjectAnimator.ofInt(this.viewBind.progressBar, "progress", this.currentProgress, newProgress)
        animator.duration = 300L
        animator.interpolator = DecelerateInterpolator()
        animator.start()
    }

    private fun startDismissAnimation(progress: Int) {
        val anim = ObjectAnimator.ofFloat(this.viewBind.progressBar, "alpha", 1.0f, 0.0f)
        anim.duration = 1500L
        anim.interpolator = DecelerateInterpolator()
        anim.addUpdateListener { valueAnimator ->
            val fraction = valueAnimator.animatedFraction
            val offset = 100 - progress
            this@MyWebActivity.viewBind.progressBar.progress = (progress.toFloat() + offset.toFloat() * fraction).toInt()
        }
        anim.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                this@MyWebActivity.viewBind.progressBar.progress = 0
                this@MyWebActivity.viewBind.progressBar.visibility = View.GONE
                this@MyWebActivity.isAnimStart = false
            }
        })
        anim.start()
    }


    override fun onBackPressed() {

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            if (requestCode == CHOOSE_REQUEST) {
                // 图片、视频、音频选择结果回调
                val selectList = PictureSelector.obtainMultipleResult(data)
                // 例如 LocalMedia 里面返回三种path
                // 1.media.getPath(); 为原图path
                // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true  注意：音视频除外
                // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true  注意：音视频除外
                // 如果裁剪并压缩了，以取压缩路径为准，因为是先裁剪后压缩的
                val path = Uri.parse(if (selectList[0].isCompressed) {
                    selectList[0].compressPath
                } else selectList[0].path)
                when {
                    null != mUploadMessage -> {
                        mUploadMessage?.onReceiveValue(path)
                        mUploadMessage = null

                    }
                    null != mUploadMessageForAndroid5 -> {
                        if (path != null) {
                            mUploadMessageForAndroid5?.onReceiveValue(arrayOf(path))
                        } else {
                            mUploadMessageForAndroid5?.onReceiveValue(arrayOf())
                        }
                        mUploadMessageForAndroid5 = null
                    }
                    else -> return
                }
            }
        } else {
            when {
                null != mUploadMessage -> {
                    mUploadMessage?.onReceiveValue(Uri.EMPTY)
                    mUploadMessage = null

                }
                null != mUploadMessageForAndroid5 -> {
                    mUploadMessageForAndroid5?.onReceiveValue(arrayOf())
                    mUploadMessageForAndroid5 = null
                }
                else -> return
            }
        }

    }

    companion object {
        var Intent_WebUrl = "webUrl"
        var Intent_WebTitle = "webTitle"
    }

    override fun onPageChanged(page: Int, pageCount: Int) {

    }

    override fun onPageError(page: Int, t: Throwable?) {
    }

    override fun loadComplete(nbPages: Int) {
    }
}
