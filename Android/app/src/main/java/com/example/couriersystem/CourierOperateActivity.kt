package com.example.couriersystem

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.couriersystem.databinding.ActivityCourierOperateBinding
import com.google.zxing.integration.android.IntentIntegrator

class CourierOperateActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCourierOperateBinding
    private lateinit var receiver: MyReceiver

    inner class MyReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (intent.action == "com.example.couriersystem.COURIER_GET_ORDERS_SUCCESS") {
                //获取订单成功
                Log.d("MyReceiver", "onReceive: COURIER_GET_ORDERS_SUCCESS")
                val intent= Intent(this@CourierOperateActivity,OrderListActivity::class.java)
                startActivity(intent)
            }else if(intent.action == "com.example.couriersystem.COURIER_GET_ORDERS_FAIL"){
                //获取订单失败
                Log.d("MyReceiver", "onReceive: COURIER_GET_ORDERS_FAIL")

            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityCourierOperateBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // 注册广播
        val filter = IntentFilter().apply {
            addAction("com.example.couriersystem.COURIER_GET_ORDERS_SUCCESS")
            addAction("com.example.couriersystem.COURIER_GET_ORDERS_FAIL")
        }
        receiver = MyReceiver()
        registerReceiver(receiver, filter)
        binding.courierId.text=Courier.Id
        binding.courierName.text=Courier.Name
        binding.allOrders.setOnClickListener {
            Websocket.send("courierAskAllOrders:"+Courier.Id)
        }

        binding.scan.setOnClickListener {
            IntentIntegrator(this).setOrientationLocked(false)
                .setCaptureActivity(ScanCodeActivity::class.java)
                .setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)// 扫码的类型,可选：一维码，二维码，一/二维码
                .setPrompt("请对准二维码")// 设置提示语
                .setCameraId(0)// 选择摄像头,可使用前置或者后置
                .setBeepEnabled(false)// 是否开启声音,扫完码之后会"哔"的一声
                .initiateScan()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show()
            } else {
                var msg=result.contents.split(":")
                if(msg[0]=="码上识件"){
                    if(msg[6]!=Courier.Id){
                        Toast.makeText(this, "该件的派送者不是你，你无权查看快递信息", Toast.LENGTH_LONG).show()
                    }else{
                        Order.Id=msg[1]
                        Order.Location=msg[2]
                        Order.AddresseeId=msg[3]
                        Order.AddresseeName=msg[4]
                        Order.AddresseePhone=msg[5]
                        Order.CourierId=msg[6]

                        val intent=Intent(this,CodeMessageActivity::class.java)
                        startActivity(intent)
                    }
                }else{
                    Toast.makeText(this, "请扫描本系统的二维码", Toast.LENGTH_LONG).show()
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }
}