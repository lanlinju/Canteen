package com.example.canteen.activities

import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.lifecycleScope
import com.example.canteen.network.im.JWebSocketClient
import com.example.canteen.utilities.Constants
import com.example.canteen.utilities.getPreferenceManager
import com.example.canteen.utilities.showLog
import kotlinx.coroutines.launch
import java.net.URI

class JWebSocketClientService : LifecycleService() {
    lateinit var client: JWebSocketClient
    val data: MutableLiveData<String> = MutableLiveData()

    override fun onCreate() {
        "service onCreate".showLog()
        super.onCreate()

    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        initSocketClient()
        "service onStartCommand".showLog()
        return super.onStartCommand(intent, flags, startId)
    }

    inner class JWebSocketClientBinder : Binder() {
        val service = this@JWebSocketClientService
    }

    override fun onBind(intent: Intent): IBinder {
        super.onBind(intent)
        return JWebSocketClientBinder()
    }

    /**
     * 初始化websocket连接
     */
    private fun initSocketClient() {
        val uri = URI.create(
            Constants.WEBSCOCKET_URI + applicationContext.getPreferenceManager().getString(
                Constants.KEY_NAME
            )
        )
        client = object : JWebSocketClient(uri) {
            override fun onMessage(message: String?) {
                lifecycleScope.launch {
                    message?.let {
                        data.value = it
                    }

                }
                message?.showLog()
            }
        }
        client.connect()
    }

    override fun onDestroy() {
        closeConnect()
        "service onDestroy".showLog()
        super.onDestroy()

    }

    /**
     * 发送消息
     *
     * @param msg
     */
    fun sendMsg(msg: String) {
        if (null != client) {
            Log.e("JWebSocketClientService", "发送的消息：$msg")
            client.send(msg)
        }
    }

    /**
     * 断开连接
     */
    private fun closeConnect() {
        client?.close()
    }
}