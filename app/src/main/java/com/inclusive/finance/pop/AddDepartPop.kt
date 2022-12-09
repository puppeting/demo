package com.inclusive.finance.pop


import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.fragment.app.DialogFragment
import com.blankj.utilcode.util.ScreenUtils
import com.blankj.utilcode.util.ToastUtils
import com.inclusive.finance.R
import com.inclusive.finance.app.ToolApplication
import com.inclusive.finance.databinding.PopDepartBinding
import com.inclusive.finance.utils.StatusBarUtil

class AddDepartPop(
    var mContext: Context?,
    val title: String,
    val id: String,
    var listener: (confirm: Boolean) -> Unit,
) :
    DialogFragment(), View.OnClickListener {
    var confirmStr = "确定"
    var cancelStr = "取消"
    fun setCancelText(str: String): AddDepartPop {
        cancelStr = str
        return this
    }

    fun setConfirmText(str: String): AddDepartPop {
        confirmStr = str
        return this
    }

    override fun onClick(v: View?) { //        KeyboardUtils.hideSoftInput(findViewById<EditText>(R.id.tv))
        when (v?.id) {
            R.id.tv_cancel -> {
                listener.invoke(false)
                dismiss()
            }
            R.id.tv_ok -> {
                if (id == "") {
                    var listBumen =
                        ToolApplication.singletonTest(requireContext())
                            .getListLaiYuan(dataBind?.tvTitle?.text.toString(),0)
                    if (listBumen != null) {
                        ToastUtils.showShort("已存在")
                    } else {
                        if (ToolApplication.singletonTest(requireContext()).insertLaiYuan(
                                dataBind?.tvTitle?.text.toString()) == 0
                        ) {
                            listener.invoke(true)
                            dismiss()
                        } else {
                            ToastUtils.showShort("操作失败")
                        }
                    }
                }else{
                    if (ToolApplication.singletonTest(requireContext()).updateLaiYuan(
                            id.toInt(),dataBind?.tvTitle?.text.toString()) == 0
                    ) {
                        listener.invoke(true)
                        dismiss()
                    } else {
                        ToastUtils.showShort("操作失败")
                    }
                }
//                 if(ToolApplication.singletonTest(requireContext()).updatepwd(
//                    dataBind?.tvTitle?.text.toString(),title.toInt()
//                )==0) {
//                     listener.invoke(true)
//                     dismiss()
//                 }else{
//                     ToastUtils.showShort("操作失败")
//                 }
            }
            else -> {
            }
        }
    }

    //    override fun onCreateContentView(): View {
    //        return createPopupById(R.layout.pop_confirm)
    //    }
    override fun onStart() {
        super.onStart()
        StatusBarUtil.immersive(dialog?.window) //setStyle(STYLE_NO_TITLE,R.style.MyDialog)
        val params = dialog?.window?.attributes
        dialog?.setCanceledOnTouchOutside(false)
        params?.width = ScreenUtils.getScreenWidth()
        params?.height = RelativeLayout.LayoutParams.MATCH_PARENT
        params?.gravity = Gravity.CENTER //高度自己定义
        dialog?.window?.setLayout(RelativeLayout.LayoutParams.MATCH_PARENT,
            RelativeLayout.LayoutParams.MATCH_PARENT)

    }

    lateinit var dataBind: PopDepartBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View { //        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT)) //设置背景为透明

        dataBind = PopDepartBinding.inflate(inflater, container, false)
        return dataBind.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        dataBind.tvTitle.setText(title)
        dataBind.tvOk.text = confirmStr
        dataBind.tvCancel.text = cancelStr
        dataBind.tvCancel.setOnClickListener(this)
        dataBind.tvOk.setOnClickListener(this)
    }
}