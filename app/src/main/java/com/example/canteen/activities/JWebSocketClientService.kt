package com.example.canteen.activities

import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import com.example.canteen.models.Chat
import com.example.canteen.network.im.JWebSocketClient
import com.example.canteen.utilities.Constants
import com.example.canteen.utilities.getPreferenceManager
import com.example.canteen.utilities.showLog
import com.example.canteen.utilities.showToast
import com.google.gson.Gson
import kotlinx.coroutines.launch
import java.net.URI

class JWebSocketClientService : LifecycleService() {
    lateinit var client: JWebSocketClient
    private val _chatMessage: MutableLiveData<Chat> = MutableLiveData()
    val chatMessage:LiveData<Chat> get() = _chatMessage

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
                Constants.KEY_USER_ID
            )
        )
        client = object : JWebSocketClient(uri) {
            override fun onMessage(message: String?) {
                lifecycleScope.launch {
                    _chatMessage.value = Gson().fromJson(message,Chat::class.java)
                }

            }
        }
        client.connect()
    }

    override fun onDestroy() {
        //closeConnect()
        "service onDestroy".showLog()
        super.onDestroy()

    }

    /**
     * 发送消息
     *
     * @param msg
     */
    fun sendMessage(msg: String) {
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