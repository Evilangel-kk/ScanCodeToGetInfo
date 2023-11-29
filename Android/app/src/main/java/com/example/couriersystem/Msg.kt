package com.example.couriersystem

/*
* 记录信息的单例类
* 当客户端处理完数据后返回对应信息给Activity*/
object Msg {
    //登录信息
    val LOGIN_SUCCESS="1"
    val LOGIN_FAIL="2"
    //注册信息
    val ENROLL_SUCCESS="3"
    val ENROLL_FAIL="4"
    //检索完毕信息
    val GETINFO_SUCCESS="5"
    val GETINFO_FAIL="6"
    val ID_EXISTED = "7"
    val ID_INEXIST = "8"
    val ORDERID_EXISTED = "9"
    val ADDORDER_SUCCESS = "10"
}