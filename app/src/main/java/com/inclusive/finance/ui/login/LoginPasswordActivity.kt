package com.inclusive.finance.ui.login

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.CountDownTimer
import android.text.TextUtils
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.blankj.utilcode.util.*
import com.hwangjr.rxbus.RxBus
import com.hwangjr.rxbus.annotation.Subscribe
import com.hwangjr.rxbus.annotation.Tag
import com.hwangjr.rxbus.thread.EventThread
import com.inclusive.finance.R
import com.inclusive.finance.app.MyApplication
import com.inclusive.finance.app.ToolApplication
import com.inclusive.finance.base.BaseActivity
import com.inclusive.finance.bean.User
import com.inclusive.finance.bean.model.LoginPasswordModel
import com.inclusive.finance.config.Constants
import com.inclusive.finance.config.Constants.BusAction.Bus_LoginSuccess
import com.inclusive.finance.config.PreferencesService
import com.inclusive.finance.databinding.ActivityLoginPwdBinding
import com.inclusive.finance.interfaces.PresenterClick
import com.inclusive.finance.utils.SZWUtils
import com.inclusive.finance.utils.StatusBarUtil
import com.lzy.okgo.OkGo
import com.lzy.okgo.model.HttpHeaders
import org.jetbrains.anko.startActivity

@Route(path = "/com/LoginPasswordActivity")
class LoginPasswordActivity : BaseActivity(), PresenterClick {
    private lateinit var viewModel: LoginPasswordModel
    private var countDownTimer: CountDownTimer? = null
    private val time = 60000 //倒计时时间
    private var downKey = "L"

    companion object {
        const val RESULT_LOGIN_CANCELED = 3000
        fun login(act: Activity, phoneNum: String, pwd: String, it: User?) {
            saveUserInfo(it, phoneNum, pwd)
            val intent = act.intent
            val className = intent.getStringExtra(Constants.Result.Intent_ClassName)
            if (!TextUtils.isEmpty(className)) {
                intent.setClassName(act, className ?: "")
                act.startActivityForResult(intent, 100)
            }
            RxBus.get().post(Bus_LoginSuccess, Bus_LoginSuccess)

        }

        private fun saveUserInfo(it: User?, phoneNum: String, pwd: String) {
            MyApplication.user = it //密码登录后，关于手势的清空
            SPUtils.getInstance().put(Constants.SPUtilsConfig.ISGESTURELOCK_KEY, false)
            SPUtils.getInstance().put(Constants.SPUtilsConfig.GESTURELOCK_KEY, "") //记录账号密码
            SPUtils.getInstance().put(Constants.SPUtilsConfig.SP_PHONE, phoneNum)
            SPUtils.getInstance().put(phoneNum, pwd) //记录token
            SPUtils.getInstance().put(Constants.SPUtilsConfig.SP_TOKEN, it?.token)
            val headers = HttpHeaders()
            headers.put("X-Access-Token", SPUtils.getInstance()
                .getString(Constants.SPUtilsConfig.SP_TOKEN))    //header不支持中文，不允许有特殊字符
            OkGo.getInstance().addCommonHeaders(headers)
        }

    }

    override fun initToolbar() {
        StatusBarUtil.darkMode(this)

     }

    lateinit var viewBind: ActivityLoginPwdBinding
    override fun setInflateBinding() {
        viewModel = ViewModelProvider(this).get(LoginPasswordModel::class.java)
        viewBind = DataBindingUtil.setContentView<ActivityLoginPwdBinding>(this, R.layout.activity_login_pwd)
            .apply {
                data = viewModel
                presenterClick = this@LoginPasswordActivity
                lifecycleOwner = this@LoginPasswordActivity
            }
        ToolApplication.singletonTest(this)


    }

