package com.example.couriersystem

import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString

object Websocket {
    private var webSocket: WebSocket? = null
    private val client = OkHttpClient()

    fun connect(url: String) {
        val request = Request.Builder().url(url).build()
        webSocket = client.newWebSocket(request, object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                println("WebSocket连接已打开")
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                println("收到服务器消息：$text")
            }

            override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
                // 处理二进制数据
            }

            override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
                webSocket.close(NORMAL_CLOSURE_STATUS, null)
                println("WebSocket连接即将关闭")
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                println("WebSocket连接失败：${t.message}")
            }
        })
    }

    fun send(text: String) {
        webSocket?.send(text)
    }

    fun close() {
        webSocket?.close(NORMAL_CLOSURE_STATUS, null)
    }

    private const val NORMAL_CLOSURE_STATUS = 1000
}