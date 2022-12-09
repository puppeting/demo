package com.inclusive.finance.bean

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.google.gson.JsonObject
import com.inclusive.finance.BR
import java.util.*

data class BaseListBean(
    val equally:Boolean,//是否平分列表宽度
    val title:String,//标题
    var list: ArrayList<JsonObject>,
    val titleList: ArrayList<ListTitle>) {
    var actionTypes: String = "0,1,2,3,4"//增/删/改/查/pdf    localhost/jeecg/user/get?id=
    var getUrl=""
    var saveUrl=""
    var deleteUrl=""
    var pdfUrl=""
 }


open class ListBean(
    val bean: JsonObject
) : BaseObservable() {
    @Bindable
    var checked = false
        set(value) {
            field = value
            notifyPropertyChanged(BR.checked)
        }
}

open class KVBean : KeyAndValueBean() {
    private val keyName = ""
    private val valueName = ""
    var checked = false
    override fun absKey() = keyName

    override fun absValue() = valueName

    override fun absCheck() = checked
}

open  class ListTitle(
    val key: String,
    val ems: Int,
    var enums12: ArrayList<BaseTypeBean.Enum12>? = null, //枚举值 。若有下拉
    val value: String
)
