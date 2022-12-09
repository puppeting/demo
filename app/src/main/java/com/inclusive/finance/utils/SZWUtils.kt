package com.inclusive.finance.utils

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.net.Uri
import android.os.Handler
import android.os.Message
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.TextUtils
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView

import com.blankj.utilcode.util.*
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.bottomnavigation.BottomNavigationMenuView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.gson.JsonArray
import com.google.gson.JsonObject
 import com.inclusive.finance.R
import com.inclusive.finance.app.MyApplication
import com.inclusive.finance.bean.BaseTypeBean
import com.inclusive.finance.bean.model.ApplyModel
import com.inclusive.finance.config.Urls
 import com.inclusive.finance.observer.SmsContentObserver

import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.math.BigDecimal
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern
import kotlin.math.abs


/**
 * Created by 史忠文
 * on 2017/10/17.
 */
object SZWUtils {
    /**
     * 获取加盐加密后的密码
     *
     */
    fun getMd5Pwd(passstr: String): String {
        if (passstr.isNotEmpty()) return EncryptUtils.encryptMD5ToString(passstr[0] + MyApplication.salt + passstr.substring(1))
            .toLowerCase()
        return ""
    }

    /**
     * @param phoneNum 电话号码
     * @return 有隐藏中间
     */
    fun hideMidPhone(phoneNum: String): String {

        return if (TextUtils.isEmpty(phoneNum)) "暂无电话"
        else if (phoneNum.length != 11) phoneNum
        else phoneNum.substring(0, 3) + "****" + phoneNum.substring(phoneNum.length - 4, phoneNum.length)
    }




    /**
     * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘
     */

    fun isShouldHideKeyboard(v: View?, event: MotionEvent): Boolean {
        if (v != null && (v is EditText)) {
            val l = intArrayOf(0, 0)
            v.getLocationInWindow(l)
            val left = l[0]
            val top = l[1]
            val bottom = top + v.getHeight()
            val right = left + v.getWidth()
            return !(event.x > left && event.x < right && event.y > top && event.y < bottom)
        }
        return false
    }

    /**
     * 增加固定外边距
     */
    fun setMargin(view: View, size: Float) {
        val lp = view.layoutParams
        if (lp is ViewGroup.MarginLayoutParams) {
            lp.topMargin += SizeUtils.dp2px(size)
        }

        view.layoutParams = lp

    }

    /**
     * 增加固定内边距
     */
    fun setPaddingSmart(view: View, size: Float) {
        val lp = view.layoutParams
        if (lp != null && lp.height > 0) {
            lp.height += SizeUtils.dp2px(size)
        }
        view.setPadding(view.paddingLeft, view.paddingTop + SizeUtils.dp2px(size), view.paddingRight, view.paddingBottom)

    }

    /**
     * 减少固定外边距
     */
    fun minusMargin(view: View, size: Float) {
        val lp = view.layoutParams
        if (lp is ViewGroup.MarginLayoutParams) {
            lp.topMargin -= SizeUtils.dp2px(size)
        }

        view.layoutParams = lp

    }

    /**
     * 减少固定内边距
     */
    fun minusPaddingSmart(view: View, size: Float) {
        val lp = view.layoutParams
        if (lp != null && lp.height > 0) {
            lp.height -= SizeUtils.dp2px(size)
        }
        view.setPadding(view.paddingLeft, view.paddingTop - SizeUtils.dp2px(size), view.paddingRight, view.paddingBottom)

    }

    //    /**
    //     * 设置灰色还是yellow 箭头
    //     *
    //     * @param b true grey  ; false yellow
    //     */
    //    fun setGreyOrYellow(context: Context?, view: RadioButton, b: Boolean) {
    //        if (context != null)
    //            if (b) {
    //                view.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(context, R.drawable.selector_tab_triangle_grey), null)
    //                view.setTextColor(ContextCompat.getColor(context, R.color.MaterialGrey600))
    //            } else {
    //                view.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(context, R.drawable.selector_tab_triangle_red), null)
    //                view.setTextColor(ContextCompat.getColor(context, R.color.color_main_blue))
    //            }
    //    }

