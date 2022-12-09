package com.inclusive.finance.bean

import androidx.databinding.Bindable
import com.inclusive.finance.BR
import java.io.Serializable

open class KeHuInfoBean {
    var qyqk: MutableList<QyqkDTO>? = null
    var jbxx: Jbxx? = null
    var jtgx: MutableList<JtgxDTO>? = null


    open class Jbxx :KeyAndValueBean(){

        override fun absKey(): String {
            if(custName!= null&&custName?.length==3)

            return "**"+custName?.substring(custName?.length!!-1,custName?.length!!)
            else if(custName!= null&&custName?.length==2)

                return "*"+custName?.substring(custName?.length!!-1,custName?.length!!)
            else if(custName!= null&&custName?.length!!>=4)

                return "***"+custName?.substring(custName?.length!!-1,custName?.length!!)
            else
                return ""
        }

        override fun absValue(): String {
            return idenNo?.substring(0,6)+"***********"+idenNo?.substring(17,18)
        }

        override fun absCheck(): Boolean {
            return true
        }
        
        var custName: String? = null
        var khxj: Float? = null
        var houseNumber: String? = null

        var wjfl: String? = null
        var idenNo: String? = null
        var sex: String? = null
        var zgxl: String? = null
        var csrq: String? = null
        var lxdh: String? = null
        var mz: String? = null
        var zy: String? = null
        var hy: String? = null
        var zqbs: String? = null
        var bjyqcs: String? = null
        var qxjlcs: String? = null
        var dwdbwjfl: String? = null
        var dwdbhs: String? = null
        var bnwyqdkbs: String? = null
        var bnwyqdkdbbs: String? = null

        var sfwglr: String? = null
        var sfwgd: String? = null
        var sfw41yczh: String? = null

        /** 个人网银活跃标志 */
        var grwyhybz: String? = null

        var sfktgrwy: String? = null
            get() {
                return if (field == "是" && grwyhybz == "是")
                    "开通-活跃"
                else if (field == "是" && grwyhybz == "否")
                    "开通"
                else
                    " "
            }

        /** 贷记卡活跃标志 */
        var djkhybz: String? = null

        var sfktdjk: String? = null

            get() {
                return if (field == "是" && djkhybz == "是")
                    "开通-活跃"
                else if (field == "是" && djkhybz == "否")
                    "开通"
                else
                    ""
            }
        /** 是否开通微信 */
        var sfktwx: String? = null

            get() {
                return if (field == "是" && wxhybz == "是")
                    "开通-活跃"
                else if (field == "是" && wxhybz == "否")
                    "开通"
                else
                    ""
            }
        var sdshhybz: String? = null

        /** 是否开通收单商户 */
        var sfktsdsh: String? = null

            get() {
                return if (field == "是" && sdshhybz == "是")
                    "开通-活跃"
                else if (field == "是" && sdshhybz == "否")
                    "开通"
                else
                    ""
            }
        var sbkhybz: String?=null
        /** 是否开通社保卡*/
        var sfktsbk: String? = null

            get() {
                return if (field == "是" && sbkhybz == "是")
                    "开通-活跃"
                else if (field == "是" && sbkhybz == "否")
                    "开通"
                else
                    ""
            }
        var etchybz: String?=null
        /** 是否开通etc*/
        var sfktetc: String? = null
            get() {
                return if (field == "是" && etchybz == "是")
                    "开通-活跃"
                else if (field == "是" && etchybz == "否")
                    "开通"
                else
                    ""
            }

        var elywhybz: String?=null

        /** 是否开通一路有我*/
        var sfktelyw: String? = null
            get() {
                return if (field == "是" && elywhybz == "是")
                    "开通-活跃"
                else if (field == "是" && elywhybz == "否")
                    "开通"
                else
                    ""
            }
        var cfthybz: String?=null

