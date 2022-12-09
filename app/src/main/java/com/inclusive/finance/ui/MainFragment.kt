package com.inclusive.finance.ui

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.PopupMenu
import androidx.core.content.ContextCompat
import androidx.core.view.setPadding
import androidx.lifecycle.ViewModelProvider
import com.alibaba.android.arouter.launcher.ARouter
import com.blankj.utilcode.util.SPUtils
import com.hwangjr.rxbus.RxBus
import com.hwangjr.rxbus.annotation.Subscribe
import com.hwangjr.rxbus.annotation.Tag
import com.hwangjr.rxbus.thread.EventThread
import com.inclusive.finance.IRouter
import com.inclusive.finance.R
import com.inclusive.finance.app.MyApplication
import com.inclusive.finance.base.MainNavigationFragment
import com.inclusive.finance.bean.User
import com.inclusive.finance.bean.model.ApplyModel
import com.inclusive.finance.bean.model.MainActivityModel
import com.inclusive.finance.config.Constants
import com.inclusive.finance.databinding.FragmentMainBinding
import com.inclusive.finance.pop.AccountPop
import com.inclusive.finance.utils.StatusBarUtil
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshListener
import org.jetbrains.anko.support.v4.act

class MainFragment : MainNavigationFragment(), OnRefreshListener {
    private var accountPop: AccountPop? = null
    lateinit var viewModel: MainActivityModel

    private var mLastClickTime: Long = 0
    private val TIME_INTERVAL = 500L
    var viewBind: FragmentMainBinding? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        viewModel = ViewModelProvider(act).get(MainActivityModel::class.java)
        if (viewBind == null) {
            viewBind = FragmentMainBinding.inflate(inflater, container, false).apply {
                data = viewModel
                 lifecycleOwner = viewLifecycleOwner

            }
            oneInit()
        }
        if(SPUtils.getInstance().getString(Constants.SPUtilsConfig.SP_LOGINNAME)!="admin"){
            viewBind?.actionBarCustom?.btSystem?.visibility=View.GONE
            viewBind?.actionBarCustom?.btFiel?.visibility=View.GONE

        }
        viewBind?.lifecycleOwner = viewLifecycleOwner
        return viewBind?.root ?: View(context)
    }

    private fun oneInit() {
        StatusBarUtil.darkMode(act)
        if(SPUtils.getInstance().getString(Constants.SPUtilsConfig.SP_LOGINNAME)!="admin"){
            viewBind?.actionBarCustom?.btSystem?.visibility=View.GONE
            viewBind?.actionBarCustom?.btFiel?.visibility=View.GONE

        }
        viewBind?.actionBarCustom?.btSystem?.setOnClickListener {

            IRouter.goF(it, R.id.action_to_applyActivity, "", ApplyModel.BUSINESS_TYPE_APPLY, false)
        }
        viewBind?.actionBarCustom?.btSearch?.setOnClickListener {
            IRouter.goF(it, R.id.action_to_applyActivity, "", ApplyModel.BUSINESS_TYPE_LISHI, false)

        }
        viewBind?.actionBarCustom?.btShoucang?.setOnClickListener {
            IRouter.goF(it,
                R.id.action_to_applyActivity,
                "",
                ApplyModel.BUSINESS_TYPE_SHOUCANG,
                false)

        }
        viewBind?.actionBarCustom?.btFiel?.setOnClickListener {
            IRouter.goF(it,
                R.id.action_to_applyActivity,
                "",
                ApplyModel.BUSINESS_TYPE_WENJIAN,
                false)

        }
        StatusBarUtil.setPaddingSmart(act, viewBind?.actionBarCustom?.appBar)
         var title=viewBind?.actionBarCustom?.toolbar?.findViewById<TextView>(R.id.mleft)
        title?.visibility=View.VISIBLE
        title?.text=getString(R.string.LITERATURE)
        viewBind?.actionBarCustom?.toolbar?.menu?.add(getString(R.string.userhelp))?.apply {
            setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
            actionView = TextView(context).apply {
                setPadding(15)
                setTextColor(resources.getColor(R.color.white))
                SPUtils.getInstance().getString(Constants.SPUtilsConfig.SP_REALNAME).also { text = it }
                if (isAdded)
                setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, ContextCompat.getDrawable(context, R.mipmap.xial), null)
                setOnClickListener {
                    val popup = PopupMenu(act, this)
                     popup.menuInflater.inflate(R.menu.main_menu_toolbar_overflow, popup.menu)
                    popup.show()
                    popup.setOnMenuItemClickListener {
                        when (it.itemId) {

                            R.id.item_account -> {
                                if (accountPop == null) {
                                    accountPop = AccountPop(act as MainActivity)
                                    viewModel.accountPop = accountPop
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
//                SPUtils.getInstance().getString(Constants.SPUtilsConfig.SP_DepartmentName).also { text = it }
//                if (isAdded)
//                 setOnClickListener {
//
//
//                }
//            }
//        }
        viewBind?.btSearch?.setOnClickListener { IRouter.goF(it,"action_to_applyList2",viewBind?.etSearch?.text?.toString()) }
        viewBind?.ivSearch?.setOnClickListener { IRouter.goF(it,"action_to_applyList","wssssssss") }



    }


    override fun onConfigurationChanged(newConfig: Configuration) {
         super.onConfigurationChanged(newConfig)
    }

    @Subscribe(thread = EventThread.MAIN_THREAD, tags = [Tag(Constants.BusAction.Bus_LoginSuccess)])
    fun loginSuccess(str: String) {

    }

    override fun refreshData(type: Int?) {

    }

    override fun onRefresh(refreshLayout: RefreshLayout) {

        if (MyApplication.user != null) (viewBind?.actionBarCustom?.toolbar?.menu?.getItem(0)?.actionView as TextView).text = "欢迎：${(MyApplication.user as User).userInfo?.realname}"


    }
}