    /**
     * 获取assets里的json
     */
    fun getJson(context: Context?, fileName: String): String {

        val stringBuilder = StringBuilder()
        try {
            val assetManager = context?.assets
            val bf = BufferedReader(InputStreamReader(assetManager?.open(fileName)))
            var b = true
            while (b) {
                val line = bf.readLine()
                if (line != null) {
                    stringBuilder.append(line)
                } else {
                    b = false
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return stringBuilder.toString()
    }

    /**
     * 注册读取短信observer
     *
     * @param context  上下文
     * @param mHandler 监听
     * @return observer
     */
    fun registerSMS(context: Context, mHandler: Handler): SmsContentObserver { //注册内容观察者获取短信
        val smsContentObserver = SmsContentObserver(context, mHandler) // ”表“内容观察者 ，通过测试我发现只能监听此Uri -----> content://sms // 监听不到其他的Uri 比如说 content://sms/outbox
        val smsUri = Uri.parse("content://sms")
        context.contentResolver.registerContentObserver(smsUri, true, smsContentObserver)
        return smsContentObserver
    }

    /**
     * @param mContext 上下文
     * @param textView 返回验证码的textView
     * @return 验证码handler
     */
    fun patternCode(mContext: Context, textView: TextView, length: Int): Handler =
        MyHandler(mContext, textView, length)

    class MyHandler constructor(internal var mContext: Context, private var textView: TextView, private var length: Int) :
        Handler() {

        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            val outbox = msg.obj as String //            edCode.setText(outbox); //            Toast.makeText(mContext, outbox, Toast.LENGTH_SHORT).show();
            textView.text = SZWUtils.patternCode(outbox, length)
        }
    }

    /**
     * 从短信字符窜提取验证码
     * @param body 短信内容
     * @param length  验证码的长度 一般6位或者4位
     * @return 接取出来的验证码
     */
    fun patternCode(body: String, length: Int): String? { // 首先([a-zA-Z0-9]{length})是得到一个连续的六位数字字母组合
        // (?<![a-zA-Z0-9])负向断言([0-9]{length})前面不能有数字
        // (?![a-zA-Z0-9])断言([0-9]{length})后面不能有数字出现


        //  获得数字字母组合
        //    Pattern p = Pattern   .compile("(?<![a-zA-Z0-9])([a-zA-Z0-9]{" + YZMLENGTH + "})(?![a-zA-Z0-9])");

        //  获得纯数字
        val p = Pattern.compile("(?<![0-9])([0-9]{$length})(?![0-9])")

        val m = p.matcher(body)
        if (m.find()) {
            println(m.group())
            return m.group(0)
        }
        return null
    }

    /*取消底部bottom动画*/
    @SuppressLint("RestrictedApi")
    fun disableShiftMode(view: BottomNavigationView) {
        var menuView = view.getChildAt(0) as BottomNavigationMenuView
        try {
            var shiftingMode = menuView.javaClass.getDeclaredField("labelVisibilityMode")
            shiftingMode.isAccessible = true
            shiftingMode.setInt(menuView, 1) //labelVisibilityMode
            //            shiftingMode.setBoolean(menuView, false);//mShiftingMode
            shiftingMode.isAccessible = false

            for (i in 0 until menuView.childCount) {
                var item = menuView.getChildAt(i) as BottomNavigationItemView //noinspection RestrictedApi //                item.setShiftingMode(false);
                item.setShifting(false) // set once again checked value, so view will be updated
                //noinspection RestrictedApi
                item.setChecked(item.itemData?.isChecked == true)
            }
        } catch (e: NoSuchFieldException) {
            Log.e("BNVHelper", "Unable to get shift mode field", e)
        } catch (e: IllegalAccessException) {
            Log.e("BNVHelper", "Unable to change value of shift mode", e)
        }
    }

    /**
     * JsonObject中获取字符串防null
     */
    fun getJsonObjectString(jsonObject: JsonObject?, key: String): String {
        return if (jsonObject?.get(key) == null || jsonObject.get(key).isJsonNull) "" else jsonObject.get(key).asString
    }

    /**
     * JsonObject中获取字符串防null  多选
     */
    fun getJsonObjectStringList(jsonObjects: List<JsonObject>?, key: String): List<String> {
        return jsonObjects?.map { if (it.get(key) == null || it.get(key).isJsonNull) "" else it.get(key).asString }
            ?: arrayListOf()
    }

    /**
     * JsonObject中获取字符串防null
     */
    fun getJsonObjectArray(jsonObject: JsonObject?, key: String): JsonArray {
        return if (jsonObject?.get(key) == null || jsonObject.getAsJsonArray(key).isJsonNull) JsonArray() else jsonObject.get(key).asJsonArray
    }

    /**
     * JsonObject中获取boolean 防null
     */
    fun getJsonObjectBoolean(jsonObject: JsonObject, key: String): Boolean {
        return if (jsonObject.get(key) == null || jsonObject.get(key).isJsonNull) false else jsonObject.get(key).asBoolean
    }

    /**
     * 校验格式是否正确，正确返回Calender 错误返回当前时间
     */
    fun getCalender(timeStr: String, format: String? = "yyyy-MM-dd"): Calendar {
        return Calendar.getInstance().apply {
            try {
                time = SimpleDateFormat(format ?: "", Locale.CHINA).parse(timeStr)
                    ?: TimeUtils.getNowDate()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    /**
     * 校验图片地址是否正确
     */
    fun getIntactUrl(picUrl: String?): String {
        return when {
            picUrl?.contains("http://") == true || picUrl?.contains("https://") == true -> return picUrl
            picUrl.isNullOrEmpty() -> ""
            else -> (Urls.url + picUrl)
        }
    }

    /**
     * 选择列表数据时使用（单选）
     */
    fun getJsonObjectBeanFromList(data: List<JsonObject>?, msg: String = "请选择一条数据", listener: (JsonObject) -> Unit) {
        val jsonObject = data?.firstOrNull { b -> getJsonObjectBoolean(b, "isCheck") }
        if (jsonObject == null) {
            ToastUtils.showLong(msg)
        } else {
            listener.invoke(jsonObject)
        }
    }

    /**
     * 选择列表数据时使用（多选）
     */
    fun getJsonObjectBeanListFromList(data: List<JsonObject>?, msg: String = "请选择至少一条数据", listener: (List<JsonObject>) -> Unit) {
        val jsonObjects = data?.filter { b -> getJsonObjectBoolean(b, "isCheck") }
        if (jsonObjects.isNullOrEmpty()) {
            ToastUtils.showLong(msg)
        } else {
            listener.invoke(jsonObjects)
        }
    }

    fun isSingleCheck(list: List<JsonObject>, msg: String = "仅能选择一条数据"): Boolean {
        if (list.size > 1) {
            ToastUtils.showLong(msg)
        }
        return list.size <= 1
    }

    /**
     *设置是否为查看模式
     */
    fun setSeeOnlyMode(viewModel: ApplyModel, it: List<BaseTypeBean>) {
        if (viewModel.seeOnly == true) {
            it.forEach { baseTypeBean ->
                baseTypeBean.editable = false
                if (baseTypeBean.layoutType == BaseTypeBean.TYPE_4) {
                    baseTypeBean.listBean?.saveUrl = ""
                }
            }
        }
    }

    /**
     *加载图片
     */
    fun loadPhotoImg(mContext: Context?, url: String? = "", imgView: ImageView?, radius: Int? = 1, error: Int? = null, placeholder: Int? = null) {
        if (mContext != null) {
//            try {
//                val with = GlideApp.with(mContext.applicationContext)
//                if (url?.contains(".gif") == true) with.asGif()
//                val options = RequestOptions().transform(CenterInside(), RoundedCorners(radius
//                    ?: 1))
//                    .error(ContextCompat.getDrawable(mContext.applicationContext, if (url.isNullOrEmpty()) R.mipmap.icon_photo_bg else error
//                        ?: R.mipmap.icon_error))
//
//                Log.e("返回的图片url","****"+getIntactUrl(url))
//                val apply = with.load(getIntactUrl(url)).apply(options)
//                if (!url.isNullOrEmpty()) apply.thumbnail(Glide.with(imgView
//                    ?: ImageView(mContext.applicationContext))
//                    .load(placeholder ?: R.drawable.loading))
//
//                //                .placeholder(ContextCompat.getDrawable(mContext, placeholder
//                //                    ?: R.drawable.loading))
//                apply.into(imgView ?: ImageView(mContext.applicationContext))
//            } catch (e: Exception) {
//                e.printStackTrace()
//            }
        }

    }









    fun setCalculateCount(it: ArrayList<BaseTypeBean>, s: String, lastYearAll: BigDecimal) {
        it.firstOrNull { item -> item.dataKey == s }?.valueName = lastYearAll.setScale(2, BigDecimal.ROUND_HALF_UP)
            .toString()
    }

    fun getCalculateCount(it: ArrayList<BaseTypeBean>, s: String) =
        it.firstOrNull { item -> item.dataKey == s && item.valueName.isNotEmpty() }?.valueName?.toDoubleOrNull()
            ?: 0.00

    /**
     *
     * 计算月差
     * */
    fun getMonthSpace(endDate: String, startDate: String): Int {
        var result: Int
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.CHINA)
        val c1: Calendar = Calendar.getInstance()
        val c2: Calendar = Calendar.getInstance()
        c1.time = sdf.parse(endDate) ?: Date()
        c2.time = sdf.parse(startDate) ?: Date()
        val y1: Int = c1.get(1)
        val y2: Int = c2.get(1)
        val m1: Int = c1.get(2)
        val m2: Int = c2.get(2)
        val d1: Int = c1.get(5)
        val d2: Int = c2.get(5)
        result = (y1 - y2) * 12 + (m1 - m2)
        if (d1 > d2) {
            result++
        }
        return if (result == 0) 1 else abs(result)
    }

    /**
     *
     * 获取各环节businessType
     * */
    fun getBusinessType(type: Int) = when (type) {
        ApplyModel.BUSINESS_TYPE_SXSP,
        ApplyModel.BUSINESS_TYPE_APPLY,
        -> {
            "0"
        }
        ApplyModel.BUSINESS_TYPE_QPLC,
        -> {
            "1"
        }
        ApplyModel.BUSINESS_TYPE_JNJ_CJ_PERSONAL,
        ApplyModel.BUSINESS_TYPE_JNJ_CJ_COMPANY,
        ApplyModel.BUSINESS_TYPE_JNJ_JC_OFF_SITE_PERSONAL,
        ApplyModel.BUSINESS_TYPE_JNJ_JC_ON_SITE_PERSONAL,
        ApplyModel.BUSINESS_TYPE_JNJ_JC_ON_SITE_COMPANY,
        -> {
            "03"
        }
        ApplyModel.BUSINESS_TYPE_SJ_PERSONAL,
        ApplyModel.BUSINESS_TYPE_SJ_COMPANY,
        -> {
            "01"
        }
        ApplyModel.BUSINESS_TYPE_RC_OFF_SITE_PERSONAL,
        ApplyModel.BUSINESS_TYPE_RC_ON_SITE_PERSONAL,
        ApplyModel.BUSINESS_TYPE_RC_ON_SITE_COMPANY,
        -> {
            "02"
        }
        ApplyModel.BUSINESS_TYPE_VISIT_NEW,
        ApplyModel.BUSINESS_TYPE_VISIT_EDIT,
        -> {
            "ZF"
        }
        ApplyModel.BUSINESS_TYPE_PRECREDIT,
        -> {
            "YSX"
        }
        ApplyModel.BUSINESS_TYPE_CREDIT_MANAGER,
        ApplyModel.BUSINESS_TYPE_CREDIT_MANAGER_LGFK,
        ApplyModel.BUSINESS_TYPE_CREDIT_MANAGER_ZXGL,
        -> {
            "YX"
        }

        ApplyModel.BUSINESS_TYPE_SJ,
        -> {
            "4"
        }
        ApplyModel.BUSINESS_TYPE_RC,
        -> {
            "5"
        }
        ApplyModel.BUSINESS_TYPE_JNJ_YX,
        -> {
            "8"
        }
        ApplyModel.BUSINESS_TYPE_INFORMATION_OFFICER,
        -> {
            "9"
        }
        ApplyModel.BUSINESS_TYPE_QUESTIONNAIRE,
        -> {
            "10"
        }
        ApplyModel.BUSINESS_TYPE_CREDIT_REVIEW,
        -> {
            "11"
        }
        ApplyModel.BUSINESS_TYPE_COMPARISON_OF_QUOTAS,
        -> {
            "12"
        }
        ApplyModel.BUSINESS_TYPE_SUNSHINE_APPLY,
        ApplyModel.BUSINESS_TYPE_SUNSHINE_QPLC,
        -> {
            "13"
        }
        ApplyModel.BUSINESS_TYPE_INFORMATION_FENGXIANGUANLI,
        -> {
            "14"
        }
        ApplyModel.BUSINESS_TYPE_POC_KHXXGL,
        -> {
            "100"
        }
        ApplyModel.BUSINESS_TYPE_POC_SXXX,
        -> {
            "110"
        }
        ApplyModel.BUSINESS_TYPE_POC_HTDJ,
        -> {
            "120"
        }
        ApplyModel.BUSINESS_TYPE_POC_FDSQ,
        -> {
            "130"
        }
        else -> {
            ""
        }
    }

    /**
     *
     * 数字变红
     * */
    fun setNumColorRed(str: String): SpannableStringBuilder? {
        val style = SpannableStringBuilder(str)
        for (i in str.indices) {
            val a = str[i]
            if (a in '0'..'9') {
                style.setSpan(ForegroundColorSpan(Color.RED), i, i + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                style.setSpan(RelativeSizeSpan(1.0f), i, i + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            }
        }
        return style
    }






}