        /** 是否开通财付通*/
        var sfktcft: String? = null
            get() {
                return if (field == "是" && cfthybz == "是")
                    "开通-活跃"
                else if (field == "是" && cfthybz == "否")
                    "开通"
                else
                    ""
            }
        var zfbhybz: String?=null
        /** 是否开通支付宝*/
        var sfktzfb: String? = null
            get() {
                return if (field == "是" && zfbhybz == "是")
                    "开通-活跃"
                else if (field == "是" && zfbhybz == "否")
                    "开通"
                else
                    ""
            }
        var bfbhybz: String?=null
        /** 是否开通财付通*/
        var sfktbfb: String? = null
            get() {
                return if (field == "是" && bfbhybz == "是")
                    "开通-活跃"
                else if (field == "是" && bfbhybz == "否")
                    "开通"
                else
                    ""
            }
        var jdhybz: String?=null
        /** 是否开通京东活跃标志*/
        var sfktjd: String? = null
            get() {
                return if (field == "是" && jdhybz == "是")
                    "开通-活跃"
                else if (field == "是" && jdhybz == "否")
                    "开通"
                else
                    ""
            }
        var tyzfhybz: String?=null
        /** 是否开通京东活跃标志*/
        var sfkttyzf: String? = null
            get() {
                return if (field == "是" && tyzfhybz == "是")
                    "开通-活跃"
                else if (field == "是" && tyzfhybz == "否")
                    "开通"
                else
                    ""
            }
        var snyfbhybz: String?=null
        /** 是否开通苏宁易付宝*/
        var sfktsnyfb: String? = null
            get() {
                return if (field == "是" && snyfbhybz == "是")
                    "开通-活跃"
                else if (field == "是" && snyfbhybz == "否")
                    "开通"
                else
                    ""
            }
        var ybzfhybz: String?=null
        /** 是否开通苏宁易付宝*/
        var sfktybzf: String? = null
            get() {
                return if (field == "是" && ybzfhybz == "是")
                    "开通-活跃"
                else if (field == "是" && ybzfhybz == "否")
                    "开通"
                else
                    ""
            }
        var sjyhhybz: String?=null
        /** 是否开通手机银行*/
        var sfktsjyh: String? = null
            get() {
                return if (field == "是" && sjyhhybz == "是")
                    "开通-活跃"
                else if (field == "是" && sjyhhybz == "否")
                    "开通"
                else
                    ""
            }
        var rhposhybz: String?=null
        /** 是否开通手机银行*/
        var sfktrhpos: String? = null
            get() {
                return if (field == "是" && rhposhybz == "是")
                    "开通-活跃"
                else if (field == "是" && rhposhybz == "否")
                    "开通"
                else
                    ""
            }
        var ylzxhybz: String?=null
        /** 是否开通手机银行*/
        var sfktylzx: String? = null
            get() {
                return if (field == "是" && ylzxhybz == "是")
                    "开通-活跃"
                else if (field == "是" && ylzxhybz == "否")
                    "开通"
                else
                    ""
            }
//      var ylzxhybz: String?=null
        /** 是否开通手机银行*/
        var sfktysf: String? = null
            get() {
                return if (field == "是" )
                    "开通-活跃"
                else if (field == "是" )
                    "开通"
                else
                    ""
            }
         var mtzfhybz: String?=null
        /** 是否开通美团*/
        var sfktmtzf: String? = null
            get() {
                return if (field == "是" && mtzfhybz == "是")
                    "开通-活跃"
                else if (field == "是" && mtzfhybz == "否")
                    "开通"
                else
                    ""
            }
        var age: String? = null
            get()=field+"岁"
        var hjdz: String? = null
        var czdz: String? = null
        var jydz: String? = null
        var szzNo: String? = null
        var jxhjcs: String? = null
        var szz: String? = null
        var szcNo: String? = null
        var szc: String? = null
        var szxq: String? = null
        var orgNo: String? = null
        var orgName: String? = null
        var jtrk: String? = null
            get()=field+"口之家"
        var jtxjzc: String? = null
            get() {
                return if (field == "" || field==null)
                    "0元"
                else
                    field+"元"
            }
        var jtfz: String? = null
            get() {
                return if (field == "" || field==null)
                    "0元"
                else
                    field+"元"
            }
        var jtysx: String? = null
            get() {
                return if (field == "" || field==null)
                    "0元"
                else
                    field+"元"
            }
        var qtjtcyyqqk: String? = null
        var qtjtcydwdbqk: String? = null

