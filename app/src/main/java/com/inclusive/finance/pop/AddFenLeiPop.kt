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
import com.inclusive.finance.databinding.PopFenleiBinding
import com.inclusive.finance.room.StandardSystem
import com.inclusive.finance.utils.StatusBarUtil

class AddFenLeiPop(
    var mContext: Context?,
    var type:String,
    val title: String,
    val id: String,
    var listener: (confirm: Boolean) -> Unit,
) :
    DialogFragment(), View.OnClickListener {
    private var listBumen: MutableList<StandardSystem>? = arrayListOf()
    private   var msourname2: String = ""
    private   var msourname1: String = ""

    var confirmStr = "确定"
    var cancelStr = "取消"
    fun setCancelText(str: String): AddFenLeiPop {
        cancelStr = str
        return this
    }

    fun setConfirmText(str: String): AddFenLeiPop {
        confirmStr = str
        return this
    }
    var msourname=""
    override fun onClick(v: View?) { //        KeyboardUtils.hideSoftInput(findViewById<EditText>(R.id.tv))
        when (v?.id) {
            R.id.tv_cancel -> {
                listener.invoke(false)
                dismiss()
            }
            R.id.tv_ok -> {

                if (id == "") {
                    if(type=="1"){
                          listBumen =
                            ToolApplication.singletonTest(requireContext()).getListBiaoZhunTiXi(dataBind?.tvTitle?.text.toString(),"")
                    }else if(type=="2"){

                          listBumen =
                            ToolApplication.singletonTest(requireContext()).getListBiaoZhunTiXi("",dataBind?.tvTitle?.text.toString())
                    }

                    if (listBumen != null) {
                        ToastUtils.showShort("已存在")
                    } else {
                        if(type=="1"){
                            msourname="StandardName"
                            if (ToolApplication.singletonTest(requireContext()).insertBiaoZhunTiXi(
                                    dataBind?.tvTitle?.text.toString(),msourname) == 0
                            ) {
                                listener.invoke(true)
                                dismiss()
                            } else {
                                ToastUtils.showShort("操作失败")
                            }
                        }else if(type=="2"){
                            var mu =
                                ToolApplication.singletonTest(requireContext()).getListBiaoZhunTiXi(title,"")
                            msourname="StandardsSecondName"
                            var mm =""
                            if (mu.get(0).StandardsSecondName!=null) {
                                  mm =mu.get(0).StandardsSecondName + "" + dataBind?.tvTitle?.text.toString() + ","
                            }else{
                                mm=dataBind?.tvTitle?.text.toString() + ","
                            }
                            if (ToolApplication.singletonTest(requireContext()).updateBiaoZhunTiXi(
                                    mm,msourname,title) == 0
                            ) {
                                listener.invoke(true)
                                dismiss()
                            } else {
                                ToastUtils.showShort("操作失败")
                            }
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

            }
            else -> {
            }
        }
    }

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

    lateinit var dataBind: PopFenleiBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View { //        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT)) //设置背景为透明
        dataBind = PopFenleiBinding.inflate(inflater, container, false)
         if(type=="1")
             dataBind?.tvBumen?.text="一级分类"
        else if(type=="2"){
             dataBind?.tvBumen?.text="二级分类"
         }

        return dataBind.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        dataBind.tvTitle.setText(title)
        dataBind.tvOk.text = confirmStr
        dataBind.tvCancel.text = cancelStr
        dataBind.tvCancel.setOnClickListener(this)
        dataBind.tvOk.setOnClickListener(this)
    }
}