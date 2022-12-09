package com.inclusive.finance.bean

open class MainMenuBean {
    var title :String? =""
    var list:ArrayList<MenuBean> ?=null

    open class MenuBean {
        var title :String? =""
        var img:String? =""
        var className:String?=""
    }
}