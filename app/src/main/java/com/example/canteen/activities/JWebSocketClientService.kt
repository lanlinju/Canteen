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
    private var client: JWebSocketClient? = null
     val _chatMessage: MutableLiveData<Chat> = MutableLiveData()
    val chatMessage: LiveData<Chat> get() = _chatMessage

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
            //创建
            override fun onMessage(message: String?) {//接收聊天信息 监听
                val chat = Gson().fromJson(message, Chat::class.java)
                _chatMessage.postValue(chat)
            }
        }
        client?.connect()
    }

    override fun onDestroy() {
        //closeConnect()
        "service onDestroy".showLog()
        super.onDestroy()

    }

    fun connectWebSocket() {//退出账号重新连接
        if (client == null) {
            initSocketClient()//重新连接
            return
        }
        client?.let {
            if (it.isClosed) {
                initSocketClient()//重新连接
            }
        }

    }

    /**
     * 发送消息
     *
     * @param msg
     */
    fun sendMessage(msg: String) {
        Log.e("JWebSocketClientService", "发送的消息：$msg")
        try {
            client?.send(msg)
        } catch (e: Exception) {
            e.message?.showToast()
        }

    }

    /**
     * 断开连接
     */
    fun closeConnect() {
        client?.close()
    }
}