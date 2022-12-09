package com.inclusive.finance.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import com.alibaba.android.arouter.launcher.ARouter
import com.blankj.utilcode.util.SPUtils
import com.hwangjr.rxbus.RxBus
import com.inclusive.finance.R
import com.inclusive.finance.base.MyBaseFragment
import com.inclusive.finance.bean.model.MainActivityModel
import com.inclusive.finance.config.Constants
import com.inclusive.finance.databinding.FragmentAccountCenterBinding
import org.jetbrains.anko.support.v4.act

class AccountCenterFragment : MyBaseFragment(), View.OnClickListener,
    CompoundButton.OnCheckedChangeListener {
    lateinit var viewModel: MainActivityModel
    lateinit var viewBind: FragmentAccountCenterBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(act).get(MainActivityModel::class.java)

        viewBind = FragmentAccountCenterBinding.inflate(inflater, container, false).apply {
            data = viewModel
            lifecycleOwner = viewLifecycleOwner
        }
        return viewBind.root
    }

    override fun initView() {
         viewBind.btClose.setOnClickListener(this)
         viewBind.btPwdChange.setOnClickListener(this)
         viewBind.btGestureChange.setOnClickListener(this)
         viewBind.isGestureLogin.setOnCheckedChangeListener(this)
    }

    override fun onClick(v: View?) {
        when (v) {
            viewBind.btClose -> {
//                dismiss()
                RxBus.get().post("dismiss","dismiss")
                 }
            viewBind.btPwdChange -> {
                NavHostFragment.findNavController(this)
                    .navigate(R.id.action_to_pwd)
            }
            viewBind.btGestureChange -> {
                RxBus.get().post("dismiss","dismiss")
                ARouter.getInstance().build("/com/LoginPasswordActivity")
                    .withTransition(R.anim.slide_in_bottom, android.R.anim.fade_out)
                    .navigation(context)

            }
            else -> {
            }
        }
    }

    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
        if (isChecked) {
            if (SPUtils.getInstance().getString(Constants.SPUtilsConfig.GESTURELOCK_KEY)
                    .isEmpty()
            ) {
                viewModel.isGestureLogin.value = false
                NavHostFragment.findNavController(this)
                    .navigate(R.id.action_to_gesture)
            } else {
                SPUtils.getInstance().put(Constants.SPUtilsConfig.ISGESTURELOCK_KEY, isChecked)
            }
        } else {
            SPUtils.getInstance().put(Constants.SPUtilsConfig.ISGESTURELOCK_KEY, isChecked)
        }
    }


}