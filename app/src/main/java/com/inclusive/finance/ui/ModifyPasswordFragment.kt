package com.inclusive.finance.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import com.blankj.utilcode.util.ObjectUtils
import com.blankj.utilcode.util.SPUtils
import com.blankj.utilcode.util.ToastUtils
import com.inclusive.finance.app.ToolApplication
import com.inclusive.finance.base.MyBaseFragment
import com.inclusive.finance.bean.model.MainActivityModel
import com.inclusive.finance.config.Constants
import com.inclusive.finance.databinding.FragmentModifyPasswordBinding
import org.jetbrains.anko.support.v4.act
import razerdp.basepopup.BasePopupWindow

class ModifyPasswordFragment : MyBaseFragment(), View.OnFocusChangeListener {
    private var muser: List<com.inclusive.finance.room.User>? = arrayListOf()
    private lateinit var viewModel:MainActivityModel
    lateinit var viewBind: FragmentModifyPasswordBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBind = FragmentModifyPasswordBinding.inflate(inflater, container, false)
        return viewBind.root
    }


    override fun initView() {
        viewModel = ViewModelProvider(act).get(MainActivityModel::class.java)
        viewBind.actionBarCustom.toolbar.setNavigationOnClickListener {
            NavHostFragment.findNavController(this)
                .navigateUp()
        }
        if (isAdded)
        if (act is NavActivity){
            viewBind.actionBarCustom.root.visibility=View.GONE
        }
        viewBind.actionBarCustom.btSearch.visibility=View.GONE
        viewBind.actionBarCustom.btSystem.visibility=View.GONE
        viewBind.actionBarCustom.btFiel.visibility=View.GONE
        viewBind.actionBarCustom.btShoucang.visibility=View.GONE
        muser=ToolApplication.singletonTest(requireContext()).allDate?.filter { SPUtils.getInstance().getInt(Constants.SPUtilsConfig.SP_PHONE)==it.id  }
        viewBind.tvAccount.text = SPUtils.getInstance().getString(Constants.SPUtilsConfig.SP_LOGINNAME).toString()
        viewBind.tvOk.setOnClickListener {
            val oldPassword: String = viewBind.etOld.text.toString().trim()
            val newPassword: String =viewBind.etNewPass.text.toString().trim()
            val okPassword: String = viewBind.etOkPass.text.toString().trim()
            if (ObjectUtils.isEmpty(oldPassword) || ObjectUtils.isEmpty(newPassword) || ObjectUtils.isEmpty(
                    okPassword
                )
            ) {
                viewBind.tvErrorMsg.text = "密码不能为空"
                return@setOnClickListener
            }
            if (!ObjectUtils.equals(
                    oldPassword,
                    muser?.get(0)?.Password
                )
            ) {
                viewBind.tvErrorMsg.text = "原密码不正确"
                return@setOnClickListener
            }
            if (!ObjectUtils.equals(newPassword, okPassword)) {
                viewBind.tvErrorMsg.text = "两次输入密码不一致"
                return@setOnClickListener
            }
            viewBind.tvErrorMsg.text =""
            if(0==ToolApplication.singletonTest(requireContext()).updatepwd(
                    newPassword, muser?.get(0)?.id
                )){
                ToastUtils.showShort("修改成功")

                if (isAdded)
                    if (act is NavActivity){
                        act.finish()
                    }else{
                        NavHostFragment.findNavController(this)
                            .navigateUp()
                    }

            }else{
                ToastUtils.showShort("修改失败")

            }


        }

       viewBind.etOld.onFocusChangeListener = this
       viewBind.etNewPass.onFocusChangeListener = this
       viewBind.etOkPass.onFocusChangeListener = this
    }

    override fun onFocusChange(v: View, hasFocus: Boolean) {
        if (hasFocus)
            viewModel.accountPop?.setAdjustInputMode(
                v.id,
                BasePopupWindow.FLAG_KEYBOARD_ANIMATE_ALIGN or BasePopupWindow.FLAG_KEYBOARD_FORCE_ADJUST or BasePopupWindow.FLAG_KEYBOARD_ALIGN_TO_VIEW
            )
    }


}