        var zzhqkhsj: String? = null
        var hqzhxhsj: String? = null
        var zzdqkhsj: String? = null
        var dqzhxhsj: String? = null
        var zjybdqdqsj: String? = null
        var dydqckdqbs: String? = null
        var dydqckdqje: String? = null
        var dqckye: String? = null
        get() {
            return if (field == "" || field==null)
                "0元"
            else
                field+"元"
        }
        var dqhqckye: String? = null
        var zrckye: String? = null

        var ncckye: String? = null
        var nchqckye: String? = null
        var ncdqckye: String? = null
        var ckdqjzr: String? = null
            get() {
                return if (field == "" || field==null)
                    "0元"
                else
                    field+"元"
            }
        var ckdqjnc: String? = null
            get() {
                return if (field == "" || field==null)
                    "0元"
                else
                    field+"元"
            }
        var jbnrj: String? = null
        var jbnhqrj: String? = null
        var jbndqrj: String? = null
        var jbnjybs: String? = null
        var jbnjyje: String? = null
        var jynrj: String? = null
            get() {
                return if (field == "" || field==null)
                    "0元"
                else
                    field+"元"
            }
        var jynhqrj: String? = null
        var jyndqrj: String? = null
        var jynjybs: String? = null
        var dqdqckye: String? = null
        var jynjyje: String? = null
        var dndfje: String? = null
        var ckzzhzrq: String? = null
        var ckhysj: String? = null
        var ckhyjg: String? = null
        var ghjg: String? = null
        var ckhyjgmc: String? = null
        var ghjgmc: String? = null
        var ygysxje: String? = null
        var ygysxksrq: String? = null
        var ygysxjzrq: String? = null
        var dqyxzhsxje: String? = null

        var dqyxzhsxksrq: String? = null
        var dqyxzhsxjzrq: String? = null
        var dqyxlssxje: String? = null
        var dqyxlssxye: String? = null
        var dqzdkje: String? = null
        var dqzdkye: String? = null
            get() {
                return if (field == "" || field==null)
                    "0元"
                else
                    field+"元"
            }
        var zrzdkje: String? = null
        var zrzdkye: String? = null
        var dkzzhzrq: String? = null

        var bfbdnjybs: String? = null
        var bfbdnjyje: String? = null
        var bfbdyjybs: String? = null
        var bfbdyjyje: String? = null
        var bfbgbsj: String? = null
         var bfbktsj: String? = null
        var bfbsypl: String? = null
        var bnbldkye: String? = null
        var bwbldkye: String? = null
        var cftdnjybs: String? = null
        var cftdnjyje: String? = null
        var cftdyjybs: String? = null
        var cftdyjyje: String? = null
        var cftgbsj: String? = null
         var cftktsj: String? = null
        var cftsypl: String? = null

        var djkdnjybs: String? = null
        var djkdnjyje: String? = null
        var djkdyjybs: String? = null
        var djkdyjyje: String? = null
        var djkfqje: String? = null
        var djkfqqs: String? = null
        var djkgbsj: String? = null

        var djkktsj: String? = null
        var djksypl: String? = null
        var djktzje: String? = null
        var djkxyed: String? = null
            get() {
                return if (field == "" || field==null)
                    "0元"
                else
                    field+"元"
            }
        var djkxyye: String? = null
        var djkyqje: String? = null
        var djkyqqs: String? = null
        var djkzhzt: String? = null
        var dkdqjnc: String? = null
            get() {
                return if (field == "" || field==null)
                    "0元"
                else
                    field+"元"
            }
        var dkdqjzr: String? = null
            get() {
                return if (field == "" || field==null)
                    "0元"
                else
                    field+"元"
            }
        var dkghjg: String? = null
        var dkghjgmc: String? = null
        var dkghr: String? = null
        var dkghrmc: String? = null

        var dqajdkbs: String? = null
        var dqajdkje: String? = null
        var dqajdkye: String? = null

        var dqfajdkbs: String? = null
        var dqfajdkje: String? = null
        var dqfajdkye: String? = null

        var dwdbblje: String? = null
        var dwdbblye: String? = null
        var dwdbje: String? = null
        var dwdbye: String? = null

