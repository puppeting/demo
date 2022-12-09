package com.inclusive.finance.bean.model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.blankj.utilcode.util.SPUtils
import com.inclusive.finance.bean.KehuBean
import com.inclusive.finance.bean.User
import com.inclusive.finance.config.Constants
import com.inclusive.finance.pop.AccountPop

class MainActivityModel: ViewModel() {
    var userInfo: MutableLiveData<User> = MutableLiveData(User())
    var accountPop: AccountPop?=null
    var idCardNum: String?=null
    var kehuBean: MutableLiveData<KehuBean> = MutableLiveData(KehuBean())
    val isGestureLogin: MutableLiveData<Boolean> by lazy {MutableLiveData(
        SPUtils.getInstance().getBoolean(
            Constants.SPUtilsConfig.ISGESTURELOCK_KEY, false
        ))
    }

}