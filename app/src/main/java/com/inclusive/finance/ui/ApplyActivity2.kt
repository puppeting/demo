package com.inclusive.finance.ui

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.net.Uri
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.view.ViewStub
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.widget.PopupMenu
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.setPadding
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavGraph
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.blankj.utilcode.util.SPUtils
import com.flyco.roundview.RoundLinearLayout
import com.hwangjr.rxbus.RxBus
import com.hwangjr.rxbus.annotation.Subscribe
import com.hwangjr.rxbus.annotation.Tag
import com.hwangjr.rxbus.thread.EventThread
import com.inclusive.finance.DataCtrlClass
import com.inclusive.finance.R
import com.inclusive.finance.base.BaseActivity
import com.inclusive.finance.base.MyBaseFragment
import com.inclusive.finance.base.permissionCameraWithPermissionCheck
import com.inclusive.finance.bean.model.ApplyModel
import com.inclusive.finance.config.Constants
import com.inclusive.finance.databinding.*
import com.inclusive.finance.interfaces.PresenterClick
import com.inclusive.finance.pop.AccountPop
import com.inclusive.finance.pop.AccountPop2
import com.inclusive.finance.pop.AccountPop3
import com.inclusive.finance.utils.SZWUtils
import com.inclusive.finance.utils.StatusBarUtil
import org.jetbrains.anko.support.v4.act

@Route(path = "/com/ApplyActivity")
class ApplyActivity2 : BaseActivity(), PresenterClick {


    private var accountPop: AccountPop3? = null

    @Autowired
    @JvmField
    var creditId: String? = ""

    @Autowired
    @JvmField
    var dhId: String? = ""

    @Autowired
    @JvmField
    var zfId: String? = ""

    @Autowired
    @JvmField
    var ysxId: String? = ""

    @Autowired
    @JvmField
    var keyId: String? = ""

    @Autowired
    @JvmField
    var idno: String? = ""

    @Autowired
    @JvmField
    var jgno: String? = ""

    @Autowired
    @JvmField
    var jsonObject = ""

    @Autowired
    @JvmField
    var seeOnly: Boolean? = false

    @Autowired
    @JvmField
    var businessType: Int = ApplyModel.BUSINESS_TYPE_APPLY

    lateinit var viewModel: ApplyModel
    lateinit var viewBind: ActivityApplyBinding
    var menuBind: ViewDataBinding? = null
    override fun initToolbar() {
        permissionCameraWithPermissionCheck(null, 200, false) {}
        StatusBarUtil.darkMode(this)
        StatusBarUtil.setPaddingSmart(this, viewBind.appBar)
    }

    override fun setInflateBinding() {
        viewBind =
            DataBindingUtil.setContentView<ActivityApplyBinding>(this, R.layout.activity_apply)
                .apply {
                    viewModel = ViewModelProvider(this@ApplyActivity2).get(ApplyModel::class.java)
                    data = viewModel
                    presenterClick = this@ApplyActivity2
                    lifecycleOwner = this@ApplyActivity2
                }
    }

