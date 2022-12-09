package com.inclusive.finance.ui.login

import android.util.Log
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.blankj.utilcode.util.ToastUtils
import com.inclusive.finance.R
import com.inclusive.finance.app.ToolApplication
import com.inclusive.finance.base.BaseActivity
import com.inclusive.finance.bean.model.LoginPasswordModel
import com.inclusive.finance.databinding.ActivityResgBinding
import com.inclusive.finance.interfaces.PresenterClick
import com.inclusive.finance.room.OrderDao
import com.inclusive.finance.room.User
import com.inclusive.finance.utils.StatusBarUtil

class ResgActivity : BaseActivity(), PresenterClick {

    override fun initToolbar() {
        StatusBarUtil.darkMode(this)
    }

    private   var processStatus: String = ""
    private var orderList: MutableList<User>? = mutableListOf()
    private var ordersDao: OrderDao? = null
    private lateinit var viewModel: LoginPasswordModel
    lateinit var viewBind: ActivityResgBinding
    override fun setInflateBinding() {
        viewModel = ViewModelProvider(this).get(LoginPasswordModel::class.java)
        viewModel.type = intent.getStringExtra("FROM")?.toInt()!!
        viewBind = DataBindingUtil.setContentView<ActivityResgBinding>(this, R.layout.activity_resg)
            .apply {
                data = viewModel
                presenterClick = this@ResgActivity
                lifecycleOwner = this@ResgActivity
            }
        if (viewModel.type == 2) {
            var list = ToolApplication.singletonTest(this)
                .getUserInfo(intent.getStringExtra("mid")!!.toInt())
            viewBind?.datanum = list[0]
        }
//        var listBumen =
//            ToolApplication.singletonTest(this).getListBuMenDate("")
//        val dataList = ArrayList<BaseTypeBean.Enum12>()
//        dataList.clear()
//        dataList.add(BaseTypeBean.Enum12().apply {
//            valueName = "全部"
//            keyName = ""
//        })
//        if(listBumen!=null&&listBumen?.size!!>0) {
//            listBumen.forEachIndexed { index, sysDepartment ->
//                dataList.add(BaseTypeBean.Enum12().apply {
//                    valueName = sysDepartment.departmentName
//                    keyName = sysDepartment.departmentName
//                })
//            }
//        }
//        viewBind?.userpart?.setOnClickListener {
//            DownPop(this, enums12 = dataList, checkedTextView = it as AppCompatCheckedTextView, isSingleChecked = true) { k, v, p ->
//                processStatus = if(v=="全部"){
//                    ""
//                }else{
//                    k
//                }
//            }.showPopupWindow(it)
//        }
    }

    override fun init() {
        ordersDao = ToolApplication.singletonTest(this)
//        if (ordersDao?.isDataExist() != true) {
//            ordersDao?.initTable()
//        }
//        orderList = ordersDao?.getAllDate()
    }

    override fun onClick(v: View?) {
        when (v) {
            viewBind?.llBack -> {
                finish()
            }
            viewBind?.btResg -> {

                var list = ToolApplication.singletonTest(this)
                    .getSelectDate2(viewBind?.userphone?.text?.toString(),
                        "",
                        "",
                        0)
                if (list!=null){
                    ToastUtils.showLong("登录名已存在")
                    return
                }
                var muter =
                    ToolApplication.singletonTest(this).allDate?.indexOfFirst { b -> b.UserName == viewBind.userphone.text.toString() }

                if (viewModel.type == 1) {
                    when {
                        viewBind.userphone.text.toString().isEmpty() -> {
                            ToastUtils.showLong("登录名不能为空")

                            return
                        }
                        viewBind.username.text.toString().isEmpty() -> {
                            ToastUtils.showLong("姓名不能为空")

                            return
                        }
                        viewBind.userpart.text.toString().isEmpty() -> {
                            ToastUtils.showLong("单位不能为空")
                            return
                        }
                        viewBind.userpwd.text.toString().isEmpty() -> {
                            ToastUtils.showLong("密码不能为空")
                            return
                        }
                        viewBind.useraginpwd.text.toString() != viewBind.userpwd.text.toString() -> {
                            ToastUtils.showLong("两次密码输入不一致")
                            return
                        }
                    }

                    if (muter != null) {
                        if (muter != -1) {
                            ToastUtils.showLong("登录名已存在")
                        } else {
                            ordersDao?.intserUser(
                                viewBind.userphone.text.toString(),
                                viewBind.username.text.toString(),
                                viewBind.userpart.text.toString(),
                                viewBind.userpwd.text.toString() ,"待审核")
                            ToastUtils.showLong("注册成功")
                            finish()
                        }
                    } else {
                        ordersDao?.intserUser(
                            viewBind.userphone.text.toString(),
                            viewBind.username.text.toString(),
                            viewBind.userpart.text.toString(),
                            viewBind.userpwd.text.toString() ,"待审核")
                        ToastUtils.showLong("注册成功")
                        finish()
                    }
                } else {
                    if(viewModel.type==3){
                        ToolApplication.singletonTest(this).intserUser(
                            viewBind?.userphone?.text.toString(),
                            viewBind?.username?.text.toString(),
                            viewBind?.userpart?.text.toString(),
                            viewBind?.userpwd?.text.toString(),"待审核"
                         )
                        ToastUtils.showShort("提交成功")
                    }else {
                        ToolApplication.singletonTest(this).updateUser(
                            viewBind?.userphone?.text.toString(),
                            viewBind?.username?.text.toString(),
                            viewBind?.userpart?.text.toString(),
                            intent.getStringExtra("mid")?.toInt()
                        )
                        ToastUtils.showShort("修改成功")
                    }
                    finish()
                }



            }
        }
    }
}