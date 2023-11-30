package com.example.couriersystem

import android.content.Intent
import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString

object Websocket {
    private var webSocket: WebSocket? = null
    private val client = OkHttpClient()
    private val TAG = "Websocket"

    fun connect(url: String) {
        val request = Request.Builder().url(url).build()
        webSocket = client.newWebSocket(request, object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                println("WebSocket连接已打开")
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                println("收到服务器消息：$text")
                if(text==Msg.LOGIN_SUCCESS){
                    println("登录成功")
                    //发送登录成功广播
                    val intent = Intent("com.example.couriersystem.LOGIN_SUCCESS")
                    MyApplication.getInstance().getContext().sendBroadcast(intent)
                }else if(text==Msg.LOGIN_FAIL){
                    println("登录失败")
                    //发送登录失败广播
                    val intent = Intent("com.example.couriersystem.LOGIN_FAIL")
                    MyApplication.getInstance().getContext().sendBroadcast(intent)
                } else if(text==Msg.ID_EXISTED){
                    //账号存在
                    println("已经存在该账号")
                    //发送提醒账号存在广播
                    val intent = Intent("com.example.couriersystem.ID_EXISTED")
                    MyApplication.getInstance().getContext().sendBroadcast(intent)
                } else if(text==Msg.ENROLL_SUCCESS){
                    //发送注册成功广播
                    println("注册成功")
                    val intent = Intent("com.example.couriersystem.ENROLL_SUCCESS")
                    MyApplication.getInstance().getContext().sendBroadcast(intent)
                } else if(text==Msg.ADDORDER_SUCCESS){
                    println("添加成功")
                    val intent = Intent("com.example.couriersystem.ADDORDER_SUCCESS")
                    MyApplication.getInstance().getContext().sendBroadcast(intent)
                } else if(text==Msg.ORDERID_EXISTED){
                    println("订单编号已经存在")
                    val intent = Intent("com.example.couriersystem.ORDERID_EXISTED")
                    MyApplication.getInstance().getContext().sendBroadcast(intent)
                } else if(text==Msg.COURIER_GET_ORDERS_FAIL){
                    println("获取快递员订单失败")
                    val intent = Intent("com.example.couriersystem.COURIER_GET_ORDERS_FAIL")
                    MyApplication.getInstance().getContext().sendBroadcast(intent)
                }  else if(text==Msg.ADDRESSEE_GET_ORDERS_FAIL){
                    println("获取收件人订单失败")
                    val intent = Intent("com.example.couriersystem.ADDRESSEE_GET_ORDERS_FAIL")
                    MyApplication.getInstance().getContext().sendBroadcast(intent)
                }  else if(text==Msg.NOTFUND_PHONEANDNAME){
                    println("获取快递员电话号和姓名失败")
                    val intent = Intent("com.example.couriersystem.NOTFUND_PHONEANDNAME")
                    MyApplication.getInstance().getContext().sendBroadcast(intent)
                } else if("Addressee:" in text){
                    val message=text.split(":")
                    Addressee.Id=message[1]
                    Addressee.Name=message[2]
                    Addressee.Phone=message[3]
                    println("收件人信息获取成功")
                } else if("Courier:" in text){
                    val message=text.split(":")
                    Courier.Id=message[1]
                    Courier.Name=message[2]
                    Courier.Phone=message[3]
                    println("快递员信息获取成功")
                } else if("courierOrders" in text){
                    OrderList.clear()
                    val orders=text.split("&")
                    for(i in 1..orders.size-1){
                        val msg=orders[i].split(":")
                        val o=EachOrder(msg[0],msg[1],msg[2],msg[3],msg[4],msg[5])
                        Log.d(TAG, "Get one Order")
                        OrderList.add(o)
                    }
                    val intent = Intent("com.example.couriersystem.COURIER_GET_ORDERS_SUCCESS")
                    MyApplication.getInstance().getContext().sendBroadcast(intent)
                } else if("addresseeOrders" in text){
                    OrderList.clear()
                    val orders=text.split("&")
                    for(i in 1..orders.size-1){
                        val msg=orders[i].split(":")
                        val o=EachOrder(msg[0],msg[1],msg[2],msg[3],msg[4],msg[5])
                        Log.d(TAG, "Get one Order")
                        OrderList.add(o)
                    }
                    val intent = Intent("com.example.couriersystem.ADDRESSEE_GET_ORDERS_SUCCESS")
                    MyApplication.getInstance().getContext().sendBroadcast(intent)
                } else if("courierPhoneAndName" in text){
                    Phone.p=text.split(":")[1]
                    Name.n=text.split(":")[2]
                    val intent = Intent("com.example.couriersystem.FUND_PHONEANDNAME")
                    MyApplication.getInstance().getContext().sendBroadcast(intent)
                }else{
                    println("$text")
                }
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

    private val NORMAL_CLOSURE_STATUS = 1000
}