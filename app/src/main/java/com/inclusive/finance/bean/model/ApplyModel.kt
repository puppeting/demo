package com.inclusive.finance.bean.model

import androidx.lifecycle.ViewModel
import com.inclusive.finance.bean.ApplyCheckBean

open class ApplyModel : ViewModel() {
    var applyCheckBean: ApplyCheckBean? = ApplyCheckBean()
    var jsonObject: String? = null //某个被选中的条目
    var temporaryJsonObject: String? = null //风险探测使用
    var creditId: String? = null
    var keyId: String? = null
    var dhId: String? = null
    var zfId: String? = null
    var ysxId: String? = null
    var seeOnly: Boolean? = false
    var businessType = BUSINESS_TYPE_APPLY
    var title: String? = "" //同一页面不同内容，刷新时使用
    var idno: String? = null
    var jgno: String? = null

    companion object {
        const val BUSINESS_TYPE_APPLY = 10 //系统
        const val BUSINESS_TYPE_SHOUCANG = 20 //收藏
        const val BUSINESS_TYPE_LISHI = 30 //历史
        const val BUSINESS_TYPE_WENJIAN = 40 //文件

        const val BUSINESS_TYPE_INVESTIGATE = 20 //授信调查
        const val BUSINESS_TYPE_PICADD = 21 //影像补录
        const val BUSINESS_TYPE_INVESTIGATE_SIMPLEMODE = 22 //授信模型-简化
        const val BUSINESS_TYPE_ZXSP = 30 //征信审批
        const val BUSINESS_TYPE_DH_ZXSP = 31 //贷后征信审批
        const val BUSINESS_TYPE_SXSP = 32 //授信审批
        const val BUSINESS_TYPE_QPLC = 33 //签批流程
        const val BUSINESS_TYPE_ZXFHQZ = 40 //征信复核签字
        const val BUSINESS_TYPE_JNJ_CJ_PERSONAL = 50 //季年检采集-个人
        const val BUSINESS_TYPE_JNJ_YX = 51 //季年检 用信
        const val BUSINESS_TYPE_JNJ_CJ_COMPANY = 60//季年检采集-企业
        const val BUSINESS_TYPE_JNJ_JC_OFF_SITE_PERSONAL = 70 //季年检检查-非现场-个人
        const val BUSINESS_TYPE_JNJ_JC_ON_SITE_PERSONAL = 80 //季年检检查-现场-个人
        const val BUSINESS_TYPE_JNJ_JC_ON_SITE_COMPANY = 90 //季年检检查-现场-企业
        const val BUSINESS_TYPE_SJ = 100 //首检
        const val BUSINESS_TYPE_SJ_PERSONAL = 101 //首检-个人
        const val BUSINESS_TYPE_SJ_COMPANY = 110 //首检-企业
        const val BUSINESS_TYPE_RC = 120 //日常检查
        const val BUSINESS_TYPE_RC_OFF_SITE_PERSONAL = 121 //日常检查非现场-个人
        const val BUSINESS_TYPE_RC_ON_SITE_PERSONAL = 130 //日常检查现场-企业
        const val BUSINESS_TYPE_RC_ON_SITE_COMPANY = 140 //日常检查现场-企业
        const val BUSINESS_TYPE_VISIT = 150 //走访
        const val BUSINESS_TYPE_VISIT_NEW = 160 //走访新增
        const val BUSINESS_TYPE_VISIT_EDIT = 170 //走访修改
        const val BUSINESS_TYPE_PRECREDIT = 180 //预授信
        const val BUSINESS_TYPE_VISIT_APPROVAL = 190 //走访签批
        const val BUSINESS_TYPE_CREDIT_MANAGER = 200 //用信管理
        const val BUSINESS_TYPE_CREDIT_MANAGER_LGFK = 201 //用信管理_离柜放款
        const val BUSINESS_TYPE_CREDIT_MANAGER_ZXGL = 202 //用信管理_征信管理
        const val BUSINESS_TYPE_QRCODE = 210 //二维码列表
        const val BUSINESS_TYPE_SHOLI = 220 //受理列表
        const val BUSINESS_TYPE_INFORMATION_OFFICER = 230 //信息员列表
        const val BUSINESS_TYPE_QUESTIONNAIRE = 240 //问卷调查
        const val BUSINESS_TYPE_CREDIT_REVIEW = 250 //授信评议
        const val BUSINESS_TYPE_COMPARISON_OF_QUOTAS = 260 //额度对比
        const val BUSINESS_TYPE_SUNSHINE_APPLY = 270 //阳光用信申请
        const val BUSINESS_TYPE_SUNSHINE_QPLC = 280  //阳光用信签批流程
        const val BUSINESS_TYPE_SUNSHINE_ZXSP = 290  //阳光用信征信审批
        const val BUSINESS_TYPE_INFORMATION_FENGXIANGUANLI = 300 //风险信息管理
        const val BUSINESS_TYPE_CUSTOMER_POOL = 301 //客户信息池
        const val BUSINESS_TYPE_POC_KHXXGL = 302 //客户信息管理
        const val BUSINESS_TYPE_POC_KHXXGL_APPLY = 303 //客户信息管理-新增授信
        const val BUSINESS_TYPE_POC_SXXX = 310 //授信项下
        const val BUSINESS_TYPE_POC_HTDJ = 320 // 合同登记
        const val BUSINESS_TYPE_POC_FDSQ = 330 //放贷申请

    }
}