        var elywdnjybs: String? = null
        var elywdnjyje: String? = null
        var elywdyjybs: String? = null
        var elywdyjyje: String? = null
        var elywgbsj: String? = null
         var elywktsj: String? = null
        var elywsypl: String? = null
        var etcbdkzl: String? = null
        var etcdnjybs: String? = null
        var etcdnjyje: String? = null
        var etcdyjybs: String? = null
        var etcdyjyje: String? = null
        var etcgbsj: String? = null
         var etcktsj: String? = null
        var etcsypl: String? = null

        var glpoyxsxje: String? = null
        var glqyyxsxje: String? = null

        var jddnjybs: String? = null
        var jddnjyje: String? = null
        var jddyjybs: String? = null
        var jddyjyje: String? = null
        var jdgbsj: String? = null
         var jdktsj: String? = null
        var jdsypl: String? = null

        var mtzfdnjybs: String? = null
        var mtzfdnjyje: String? = null
        var mtzfdyjybs: String? = null
        var mtzfdyjyje: String? = null
        var mtzfgbsj: String? = null
         var mtzfktsj: String? = null
        var mtzfsypl: String? = null

        var ncfajdkbs: String? = null
        var ncfajdkje: String? = null
        var ncfajdkye: String? = null
        var nczdkje: String? = null
        var nczdkye: String? = null

        var s30yqxye: String? = null
        var s60yqxye: String? = null
        var sbkdnjybs: String? = null
        var sbkdnjyje: String? = null
        var sbkdyjybs: String? = null
        var sbkdyjyje: String? = null
        var sbkgbsj: String? = null
         var sbkktsj: String? = null
        var sbksypl: String? = null
        var sdshdjkdnsxf: String? = null
        var sdshdjkdysxf: String? = null
        var sdshdnjybs: String? = null
        var sdshdnjyje: String? = null
        var sdshdnkcdzj: String? = null
        var sdshdqkye: String? = null
        var sdshdyjybs: String? = null
        var sdshdyjyje: String? = null
        var sdshgbsj: String? = null
         var sdshjjkdnsxf: String? = null
        var sdshjjkdysxf: String? = null
        var sdshkhyf: String? = null
        var sdshktsj: String? = null
        var sdshsypl: String? = null


            var snyfbdyjybs: String? = null
        var snyfbdyjyje: String? = null
         var tjcp: String? = null
        var cpph: String? = null
        var cdph: String? = null



        var tyzfdnjybs: String? = null
        var tyzfdnjyje: String? = null
        var tyzfdyjybs: String? = null
        var tyzfdyjyje: String? = null
        var tyzfgbsj: String? = null
         var tyzfktsj: String? = null
        var tyzfsypl: String? = null
        var wxdnjybs: String? = null
        var wxdnjyje: String? = null
        var wxdyjybs: String? = null
        var wxdyjyje: String? = null
        var wxgbsj: String? = null
        var wxhybz: String? = null
        var wxktsj: String? = null
        var wxsypl: String? = null

        var zfbdnjybs: String? = null
        var zfbdnjyje: String? = null
        var zfbdyjybs: String? = null
        var zfbdyjyje: String? = null
        var zfbgbsj: String? = null
         var zfbktsj: String? = null
        var zfbsypl: String? = null

