package com.inclusive.finance.ui.login

import android.content.Context
import android.content.Intent
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.core.app.ActivityOptionsCompat
import androidx.databinding.DataBindingUtil
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.blankj.utilcode.util.AppUtils
import com.hwangjr.rxbus.RxBus
import com.hwangjr.rxbus.annotation.Subscribe
import com.hwangjr.rxbus.annotation.Tag
import com.hwangjr.rxbus.thread.EventThread
import com.inclusive.finance.R
import com.inclusive.finance.app.MyApplication
import com.inclusive.finance.base.BaseActivity
import com.inclusive.finance.base.permissionWAndRWithPermissionCheck
import com.inclusive.finance.bean.User
import com.inclusive.finance.config.Constants
import com.inclusive.finance.config.PreferencesService
import com.inclusive.finance.databinding.ActivityStartPageTransBinding
import com.inclusive.finance.utils.StatusBarUtil


/**
 * Created by pc on 2017/12/4.
 */
@Route(path = "/com/StartPageTransActivity")
class StartPageTransActivity : BaseActivity() {
    override fun initToolbar() {
        StatusBarUtil.darkMode(this)
    }


    lateinit var viewBind: ActivityStartPageTransBinding
    override fun setInflateBinding() {
        viewBind = DataBindingUtil.setContentView<ActivityStartPageTransBinding>(this, R.layout.activity_start_page_trans)
            .apply {
                lifecycleOwner = this@StartPageTransActivity
            }

    }

    override fun init() {
        super.init()
        initView()
    }


    private fun initView() {
        if (intent.flags and Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT != 0) {
            finish()
            return
        }

        //        UpdateHelper.getInstance().init(applicationContext, Color.parseColor("#0A93DB"));
        //        UpdateHelper.getInstance().autoUpdate(packageName, false, 120000);

        //        BDAutoUpdateSDK.uiUpdateAction(applicationContext, object : UICheckUpdateCallback {
        //            override fun onNoUpdateFound() {
        //
        //            }
        //
        //            override fun onCheckComplete() {
        //
        //            }
        //        })

//                PermissionLocationWithCheck(Intent(this, LocationService::class.java),true)
        val anim = AnimationUtils.loadAnimation(this, R.anim.logo_fade_in)
        anim.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {

                //                login()
            }

            override fun onAnimationRepeat(animation: Animation) {}

            override fun onAnimationEnd(animation: Animation) {
                viewBind.imgLogo.postDelayed({
                    val preferences = getSharedPreferences(packageName, Context.MODE_PRIVATE)
                    val versionCode = preferences.getInt("AppVersion", 0)

                    val flag = AppUtils.getAppVersionCode() > versionCode

                    //                    if (ToolApplication.appDebug || flag) {
                    //                        val editor = preferences.edit()
                    //                        editor.putInt("AppVersion", AppUtils.getAppVersionCode())
                    //                        editor.apply()
                    //                        ARouter.getInstance().build("/com/GuideActivity")
                    //                            .withTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                    //                            .navigation(this@StartPageTransActivity, 100)
                    //
                    //                    } else {
                    permissionWAndRWithPermissionCheck(null, 100, false, Runnable {
                        jump()
                    })
                    //                    type = 1
                    //                    jump(type)
                    //                    }
                }, 100)
            }
        })
        viewBind.imgLogo.animation = anim

    }

    //    /**
    //     * 登录
    //     * */
    //    fun login() {
    //        if (UMShareAPI.get(this).isAuthorize(this, SHARE_MEDIA.WEIXIN)) {
    //            UMShareAPI.get(this).getPlatformInfo(this, SHARE_MEDIA.WEIXIN, object : UMAuthListener {
    //                override fun onComplete(p0: SHARE_MEDIA?, p1: Int, p2: MutableMap<String, String>?) {
    //                    DataCtrlClass.loginWechatAppNoDialog(
    //                        this@StartPageTransActivity, p2?.get("uid")
    //                            ?: "", p2?.get("iconurl") ?: ""
    //                    ) {
    //                        LoginThirdActivity.login(
    //                            this@StartPageTransActivity,
    //                            p2?.get("uid")
    //                                ?: "", p2?.get("iconurl") ?: "", it
    //                        )
    //                        if (canJump)
    //                            jump()
    //                        else
    //                            canJump = true
    //                    }
    //                }
    //
    //                override fun onCancel(p0: SHARE_MEDIA?, p1: Int) {
    //                }
    //
    //                override fun onError(p0: SHARE_MEDIA?, p1: Int, p2: Throwable?) {
    //                }
    //
    //                override fun onStart(p0: SHARE_MEDIA?) {
    //                }
    //            })
    //        } else {
    //            if (canJump)
    //                jump()
    //            else
    //                canJump = true
    //        }
    //    }

    /**
     * @param type 0 主界面，1， 登录
     */
    private fun jump() {

        if (MyApplication.checkUserLogin() && !(MyApplication.user as User).telephone.isNullOrEmpty()) {
            PreferencesService.saveAccount(this@StartPageTransActivity, (MyApplication.user as User).userId, (MyApplication.user as User).headUrl
                ?: "")
            RxBus.get()
                .post(Constants.BusAction.Bus_LoginFinish, Constants.BusAction.Bus_LoginFinish)
            //            ARouter.getInstance().build("/com/MainActivity").withTransition(android.R.anim.fade_in, android.R.anim.fade_out).navigation(this)
        } else {

            ARouter.getInstance().build("/com/LoginPasswordActivity")
                .withOptionsCompat(ActivityOptionsCompat.makeSceneTransitionAnimation(this, viewBind.imgTop, "shareLogo"))
                .withTransition(android.R.anim.fade_in, android.R.anim.fade_out).navigation(this)

            //            val intent = Intent(this, LoginThirdActivity::class.java)
            //            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this, img_top, "shareLogo").toBundle())
        }
    }

    @Subscribe(thread = EventThread.MAIN_THREAD, tags = arrayOf(Tag(Constants.BusAction.Bus_LoginFinish)))
    fun loginFInish(str: String) {
        //        overridePendingTransition(R.anim.logo_fade_in, R.anim.slide_out_bottom)
        //        finish()
        //        ARouter.getInstance().build("/com/MainActivity").withTransition(R.anim.slide_in_bottom, android.R.anim.fade_out).navigation(this)
        finish()
    }
}
