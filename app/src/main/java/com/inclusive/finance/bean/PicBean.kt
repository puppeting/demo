package com.inclusive.finance.bean

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.inclusive.finance.BR

open class PicBean : BaseObservable() {
    var fileName: String? = ""
    var filePath: String? = ""
//    var picUrl: String? = ""
//        set(value) {
//            field = value
//            filePath = value
//        }
//    var id: String? = "" //数量

    @Bindable
    var checked = false
        set(value) {
            field = value
            notifyPropertyChanged(BR.checked)
        }

    override fun toString(): String {
        return filePath ?: ""
    }
}