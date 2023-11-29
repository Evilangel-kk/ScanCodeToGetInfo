package com.example.couriersystem

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.couriersystem.databinding.ActivityAddOrderBinding

class AddOrderActivity : AppCompatActivity() {
    private lateinit var binding:ActivityAddOrderBinding
    private lateinit var receiver: MyReceiver

    inner class MyReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (intent.action == "com.example.couriersystem.ADDORDER_SUCCESS") {
                //登陆成功
                binding.warningId.visibility=View.INVISIBLE
                binding.warningNull.visibility=View.INVISIBLE
                val intent = Intent(this@AddOrderActivity, SuccessActivity::class.java)
                startActivity(intent)
            }else if(intent.action == "com.example.couriersystem.ORDERID_EXISTED"){
                //账号已经存在
                Log.d("MyReceiver", "onReceive: ID_EXISTED")
                binding.warningId.visibility=View.VISIBLE
                binding.warningNull.visibility=View.INVISIBLE
            }
        }
    }

    @SuppressLint("UnspecifiedRegisterReceiverFlag")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityAddOrderBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // 注册广播
        val filter = IntentFilter().apply {
            addAction("com.example.couriersystem.ADDORDER_SUCCESS")
            addAction("com.example.couriersystem.ORDERID_EXISTED")
        }
        receiver = MyReceiver()
        registerReceiver(receiver, filter)
        binding.confirm.setOnClickListener {
            if(binding.orderId.text.toString()==""||binding.orderLocation.text.toString()==""){
                binding.warningNull.visibility= View.VISIBLE
                binding.warningId.visibility=View.INVISIBLE
            }else{
                binding.warningNull.visibility= View.INVISIBLE
                binding.warningId.visibility=View.INVISIBLE
                Websocket.send("AddOrder:"+binding.orderId.text.toString()+":"+binding.orderLocation.text.toString()+":"+Addressee.Id+":"+Addressee.Name+":"+Addressee.Phone)
            }
        }
    }
}