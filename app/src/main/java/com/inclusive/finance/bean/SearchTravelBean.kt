package com.inclusive.finance.bean

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import java.util.*

/**
 * Created by 史忠文
 * on 2017/6/6.
 */
@Entity
open class SearchTravelBean(var name: String = "", var date: Date? = null, @Id var id: Long = 0) {
    var code:String ?=null
    var image:String ?=null
    var pCode:String ?=null
    var haveChild:String ?=null
    var isFavorite=false
    var list:MutableList<SearchTravelBean>?=null
    companion object {
        val primaryKey = "searchContent"
        val DateKey = "date"
    }
}
