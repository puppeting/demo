package com.inclusive.finance.bean.model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.blankj.utilcode.util.SPUtils
import com.inclusive.finance.config.Constants
import com.inclusive.finance.room.User

class LoginPasswordModel: ViewModel() {
    var type=1
    var databean:MutableLiveData<User>?=MutableLiveData()
    var isGestureLogin: MutableLiveData<Boolean> = MutableLiveData(
        SPUtils.getInstance().getBoolean(
            Constants.SPUtilsConfig.ISGESTURELOCK_KEY, false
        )
    )
    var isLoginGesture: MutableLiveData<Boolean> = MutableLiveData(
        SPUtils.getInstance().getBoolean(
            Constants.SPUtilsConfig.ISGESTURELOCK_KEY, false
        )
    )
}