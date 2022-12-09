package com.inclusive.finance.pop


import android.view.View
import android.view.animation.Animation
import androidx.fragment.app.FragmentContainerView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.blankj.utilcode.util.SPUtils
import com.hwangjr.rxbus.RxBus
import com.hwangjr.rxbus.annotation.Subscribe
import com.hwangjr.rxbus.annotation.Tag
import com.hwangjr.rxbus.thread.EventThread
import com.inclusive.finance.R
import com.inclusive.finance.bean.model.MainActivityModel
import com.inclusive.finance.config.Constants
import com.inclusive.finance.ui.MainActivity
import razerdp.basepopup.BasePopupWindow
import razerdp.util.animation.AlphaConfig
import razerdp.util.animation.AnimationHelper

class AccountPop(context: MainActivity) : BasePopupWindow(context) {

    override fun onCreateContentView(): View {
        return createPopupById(R.layout.pop_account)
    }

    private var viewModel: MainActivityModel

    init {
        setOutSideDismiss(false)
        RxBus.get().register(this)
        viewModel = ViewModelProvider(context).get(MainActivityModel::class.java)

    }

    override fun showPopupWindow() {
        viewModel.isGestureLogin.value = SPUtils.getInstance().getBoolean(
            Constants.SPUtilsConfig.ISGESTURELOCK_KEY, false
        )
        super.showPopupWindow()
    }

    override fun onCreateShowAnimation(): Animation {
        return AnimationHelper.asAnimation()
            .withAlpha(AlphaConfig.IN)
            .toShow()
    }

    override fun onCreateDismissAnimation(): Animation {
        return  AnimationHelper.asAnimation()
            .withAlpha(AlphaConfig.OUT)
            .toDismiss()
    }


    @Subscribe(thread = EventThread.MAIN_THREAD, tags = arrayOf(Tag("dismiss")))
    fun dismiss(arg: String) {
        onBackPressed()
    }

    override fun onBackPressed(): Boolean {
        if (contentView.findViewById<FragmentContainerView>(R.id.nav_host_account_fragment).findNavController().navigateUp()
        ) {
            return true
        } else {

            dismiss()
        }
        return super.onBackPressed()
    }
}