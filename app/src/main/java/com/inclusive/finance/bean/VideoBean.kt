package com.inclusive.finance.bean

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.inclusive.finance.BR

open class VideoBean : BaseObservable() {
    var fileName: String? = ""
    var filePath: String? = ""
    var fileTime: String? = ""
    var imgUrl: String? = ""
    var id: String? = ""
    var createDate: String? = ""

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
    @Bindable
    var editable = false
        set(value) {
            field = value
            notifyPropertyChanged(BR.editable)
        }

    override fun toString(): String {
        return filePath ?: ""
    }
}