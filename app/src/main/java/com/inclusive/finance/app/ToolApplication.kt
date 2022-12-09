package com.inclusive.finance.app


import android.app.Application
import android.content.Context
import androidx.camera.camera2.Camera2Config
import androidx.camera.core.CameraXConfig
import com.inclusive.finance.bean.MyObjectBox
import com.inclusive.finance.glide.PictureSelectorEngineImp
import com.inclusive.finance.room.OrderDao
import com.inclusive.finance.utils.SoundUtil
import com.luck.picture.lib.app.IApp
import com.luck.picture.lib.app.PictureAppMaster
import com.luck.picture.lib.engine.PictureSelectorEngine
import io.objectbox.BoxStore
import me.jessyan.autosize.AutoSizeConfig
import me.jessyan.autosize.unit.Subunits
import java.security.SecureRandom
import java.security.cert.X509Certificate
import java.util.*
import javax.net.ssl.HttpsURLConnection
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager


/**
 * Created by 史忠文
 * on 2018/04/16.
 */

class ToolApplication : MyApplication(), IApp,CameraXConfig.Provider {
    private var mInit: Boolean = false
    val TAG: String? = MyApplication::class.java.simpleName
    lateinit var boxStore: BoxStore //数据库入口
    override fun getSaltStr(): String? = ""
    override fun onCreate() {
        super.onCreate()
        init(true)
        //数据库初始化
        boxStore = MyObjectBox.builder().androidContext(this).build()
        initX5()
         //初始化组件化基础库, 统计SDK/推送SDK/分享SDK都必须调用此初始化接口
//        UMConfigure.init(this, "5d101f8a0cafb285430000a7", "Umeng", UMConfigure.DEVICE_TYPE_PHONE, "ef5c818094f6924dac1b6f0ea3aab2ee")
        // PushSDK初始化(如使用推送SDK，必须调用此方法)
        //        initUpush()
        /**
         * 注意: 即使您已经在AndroidManifest.xml中配置过appkey和channel值，也需要在App代码中调
         * 用初始化接口（如需要使用AndroidManifest.xml中配置好的appkey和channel值，
         * UMConfigure.init调用中appkey和channel参数请置为null）。
         */




        initAutoSize()

        handleSSLHandshake()

        initSmallVideo()
        /** PictureSelector日志管理配制开始 **/
        // PictureSelector 绑定监听用户获取全局上下文或其他...
        PictureAppMaster.getInstance().app = this




        SoundUtil.initSound(appContext)
    }

    /**
     *
     * 初始化压缩组件地址
     * */
    fun initSmallVideo() {
        // 设置拍摄视频缓存路径

    }

    /**
     *
     * 信任所有证书
     * */
    fun handleSSLHandshake() {
        try {
            val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {

                override fun checkClientTrusted(certs: Array<X509Certificate?>?, authType: String?) {}
                override fun checkServerTrusted(certs: Array<X509Certificate?>?, authType: String?) {}
                override fun getAcceptedIssuers(): Array<X509Certificate?> {
                    return arrayOfNulls(0)
                }
            })
            val sc: SSLContext = SSLContext.getInstance("TLS")
            // trustAllCerts信任所有的证书
            sc.init(null, trustAllCerts, SecureRandom())
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.socketFactory)
            HttpsURLConnection.setDefaultHostnameVerifier { hostname, session -> true }
        } catch (ignored: Exception) {
        }
    }


    override fun getAppContext(): Context? {
        return this
    }

    override fun getPictureSelectorEngine(): PictureSelectorEngine? {
        return PictureSelectorEngineImp()
    }

    override fun getCameraXConfig(): CameraXConfig {
        return Camera2Config.defaultConfig()
    }

    //    private fun initUpush() {
    //        //获取消息推送代理示例
    //        val mPushAgent = PushAgent.getInstance(this)
    //        mPushAgent.setNotificaitonOnForeground(true)
    //        InAppMessageManager.getInstance(this).setInAppMsgDebugMode(true)
    //
    //
    //        val messageHandler = object : UmengMessageHandler() {
    //            override fun dealWithNotificationMessage(p0: Context?, p1: UMessage?) {
    //                UTrack.getInstance(applicationContext).trackMsgClick(p1)
    //                super.dealWithNotificationMessage(p0, p1)
    //            }
    //
    //            //            override fun getNotification(p0: Context?, p1: UMessage?): Notification {
    //            //                when (p1?.builder_id) {
    //            //                    1 -> {
    //            //                        val id = "my_channel_01";
    //            //                        val name="我是渠道名字";
    //            //                        val notificationManager = getSystemService(NOTIFICATION_SERVICE)as NotificationManager
    //            //                        val notification :Notification
    //            //                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
    //            //                            val mChannel = NotificationChannel(id, name, NotificationManager.IMPORTANCE_LOW);
    //            //                            Toast.makeText(p0, mChannel.toString(), Toast.LENGTH_SHORT).show();
    //            //                            notificationManager.createNotificationChannel(mChannel);
    //            //                            notification = Notification.Builder(p0)
    //            //                                    .setChannelId(id)
    //            //                                    .setContentTitle("5 new messages")
    //            //                                    .setContentText("hahaha")
    //            //                                    .setSmallIcon(R.mipmap.ic_launcher).build();
    //            //                        } else {
    //            //                            val notificationBuilder = NotificationCompat.Builder(p0)
    //            //                                    .setContentTitle("5 new messages")
    //            //                                    .setContentText("hahaha")
    //            //                                    .setSmallIcon(R.mipmap.ic_launcher)
    //            //                                    .setOngoing(true)
    //            //                                    .setChannelId(id)
    //            //                            notification = notificationBuilder.build();
    //            //                        }
    //            //                        return notification
    //            //                    }
    //            //                    else ->
    //            //                        //默认为0，若填写的builder_id并不存在，也使用默认。
    //            //                        return super.getNotification(p0, p1)
    //            //                }
    //            //
    //            //            }
    //        }
    //        mPushAgent.messageHandler = messageHandler
    //
    //
    //        //注册推送服务，每次调用register方法都会回调该接口
    //        mPushAgent.register(object : IUmengRegisterCallback {
    //            override fun onSuccess(deviceToken: String) {
    //                //注册成功会返回deviceToken deviceToken是推送消息的唯一标志
    //                Log.i("deviceToken", "注册成功：deviceToken：-------->  $deviceToken")
    //
    //            }
    //
    //            override fun onFailure(s: String, s1: String) {
    //                Log.e("deviceToken", "注册失败：-------->  s:$s,s1:$s1")
    //            }
    //        })
    //        //各个平台的配置，建议放在全局Application或者程序入口
    //        PlatformConfig.setWeixin("wx0974b5883005adec", "edd40368cb7cde135e3a9392f4abb512")
    //    }

    private fun initX5(){





    }


    private fun initAutoSize() {
        AutoSizeConfig.getInstance().setBaseOnWidth(true).unitsManager.setSupportDP(false)
            .setSupportSP(false).supportSubunits = Subunits.MM
    }

    companion object {
        fun getAPP(app: Application): ToolApplication = app as ToolApplication
        fun singletonTest(app: Context): OrderDao  {
            var ordersDao = OrderDao(app)
            return ordersDao
        }
        const val appDebug = false
    }
}
