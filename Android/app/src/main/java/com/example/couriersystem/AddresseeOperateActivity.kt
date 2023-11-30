package com.example.couriersystem

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.couriersystem.databinding.ActivityAddresseeOperateBinding

class AddresseeOperateActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddresseeOperateBinding
    private lateinit var receiver: MyReceiver
    inner class MyReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (intent.action == "com.example.couriersystem.ADDRESSEE_GET_ORDERS_SUCCESS") {
                //获取订单成功
                Log.d("MyReceiver", "onReceive: ADDRESSEE_GET_ORDERS_SUCCESS")
                val intent= Intent(this@AddresseeOperateActivity,OrderListActivity::class.java)
                startActivity(intent)
            }else if(intent.action == "com.example.couriersystem.ADDRESSEE_GET_ORDERS_FAIL"){
                //获取订单失败
                Log.d("MyReceiver", "onReceive: ADDRESSEE_GET_ORDERS_FAIL")

            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityAddresseeOperateBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // 注册广播
        val filter = IntentFilter().apply {
            addAction("com.example.couriersystem.ADDRESSEE_GET_ORDERS_SUCCESS")
            addAction("com.example.couriersystem.ADDRESSEE_GET_ORDERS_FAIL")
        }
        receiver = MyReceiver()
        registerReceiver(receiver, filter)
        binding.addresseeId.text=Addressee.Id
        binding.addresseeName.text=Addressee.Name
        binding.allOrders.setOnClickListener {
            Websocket.send("addresseeAskAllOrders:"+Addressee.Id)
        }

        binding.addOrder.setOnClickListener {
            val intent= Intent(this,AddOrderActivity::class.java)
            startActivity(intent)
        }
    }
}