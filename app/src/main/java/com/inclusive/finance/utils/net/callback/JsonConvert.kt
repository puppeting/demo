package com.inclusive.finance.utils.net.callback

import com.google.gson.stream.JsonReader
import com.lzy.okgo.convert.Converter
import com.inclusive.finance.utils.net.AbsNetBean
import com.inclusive.finance.utils.net.Convert
import okhttp3.Response
import org.json.JSONArray
import org.json.JSONObject
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

class JsonConvert<T : AbsNetBean> : Converter<T> {

    private var type: Type? = null
    private var clazz: Class<T>? = null


    constructor(type: Type?) {
        this.type = type
    }

    constructor(clazz: Class<T>?) {
        this.clazz = clazz
    }

    /**
     * 该方法是子线程处理，不能做ui相关的工作
     * 主要作用是解析网络返回的 response 对象，生成onSuccess回调中需要的数据对象
     * 这里的解析工作不同的业务逻辑基本都不一样,所以需要自己实现,以下给出的时模板代码,实际使用根据需要修改
     */
    @Throws(Throwable::class)
    override fun convertResponse(response: Response): T? {

        // 重要的事情说三遍，不同的业务，这里的代码逻辑都不一样，如果你不修改，那么基本不可用
        // 重要的事情说三遍，不同的业务，这里的代码逻辑都不一样，如果你不修改，那么基本不可用
        // 重要的事情说三遍，不同的业务，这里的代码逻辑都不一样，如果你不修改，那么基本不可用

        // 如果你对这里的代码原理不清楚，可以看这里的详细原理说明：https://github.com/jeasonlzy/okhttp-OkGo/blob/master/README_JSONCALLBACK.md
        // 如果你对这里的代码原理不清楚，可以看这里的详细原理说明：https://github.com/jeasonlzy/okhttp-OkGo/blob/master/README_JSONCALLBACK.md
        // 如果你对这里的代码原理不清楚，可以看这里的详细原理说明：https://github.com/jeasonlzy/okhttp-OkGo/blob/master/README_JSONCALLBACK.md

        if (type == null) {
            if (clazz == null) {
                // 如果没有通过构造函数传进来，就自动解析父类泛型的真实类型（有局限性，继承后就无法解析到）
                val genType = javaClass.genericSuperclass
                type = (genType as ParameterizedType).actualTypeArguments[0]
            } else {
                return parseClass(response, clazz)
            }
        }

        return when (type) {
            is ParameterizedType -> parseParameterizedType(response, type as ParameterizedType?)
            is Class<*> -> parseClass(response, type as Class<*>?)
            else -> parseType(response, type)
        }
    }

    @Throws(Exception::class)
    private fun parseClass(response: Response, rawType: Class<*>?): T? {
        if (rawType == null) return null
        val body = response.body() ?: return null
        val jsonReader = JsonReader(body.charStream())

        return when (rawType) {
            String::class.java -> body.string() as T
            JSONObject::class.java -> JSONObject(body.string()) as T
            JSONArray::class.java -> JSONArray(body.string()) as T
            else -> {
                val t = Convert.fromJson<T>(jsonReader, rawType)
                response.close()
                t
            }
        }
    }

    @Throws(Exception::class)
    private fun parseType(response: Response, type: Type?): T? {
        if (type == null) return null
        val body = response.body() ?: return null
        val jsonReader = JsonReader(body.charStream())

        // 泛型格式如下： new JsonCallback<任意JavaBean>(this)
        val t = Convert.fromJson<T>(jsonReader, type)
        response.close()
        return t
    }

    @Throws(Exception::class)
    private fun parseParameterizedType(response: Response, type: ParameterizedType?): T? {
        if (type == null) return null
        val body = response.body() ?: return null
        val jsonReader = JsonReader(body.charStream())
        //        String jsonReader =body.string();

        val rawType = type.rawType                     // 泛型的实际类型
        val typeArgument = type.actualTypeArguments[0] // 泛型的参数
        if (rawType !== AbsNetBean::class.java) {
            // 泛型格式如下： new JsonCallback<外层BaseBean<内层JavaBean>>(this)
            val t = Convert.fromJson<T>(jsonReader, type)
            response.close()
            return t
        } else {
            if (typeArgument === Void::class.java) {
                // 泛型格式如下： new JsonCallback<LzyResponse<Void>>(this)
                //                SimpleResponse simpleResponse = JSON.parseObject(jsonReader, SimpleResponse.class);
                val simpleResponse = Convert.fromJson<T>(jsonReader, AbsNetBean::class.java)
                response.close()

                return simpleResponse
            } else {
                // 泛型格式如下： new JsonCallback<LzyResponse<内层JavaBean>>(this)
                //                NetEntity netEntity =JSON.parseObject(jsonReader, type);
                val netEntity = Convert.fromJson<T>(jsonReader, type)
                response.close()
                val code = netEntity.statusCode
                //这里的0是以下意思
                //一般来说服务器会和客户端约定一个数表示成功，其余的表示失败，这里根据实际情况修改

//                200	正常，服务器对输出内容加密
//                500	服务器内部出现未知异常，无须处理正文内容
//                511	token已经失效，请重新申请token（使用交换密钥接口申请token并获得新的rc4的密钥），无须处理正文内容
                when (code) {
                    200 -> return netEntity
                    500 -> throw  IllegalStateException("服务器内部出现未知异常，无须处理正文内容")
                    555 -> throw  IllegalStateException("token已经失效，请重新申请token（使用交换密钥接口申请token并获得新的rc4的密钥），无须处理正文内容")
                    else -> //直接将服务端的错误信息抛出，onError中可以获取
                        throw IllegalStateException("errorCode:"+netEntity.statusCode + " msg:"+netEntity.msg)
                }
            }
        }
    }
}