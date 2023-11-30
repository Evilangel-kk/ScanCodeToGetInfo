package com.example.couriersystem

object OrderList {
    var orders= ArrayList<EachOrder>()
    fun add(order:EachOrder){
        this.orders.add(order)
    }
    fun clear(){
        orders.clear()
    }
}