    override fun init() {
        viewModel.creditId = creditId
        viewModel.dhId = dhId
        viewModel.zfId = zfId
        viewModel.ysxId = ysxId
        viewModel.keyId = keyId
        viewModel.seeOnly = seeOnly
        viewModel.businessType = businessType
        viewModel.jsonObject = jsonObject
        viewModel.idno = idno
        viewModel.jgno = jgno

        //        if (viewModel.creditId.isNullOrEmpty() && !ToolApplication.appDebug) {
        //            toast("请选择客户后再进行操作")
        //            ActivityUtils.finishActivity(this)
        //        }

//        Watermark.instance?.show(this, "Fantasy BlogDemo")
        viewBind.toolbar.setNavigationOnClickListener {
            finish()
        }

        viewBind.titleButtonLay.removeAllViews()
        val generateViewId = View.generateViewId()
        viewBind.titleButtonLay.addView(ViewStub(this).apply {
            id = generateViewId
        })
        val inflateView: View
        when (viewModel.businessType) {
            ApplyModel.BUSINESS_TYPE_APPLY,
            ApplyModel.BUSINESS_TYPE_SHOUCANG,
            ApplyModel.BUSINESS_TYPE_LISHI,
            ApplyModel.BUSINESS_TYPE_WENJIAN,

            -> {
                viewBind.toolbar.title =  "文献检索"
                viewBind.root.findViewById<ViewStub>(generateViewId).layoutResource =
                    R.layout.activity_apply_include_credit
                inflateView = viewBind.root.findViewById<ViewStub>(generateViewId).inflate()
                val bind = DataBindingUtil.bind<ActivityApplyIncludeCreditBinding>(inflateView)

                if(SPUtils.getInstance().getString(Constants.SPUtilsConfig.SP_LOGINNAME)!="admin"){
                    bind?.btSystem?.visibility=View.GONE
                    bind?.btFiel?.visibility=View.GONE

                }
                bind?.data = viewModel
                bind?.presenterClick = this@ApplyActivity2
                viewBind?.toolbar?.menu?.add(getString(R.string.userhelp))?.apply {
                    setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
                    actionView = TextView(this@ApplyActivity2).apply {
                        setPadding(15)
                        setTextColor(resources.getColor(R.color.white))
                        SPUtils.getInstance().getString(Constants.SPUtilsConfig.SP_REALNAME).also { text = it }

                         setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, ContextCompat.getDrawable(context, R.mipmap.xial), null)
                        setOnClickListener {
                            val popup = PopupMenu(this@ApplyActivity2, this)
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
                                            accountPop = AccountPop3(this@ApplyActivity2)
//                                            viewModel.accountPop = accountPop
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
//                viewBind?.toolbar?.menu?.add(getString(R.string.houqin))?.apply {
//                    setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
//                    actionView = TextView(this@ApplyActivity2).apply {
//                        setPadding(15)
//                        setTextColor(resources.getColor(R.color.white))
//                        SPUtils.getInstance().getString(Constants.SPUtilsConfig.SP_DepartmentName).also { text = it }
//
//                    }
//                }
            }


            ApplyModel.BUSINESS_TYPE_CUSTOMER_POOL -> {
                viewBind.toolbar.title = "客户信息"
                viewBind.root.findViewById<ViewStub>(generateViewId).layoutResource =
                    R.layout.activity_apply_customer_pool
                inflateView = viewBind.root.findViewById<ViewStub>(generateViewId).inflate()
                val bind = DataBindingUtil.bind<ActivityApplyCustomerPoolBinding>(inflateView)
//                val activityApplyIncludeCreditBinding = viewBind.root.findViewById<ViewStub>(generateViewId).binding as ActivityApplyIncludeCreditBinding
                bind?.flow?.referencedIds = when (viewModel.businessType) {
                    ApplyModel.BUSINESS_TYPE_CUSTOMER_POOL -> intArrayOf(
                        R.id.constraintLay47,
                        R.id.constraintLay50,
                        R.id.constraintLay51,
                        R.id.constraintLay52,
                        R.id.constraintLay53,
                        R.id.constraintLay48,
                        R.id.constraintLay49
                    )

                    else -> null
                }
                bind?.data = viewModel
                jgno=viewModel?.jgno
                idno=viewModel?.idno
                bind?.presenterClick = this@ApplyActivity2
            }
            else ->{
                viewBind.toolbar.title = "客户信息"
                viewBind.root.findViewById<ViewStub>(generateViewId).layoutResource =
                    R.layout.activity_apply_customer_pool
                inflateView = viewBind.root.findViewById<ViewStub>(generateViewId).inflate()
                val bind = DataBindingUtil.bind<ActivityApplyCustomerPoolBinding>(inflateView)
//                val activityApplyIncludeCreditBinding = viewBind.root.findViewById<ViewStub>(generateViewId).binding as ActivityApplyIncludeCreditBinding
                bind?.flow?.referencedIds = when (viewModel.businessType) {
                    ApplyModel.BUSINESS_TYPE_CUSTOMER_POOL -> intArrayOf(
                        R.id.constraintLay47,
                        R.id.constraintLay50,
                        R.id.constraintLay51,
                        R.id.constraintLay52,
                        R.id.constraintLay53,
                        R.id.constraintLay48,
                        R.id.constraintLay49
                    )

                    else -> null
                }
                bind?.data = viewModel
                jgno=viewModel?.jgno
                idno=viewModel?.idno
                bind?.presenterClick = this@ApplyActivity2
            }
        }
        menuBind = DataBindingUtil.getBinding(inflateView)

        when (menuBind) {

            is ActivityApplyIncludeCreditBinding -> {

            }

        }
        val navHostFragment: NavHostFragment =
            supportFragmentManager.findFragmentById(R.id.apply_nav_host_fragment) as NavHostFragment
        val navGraph: NavGraph =
            navHostFragment.navController.navInflater.inflate(R.navigation.nav_graph_apply)
        navGraph.setStartDestination(getActionByType(viewModel.businessType))
        navHostFragment.navController.graph = navGraph
//        viewModel.applyCheckBean?.xxgkCheck = true
    }
    @Subscribe(thread = EventThread.MAIN_THREAD, tags = [Tag("dismiss")])
    fun loginSuccessd(str: String) {
        finish()
    }
    private fun getActionByType(index: Int): Int = when (index) {
        ApplyModel.BUSINESS_TYPE_SHOUCANG,
        -> {
            viewModel.applyCheckBean?.bdqzCheck = true
            R.id.navigation_ShouCangListFragment
        }
        ApplyModel.BUSINESS_TYPE_WENJIAN,

        -> {
            viewModel.applyCheckBean?.csjgCheck = true
            R.id.navigation_ApplyWenJiantFragment
        }
        ApplyModel.BUSINESS_TYPE_LISHI,

        -> {
            viewModel.applyCheckBean?.cwjbCheck = true
            R.id.navigation_ApplyLishiFragment
        }
        ApplyModel.BUSINESS_TYPE_APPLY,

        -> {
            viewModel.applyCheckBean?.dcbgCheck = true
            R.id.navigation_ApplyPwdFragment
        }

        else -> {
            viewModel.applyCheckBean?.xxgkCheck = true
            R.id.customerInfoFragment
        }
    }

    override fun refreshData(type: Int?) {
        initData()
    }

    override fun initData() {
        //        val mainData = SZWUtils.getJson(this, "checkData.json")
        //        val data = Gson().fromJson(mainData, ComleteCheckBean::class.java)
        //        viewModel.applyCheckBean?.completeCheckBean = data
        if (viewModel.businessType != ApplyModel.BUSINESS_TYPE_CUSTOMER_POOL) {

        }else{

        }
    }

    /**
    登录后刷新数据，
     */
    @Subscribe(thread = EventThread.MAIN_THREAD, tags = [Tag(Constants.BusAction.Bus_LoginSuccess)])
    fun loginSuccess(str: String) {
        ((supportFragmentManager.findFragmentById(R.id.apply_nav_host_fragment) as NavHostFragment?)?.childFragmentManager?.primaryNavigationFragment as? MyBaseFragment)?.refreshData()
    }

    override fun onSupportNavigateUp() =
        Navigation.findNavController(this, R.id.apply_nav_host_fragment)
            .navigateUp() || super.onSupportNavigateUp()

    override fun onBackPressed() {
        finish()
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (ev.action == MotionEvent.ACTION_DOWN) {
            val v = currentFocus
            if (SZWUtils.isShouldHideKeyboard(v, ev)) {
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(v?.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
                viewBind.viewOverlay.isFocusable = true
                viewBind.viewOverlay.isFocusableInTouchMode = true
                viewBind.viewOverlay.requestFocus()
                (v as EditText).clearFocus()
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    override fun onClick(v: View?) {
        val findNavController = Navigation.findNavController(this, R.id.apply_nav_host_fragment)
        viewModel.applyCheckBean?.resetCheck()
        viewModel.jsonObject = null
        //        val bind= when (viewModel.businessType) {
        //            ApplyModel.BUSINESS_TYPE_APPLY -> {
        //                (viewBind.titleMenuViewStub.binding as ActivityApplyIncludeCreditBinding)
        //            }
        //            ApplyModel.BUSINESS_TYPE_INVESTIGATE-> {
        //                (viewBind.titleMenuViewStub.binding as ActivityApplyIncludeCreditBinding)
        //            }
        //            else -> {
        //                (viewBind.titleMenuViewStub.binding as ActivityApplyIncludeCjPersonalBinding)
        //            }
        //        }
        val fragmentId = when (v) {
            menuBind?.root?.findViewById<ConstraintLayout>(R.id.constraintLay1) -> {
                viewModel.applyCheckBean?.xxgkCheck = true
                R.id.customerInfoFragment
            }
            menuBind?.root?.findViewById<RoundLinearLayout>(R.id.bt_shoucang) -> {
                viewModel.applyCheckBean?.bdqzCheck = true
                R.id.navigation_ShouCangListFragment
            }
            menuBind?.root?.findViewById<RoundLinearLayout>(R.id.bt_fiel) -> {
                viewModel.applyCheckBean?.csjgCheck = true
                R.id.navigation_ApplyWenJiantFragment
            }
            menuBind?.root?.findViewById<RoundLinearLayout>(R.id.bt_search) -> {
                viewModel.applyCheckBean?.cwjbCheck = true
                R.id.navigation_ApplyLishiFragment
            }
            menuBind?.root?.findViewById<RoundLinearLayout>(R.id.bt_system) -> {
                viewModel.applyCheckBean?.dcbgCheck = true
                R.id.navigation_ApplyPwdFragment
            }
            else -> {
                R.id.EmptyFragment
            }
        }

        val build = NavOptions.Builder().setPopUpTo(fragmentId as Int, true).build()
        val bundle = Bundle()

            findNavController.navigate(fragmentId, null, build)

    }

    override fun onDestroy() {
        RxBus.get().post(Constants.BusAction.Bus_Refresh_List, Constants.BusAction.Bus_Refresh_List)
        super.onDestroy()
    }






}