        var zrfajdkbs: String? = null
        var zrfajdkje: String? = null
        var zrfajdkye: String? = null


//                    ("nczdkje")
//                    public Object nczdkje;
//                    ("nczdkye")
//                    public Object nczdkye;
//                    ("dkdqjzr")
//                    public Object dkdqjzr;
//                    ("dkdqjnc")
//                    public Object dkdqjnc;
//                    ("dqajdkbs")
//                    public Object dqajdkbs;
//                    ("dqajdkje")
//                    public Object dqajdkje;
//                    ("dqajdkye")
//                    public Object dqajdkye;
//                    ("dqfajdkbs")
//                    public Object dqfajdkbs;
//                    ("dqfajdkje")
//                    public Object dqfajdkje;
//                    ("dqfajdkye")
//                    public Object dqfajdkye;
//                    ("zrfajdkbs")
//                    public Object zrfajdkbs;
//                    ("zrfajdkje")
//                    public Object zrfajdkje;
//                    ("zrfajdkye")
//                    public Object zrfajdkye;
//                    ("ncfajdkbs")
//                    public Object ncfajdkbs;
//                    ("ncfajdkje")
//                    public Object ncfajdkje;
//                    ("ncfajdkye")
//                    public Object ncfajdkye;
//                    ("s30yqxye")
//                    public Object s30yqxye;
//                    ("s60yqxye")
//                    public Object s60yqxye;
//                    ("bnbldkye")
//                    public Object bnbldkye;
//                    ("bwbldkye")
//                    public Object bwbldkye;
//                    ("dwdbje")
//                    public Object dwdbje;
//                    ("dwdbye")
//                    public Object dwdbye;
//                    ("dwdbblje")
//                    public Object dwdbblje;
//                    ("dwdbblye")
//                    public Object dwdbblye;
//                    ("glqyyxsxje")
//                    public Object glqyyxsxje;
//                    ("glpoyxsxje")
//                    public Object glpoyxsxje;
//                    ("dkzzhzrq")
//                    public Object dkzzhzrq;
//                    ("dkghr")
//                    public Object dkghr;
//                    ("dkghrmc")
//                    public Object dkghrmc;
//                    ("dkghjg")
//                    public Object dkghjg;
//                    ("dkghjgmc")
//                    public Object dkghjgmc;
//                    ("sfktdjk")
//                    public String sfktdjk;
//                    ("djkhybz")
//                    public Object djkhybz;
//                    ("djkxyed")
//                    public Object djkxyed;
//                    ("djktzje")
//                    public Object djktzje;
//                    ("djkxyye")
//                    public Object djkxyye;
//                    ("djkfqje")
//                    public Object djkfqje;
//                    ("djkfqqs")
//                    public Object djkfqqs;
//                    ("djkyqje")
//                    public Object djkyqje;
//                    ("djkyqqs")
//                    public Object djkyqqs;
//                    ("djkdyjybs")
//                    public Object djkdyjybs;
//                    ("djkdyjyje")
//                    public Object djkdyjyje;
//                    ("djkdnjybs")
//                    public Object djkdnjybs;
//                    ("djkdnjyje")
//                    public Object djkdnjyje;
//                    ("djksypl")
//                    public Object djksypl;
//                    ("djkzhzt")
//                    public Object djkzhzt;
//                    ("djkktsj")
//                    public Object djkktsj;
//                    ("djkgbsj")
//                    public Object djkgbsj;
//                    ("sfktwx")
//                    public String sfktwx;
//                    ("wxhybz")
//                    public Object wxhybz;
//                    ("wxdyjybs")
//                    public Object wxdyjybs;
//                    ("wxdyjyje")
//                    public Object wxdyjyje;
//                    ("wxdnjybs")
//                    public Object wxdnjybs;
//                    ("wxdnjyje")
//                    public Object wxdnjyje;
//                    ("wxsypl")
//                    public Object wxsypl;
//                    ("wxktsj")
//                    public Object wxktsj;
//                    ("wxgbsj")
//                    public Object wxgbsj;
//                    ("sfktsdsh")
//                    public String sfktsdsh;
//                    ("sdshhybz")
//                    public Object sdshhybz;
//                    ("sdshkhyf")
//                    public Object sdshkhyf;
//                    ("sdshdqkye")
//                    public Object sdshdqkye;
//                    ("sdshdnkcdzj")
//                    public Object sdshdnkcdzj;
//                    ("sdshjjkdysxf")
//                    public Object sdshjjkdysxf;
//                    ("sdshdjkdysxf")
//                    public Object sdshdjkdysxf;
//                    ("sdshjjkdnsxf")
//                    public Object sdshjjkdnsxf;
//                    ("sdshdjkdnsxf")
//                    public Object sdshdjkdnsxf;
//                    ("sdshdyjybs")
//                    public Object sdshdyjybs;
//                    ("sdshdyjyje")
//                    public Object sdshdyjyje;
//                    ("sdshdnjybs")
//                    public Object sdshdnjybs;
//                    ("sdshdnjyje")
//                    public Object sdshdnjyje;
//                    ("sdshsypl")
//                    public Object sdshsypl;
//                    ("sdshktsj")
//                    public Object sdshktsj;
//                    ("sdshgbsj")
//                    public Object sdshgbsj;
//                    ("sfktsbk")
//                    public String sfktsbk;
//                    ("sbkhybz")
//                    public Object sbkhybz;
//                    ("sbkdyjybs")
//                    public Object sbkdyjybs;
//                    ("sbkdyjyje")
//                    public Object sbkdyjyje;
//                    ("sbkdnjybs")
//                    public Object sbkdnjybs;
//                    ("sbkdnjyje")
//                    public Object sbkdnjyje;
//                    ("sbksypl")
//                    public Object sbksypl;
//                    ("sbkktsj")
//                    public Object sbkktsj;
//                    ("sbkgbsj")
//                    public Object sbkgbsj;
//                    ("sfktetc")
//                    public String sfktetc;
//                    ("etcbdkzl")
//                    public Object etcbdkzl;
//                    ("etchybz")
//                    public Object etchybz;
//                    ("etcdyjybs")
//                    public Object etcdyjybs;
//                    ("etcdyjyje")
//                    public Object etcdyjyje;
//                    ("etcdnjybs")
//                    public Object etcdnjybs;
//                    ("etcdnjyje")
//                    public Object etcdnjyje;
//                    ("etcsypl")
//                    public Object etcsypl;
//                    ("etcktsj")
//                    public Object etcktsj;
//                    ("etcgbsj")
//                    public Object etcgbsj;
//                    ("sfktelyw")
//                    public String sfktelyw;
//                    ("elywhybz")
//                    public Object elywhybz;
//                    ("elywdyjybs")
//                    public Object elywdyjybs;
//                    ("elywdyjyje")
//                    public Object elywdyjyje;
//                    ("elywdnjybs")
//                    public Object elywdnjybs;
//                    ("elywdnjyje")
//                    public Object elywdnjyje;
//                    ("elywsypl")
//                    public Object elywsypl;
//                    ("elywktsj")
//                    public Object elywktsj;
//                    ("elywgbsj")
//                    public Object elywgbsj;
//                    ("sfktcft")
//                    public String sfktcft;
//                    ("cfthybz")
//                    public Object cfthybz;
//                    ("cftdyjybs")
//                    public Object cftdyjybs;
//                    ("cftdyjyje")
//                    public Object cftdyjyje;
//                    ("cftdnjybs")
//                    public Object cftdnjybs;
//                    ("cftdnjyje")
//                    public Object cftdnjyje;
//                    ("cftsypl")
//                    public Object cftsypl;
//                    ("cftktsj")
//                    public String cftktsj;
//                    ("cftgbsj")
//                    public Object cftgbsj;
//                    ("sfktzfb")
//                    public String sfktzfb;
//                    ("zfbhybz")
//                    public Object zfbhybz;
//                    ("zfbdyjybs")
//                    public Object zfbdyjybs;
//                    ("zfbdyjyje")
//                    public Object zfbdyjyje;
//                    ("zfbdnjybs")
//                    public Object zfbdnjybs;
//                    ("zfbdnjyje")
//                    public Object zfbdnjyje;
//                    ("zfbsypl")
//                    public Object zfbsypl;
//                    ("zfbktsj")
//                    public String zfbktsj;
//                    ("zfbgbsj")
//                    public Object zfbgbsj;
//                    ("sfktbfb")
//                    public String sfktbfb;
//                    ("bfbhybz")
//                    public Object bfbhybz;
//                    ("bfbdyjybs")
//                    public Object bfbdyjybs;
//                    ("bfbdyjyje")
//                    public Object bfbdyjyje;
//                    ("bfbdnjybs")
//                    public Object bfbdnjybs;
//                    ("bfbdnjyje")
//                    public Object bfbdnjyje;
//                    ("bfbsypl")
//                    public Object bfbsypl;
//                    ("bfbktsj")
//                    public Object bfbktsj;
//                    ("bfbgbsj")
//                    public Object bfbgbsj;
//                    ("sfktjd")
//                    public String sfktjd;
//                    ("jdhybz")
//                    public Object jdhybz;
//                    ("jddyjybs")
//                    public Object jddyjybs;
//                    ("jddyjyje")
//                    public Object jddyjyje;
//                    ("jddnjybs")
//                    public Object jddnjybs;
//                    ("jddnjyje")
//                    public Object jddnjyje;
//                    ("jdsypl")
//                    public Object jdsypl;
//                    ("jdktsj")
//                    public Object jdktsj;
//                    ("jdgbsj")
//                    public Object jdgbsj;
//                    ("sfktmtzf")
//                    public String sfktmtzf;
//                    ("mtzfhybz")
//                    public Object mtzfhybz;
//                    ("mtzfdyjybs")
//                    public Object mtzfdyjybs;
//                    ("mtzfdyjyje")
//                    public Object mtzfdyjyje;
//                    ("mtzfdnjybs")
//                    public Object mtzfdnjybs;
//                    ("mtzfdnjyje")
//                    public Object mtzfdnjyje;
//                    ("mtzfsypl")
//                    public Object mtzfsypl;
//                    ("mtzfktsj")
//                    public Object mtzfktsj;
//                    ("mtzfgbsj")
//                    public Object mtzfgbsj;
//                    ("sfkttyzf")
//                    public String sfkttyzf;
//                    ("tyzfhybz")
//                    public Object tyzfhybz;
//                    ("tyzfdyjybs")
//                    public Object tyzfdyjybs;
//                    ("tyzfdyjyje")
//                    public Object tyzfdyjyje;
//                    ("tyzfdnjybs")
//                    public Object tyzfdnjybs;
//                    ("tyzfdnjyje")
//                    public Object tyzfdnjyje;
//                    ("tyzfsypl")
//                    public Object tyzfsypl;
//                    ("tyzfktsj")
//                    public Object tyzfktsj;
//                    ("tyzfgbsj")
//                    public Object tyzfgbsj;
//                    ("sfktsnyfb")
//                    public String sfktsnyfb;
//                    ("snyfbhybz")
//                    public Object snyfbhybz;
//                    ("snyfbdyjybs")
//                    public Object snyfbdyjybs;
//                    ("snyfbdyjyje")
//                    public Object snyfbdyjyje;


//        var sfktgrwy: String? =null
//        set(value) {
//            if (value == "是")
//                field = "开通"
//            else field = ""
//        }
    }
//        var age: String? =null,
//        //            set(value) {
////                field=value + "岁"
////             }
//        get() = field + "岁"
//
//        var sfktgrwy: String? =null
//        set(value) {
//            if (value == "是")
//                field = "开通"
//            else field = ""
//        }
//        get() = field
//        var sjyhhybz: String? =null
//        set(value) {
//            if (value == "是")
//                field = "开通"
//            else field = ""
//        }
//        get() = field
//        var djkhybz: String? =null
//        set(value) {
//            if (value == "是")
//                field = "活跃"
//            else field = ""
//        }
//        get() = field
//        var age: String? =null
//        //            set(value) {
////                field=value + "岁"
////             }
//        get() = field + "岁"


    open class QyqkDTO {
        var relationCustName: String? = null
        var relationIdenNo: String? = null
        var sxje: String? = null
            get() {
                return if (field == "" || field==null)
                    "0元"
                else
                    field+"元"
            }
        var yxje: String? = null
            get() {
                return if (field == "" || field==null)
                    "0元"
                else
                    field+"元"
            }
    }
    open class JtgxDTO :KeyAndValueBean(){
//        var custName: String? = null
        var custName: String? = null
        var sex: String? = null
        var certId: String? = null
         var age: String? = null
            get()=field+"岁"
        var guanxi: String? = null
         var checked = false
            set(value) {
                field = value
            }


        override fun absKey(): String {
            return certId?.substring(0,6)+"***********"+certId?.substring(17,18)
        }

        override fun absValue(): String {
            return certId?.substring(0,6)+"************"+certId?.substring(17,18)
        }

        override fun absCheck(): Boolean {
             return true
        }
    }

}

