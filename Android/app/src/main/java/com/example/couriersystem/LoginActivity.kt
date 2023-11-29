package com.example.couriersystem

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.couriersystem.databinding.ActivityLoginBinding


class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var user:String
    private lateinit var receiver: MyReceiver

    //    广播接收消息
    inner class MyReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (intent.action == "com.example.couriersystem.LOGIN_SUCCESS") {
                //登陆成功
                Log.d("MyReceiver", "onReceive: LOGIN_SUCCESS")
                binding.warning.visibility= View.INVISIBLE
                Log.d("TAG", user)
                if(user=="courier"){
                    //跳转快递员操作界面
                    val courierintent=Intent(this@LoginActivity,CourierOperateActivity::class.java)
                    startActivity(courierintent)
                }else {
                    //跳转收件人操作界面
                    val addresseeintent=Intent(this@LoginActivity,AddresseeOperateActivity::class.java)
                    startActivity(addresseeintent)
                }
//                val successintent = Intent(this@LoginActivity, SuccessActivity::class.java)
//                successintent.putExtra("user",user)
//                startActivity(successintent)
            }else if(intent.action == "com.example.couriersystem.LOGIN_FAIL"){
                //登陆失败
                Log.d("MyReceiver", "onReceive: LOGIN_FAIL")
                binding.warning.visibility= View.VISIBLE
            }
        }
    }
    @SuppressLint("UseCompatLoadingForDrawables", "UnspecifiedRegisterReceiverFlag")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        user= intent.getStringExtra("user").toString()
        // 注册广播
        val filter = IntentFilter().apply {
            addAction("com.example.couriersystem.LOGIN_SUCCESS")
            addAction("com.example.couriersystem.LOGIN_FAIL")
        }
        receiver = MyReceiver()
        registerReceiver(receiver, filter)
        binding.home.setOnClickListener{
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
        }
        binding.toEnroll.setOnClickListener {
            val intent=Intent(this,EnrollActivity::class.java)
            intent.putExtra("user",user)
            startActivity(intent)
        }
        // 密码可视功能
        binding.userPwd.inputType=InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        binding.eye.setOnClickListener {
            if(binding.userPwd.inputType==InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD){
                binding.userPwd.inputType=InputType.TYPE_NUMBER_VARIATION_PASSWORD
                binding.eye.setImageDrawable(resources.getDrawable(R.drawable.open_eye))
            }else{
                binding.userPwd.inputType=InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                binding.eye.setImageDrawable(resources.getDrawable(R.drawable.close_eye))
            }
        }

        //点击确认
        binding.confirm.setOnClickListener {
            //查询账号对应密码
            Websocket.send(user+"FindPwdById:"+binding.userId.text.toString()+":"+binding.userPwd.text.toString())
        }
    }
}