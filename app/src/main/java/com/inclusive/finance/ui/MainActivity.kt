package com.inclusive.finance.ui

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.KeyEvent
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.alibaba.android.arouter.facade.annotation.Route
import com.hwangjr.rxbus.annotation.Subscribe
import com.hwangjr.rxbus.annotation.Tag
import com.hwangjr.rxbus.thread.EventThread
import com.inclusive.finance.R
import com.inclusive.finance.base.BaseActivity
import com.inclusive.finance.base.MainNavigationFragment
import com.inclusive.finance.base.NavigationHost
import com.inclusive.finance.databinding.ActivityMainBinding
import com.inclusive.finance.utils.SZWUtils
import com.inclusive.finance.utils.StatusBarUtil
import com.inclusive.finance.utils.Watermark
import org.jetbrains.anko.support.v4.act

@Route(path = "/com/MainActivity")
class MainActivity : BaseActivity(), NavigationHost {
    lateinit var navHostFragment: NavHostFragment
    private val TOP_LEVEL_DESTINATIONS = setOf(
        R.id.navigation_main,
        R.id.navigation_applyList,
        R.id.navigation_investigationList,
        R.id.navigation_creditApproval,
        R.id.navigation_creditApproval2,
        R.id.navigation_creditApproval3,
        R.id.navigation_creditListFragment,
        R.id.navigation_jiNianJianCaiJiListFragment,
        R.id.navigation_jiNianJianFeiXianChangJianChaListFragment,
        R.id.navigation_jiNianJianXianChangJianChaListFragment,
        R.id.navigation_JiNianJianYongXinListFragment,
        R.id.navigation_VisitListFragment,
        R.id.navigation_PreCreditFragment,
        R.id.navigation_SunPreCreditTaskFragment,
        R.id.navigation_VisitApprovalFragment,
        R.id.navigation_ClockInListFragment,
        R.id.navigation_TrackFragment,
        R.id.navigation_PointClusterFragment,
        R.id.navigation_PicAddListFragment,
        R.id.navigation_YongXinGuanLiListFragment,
        R.id.navigation_QRCodeListFragment,
        R.id.navigation_ShouLiListFragment,
        R.id.navigation_RiChangJianListFragment,
        R.id.navigation_XinXiYuanListFragment,
        R.id.navigation_WenJuanDiaoChaListFragment,
        R.id.navigation_ShouXinPingYiListFragment,
        R.id.navigation_EDuBiDuiListFragment,
        R.id.navigation_DataMapFragment,
        R.id.navigation_FengxianxinxiFragment,
        R.id.navigation_CustomerPoolListFragment,
        R.id.navigation_CustomerManagementListFragment,
        R.id.navigation_ShouXinXiangXiaListFragment,
        R.id.navigation_HeTongDengJiListFragment,
        R.id.navigation_FangDaiShengQingListFragment,
    )

    override fun initToolbar() {
        StatusBarUtil.darkMode(this)
    }

    lateinit var viewBind: ActivityMainBinding
    override fun setInflateBinding() {
        viewBind = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
            .apply {
                lifecycleOwner = this@MainActivity
            }

    }

    override fun init() {
        navHostFragment = supportFragmentManager.findFragmentById(R.id.my_nav_host_fragment) as NavHostFragment
//        Watermark.instance?.show(this, "Fantasy BlogDemo")
        NavigationUI.setupWithNavController(viewBind.bottomNavigation, navHostFragment.navController)

// 设置监听
        val  onDestinationChangedListener = NavController
            .OnDestinationChangedListener { _, destination, _ ->
                Log.e("lzp", destination.label.toString())
                if ((destination.label=="MainFragment")
                    || (destination.label=="BacklogFragment")
                    ||  (destination.label=="MineFragment")
                ){
                    viewBind.bottomNavigation.visibility= View.VISIBLE
                }else{
                    viewBind.bottomNavigation.visibility= View.GONE
                }

            }
        navHostFragment.navController.addOnDestinationChangedListener(onDestinationChangedListener)
        //        SZWUtils.disableShiftMode(bottom_navigation)
    }

    override fun onSupportNavigateUp() =
        Navigation.findNavController(this, R.id.my_nav_host_fragment)
            .navigateUp() || super.onSupportNavigateUp()


    override fun onOptionsItemSelected(item: MenuItem) =
        (NavigationUI.onNavDestinationSelected(item, Navigation.findNavController(this, R.id.my_nav_host_fragment)) || super.onOptionsItemSelected(item))


    override fun onUserInteraction() {
        super.onUserInteraction()
        getCurrentFragment()?.onUserInteraction()
    }

    private fun getCurrentFragment(): MainNavigationFragment? {
        return navHostFragment?.childFragmentManager?.primaryNavigationFragment as? MainNavigationFragment
    }

    override fun registerToolbarWithNavigation(toolbar: Toolbar) {
        val appBarConfiguration = AppBarConfiguration(TOP_LEVEL_DESTINATIONS, null)
        toolbar.setupWithNavController(Navigation.findNavController(this, R.id.my_nav_host_fragment), appBarConfiguration)
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
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    //    /**
    //    登录后刷新数据，
    //     */
        @Subscribe(thread = EventThread.MAIN_THREAD, tags = [Tag("dismiss")])
        fun loginSuccess(str: String) {
             finish()
        }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_VOLUME_UP || keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
            return super.onKeyDown(keyCode, event)
        } else if (Navigation.findNavController(this, R.id.my_nav_host_fragment).navigateUp()) {
            return true
        } else if (keyCode == KeyEvent.KEYCODE_BACK) {
            val intent = Intent(Intent.ACTION_MAIN)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            intent.addCategory(Intent.CATEGORY_HOME)
            startActivity(intent)
            //            }
            return true
        }
        return super.onKeyDown(keyCode, event)
    }
}