    override fun init() {
//        viewBind.tvVersion.text = "V${BuildConfig.VERSION_NAME}"
        if (SPUtils.getInstance().getBoolean(Constants.SPUtilsConfig.ISGESTURELOCK_KEY, false)) {

        }

        var list = ToolApplication.singletonTest(this)
            .getSelectDate2("admin",
                "",
                "",
                0)
        if (list!=null){

            return
        }else{
            ToolApplication.singletonTest(this)?.intserUser(
                "admin",
                "管理员",
                "系统",
                "123456","通过")
        }
     }




    private var mLastClickTime: Long = 0
    private val TIME_INTERVAL = 1000L
    override fun onClick(v: View?) {
        when (v) {

            viewBind.btResg -> {
                 startActivity<ResgActivity>( "FROM" to "1")
            }
            viewBind.btLoginPwd -> {

//            ToolApplication.singletonTest(this).initBiaoTable()

                var muter=ToolApplication.singletonTest(this).allDate?.indexOfFirst {
                        b->  b.Password==viewBind.edPwd.text.toString()&&b.UserName==viewBind.edPhone.text.toString()
                }

                if (muter != null) {
                    if(muter!=-1){
                        ToolApplication.singletonTest(this).allDate.forEachIndexed { index, user ->
                            if(user.Password==viewBind.edPwd.text.toString()&&user.UserName==viewBind.edPhone.text.toString()){
                                if(user.userStatus!="通过"){
                                    ToastUtils.showShort("账号审核中")
                                }else {
                                    SPUtils.getInstance()
                                        .put(Constants.SPUtilsConfig.SP_PHONE, user.id)
                                    SPUtils.getInstance()
                                        .put(Constants.SPUtilsConfig.SP_REALNAME, user.RealName)
                                    SPUtils.getInstance()
                                        .put(Constants.SPUtilsConfig.SP_DepartmentName,
                                            user.departmentName)
                                    SPUtils.getInstance()
                                        .put(Constants.SPUtilsConfig.SP_LOGINNAME, user.userName)

                                    RxBus.get()
                                        .post(Constants.BusAction.Bus_LoginFinish,
                                            Constants.BusAction.Bus_LoginFinish)
                                }
                            }
                        }
                    }else{
                        ToastUtils.showLong("账号密码输入有误")
                    }
                }else{
                    ToastUtils.showLong("账号不存在")
                }

            }
        }
    }


    private fun downTimer(l: Long) {
        countDownTimer = object : CountDownTimer(l, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                resetTimer(false, millisUntilFinished)
            }

            override fun onFinish() {
                resetTimer(true, java.lang.Long.MIN_VALUE)
            }
        }
        countDownTimer?.start()
    }

    private fun resetTimer(b: Boolean, millisUntilFinished: Long) {


    }



    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (ev.action == MotionEvent.ACTION_DOWN) {
            val v = currentFocus
            if (SZWUtils.isShouldHideKeyboard(v, ev)) {
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(v?.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)

            }
        }
        return super.dispatchTouchEvent(ev)
    }

    @Subscribe(thread = EventThread.MAIN_THREAD, tags = arrayOf(Tag(Constants.BusAction.Bus_LoginFinish)))
    fun finish(str: String) {
        RxBus.get().post(Constants.BusAction.Bus_Refresh_Main, Constants.BusAction.Bus_Refresh_Main)
        ARouter.getInstance().build("/com/MainActivity")
            .withTransition(R.anim.fade_in, R.anim.slide_out_bottom)
            .navigation(this) //        if (ActivityUtils.isActivityExistsInStack(MainActivity::class.java)) {
        //            finish()
        //            overridePendingTransition(R.anim.fade_in, R.anim.slide_out_bottom)
        //        } else Handler(Looper.getMainLooper()).postDelayed({
        //            finish()
        //            overridePendingTransition(R.anim.fade_in, R.anim.slide_out_bottom)
        //        }, 2000)

    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            val intent = Intent(Intent.ACTION_MAIN)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            intent.addCategory(Intent.CATEGORY_HOME)
            startActivity(intent) //            }
            return true
        }
        return super.onKeyDown(keyCode, event)
    }


}