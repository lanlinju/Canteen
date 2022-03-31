package com.example.canteen.network.im

import com.example.canteen.utilities.showLog
import com.example.canteen.utilities.showToast
import java.net.URI
import org.java_websocket.client.WebSocketClient
import org.java_websocket.handshake.ServerHandshake
import java.lang.Exception


open class JWebSocketClient(serverURI: URI): WebSocketClient(serverURI) {
    override fun onOpen(handshakedata: ServerHandshake?) {
        "WebSocke is open".showLog()
    }

    override fun onMessage(message: String?) {

    }

    override fun onClose(code: Int, reason: String?, remote: Boolean) {
        reason?.showLog()
    }

    override fun onError(ex: Exception?) {
        ex?.message?.showLog()
    }

}