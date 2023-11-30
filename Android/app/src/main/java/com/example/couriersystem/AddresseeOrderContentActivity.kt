package com.example.couriersystem

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.couriersystem.databinding.ActivityAddresseeOrderContentBinding

class AddresseeOrderContentActivity : AppCompatActivity() {
    private lateinit var binding:ActivityAddresseeOrderContentBinding
    private lateinit var receiver: MyReceiver

    inner class MyReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (intent.action == "com.example.couriersystem.FUND_PHONEANDNAME") {
                //查询到快递员电话号
                binding.phone.visibility=View.VISIBLE
                binding.courierPhone.text=Phone.p
                binding.courierName.text=Name.n
            }else if(intent.action == "com.example.couriersystem.NOTFUND_PHONEANDNAME"){
                //未查询到快递员电话号
                binding.phone.visibility=View.VISIBLE
                binding.courierPhone.text="未查询到"
                binding.courierName.text="未查询到"
            }
        }
    }

    companion object {
        fun actionStart(context: Context, order: EachOrder) {
            val intent = Intent(context, AddresseeOrderContentActivity::class.java).apply {
                putExtra("order", order)
            }
            context.startActivity(intent)
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityAddresseeOrderContentBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // 注册广播
        val filter = IntentFilter().apply {
            addAction("com.example.couriersystem.NOTFUND_PHONEANDNAME")
            addAction("com.example.couriersystem.FUND_PHONEANDNAME")
        }
        receiver = MyReceiver()
        registerReceiver(receiver, filter)
        val order = intent.getSerializableExtra("order") as EachOrder
        Log.d("AddresseeOrderContentActivity", "addressee")
        binding.orderId.text=order.Id
        binding.orderLocation.text=order.Location
        if(order.CourierId!=""){
            Websocket.send("FindCourierPhoneAndName:"+order.CourierId)
        }else{
            binding.courierName.text="系统还没来得及为您分配快递员"
            binding.phone.visibility= View.INVISIBLE
        }
    }
}