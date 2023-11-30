package com.example.couriersystem

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBar
import com.example.couriersystem.databinding.ActivityEnrollBinding

class EnrollActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEnrollBinding
    private lateinit var receiver: MyReceiver

    //    广播接收消息
    inner class MyReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (intent.action == "com.example.couriersystem.ENROLL_SUCCESS") {
                //登陆成功
                Log.d("MyReceiver", "onReceive: ENROLL_SUCCESS")
                binding.warningId.visibility=View.INVISIBLE
                binding.warningPwd.visibility=View.INVISIBLE
                binding.warningNull.visibility=View.INVISIBLE
                val intent = Intent(this@EnrollActivity, LoginActivity::class.java)
                startActivity(intent)
            }else if(intent.action == "com.example.couriersystem.ID_EXISTED"){
                //账号已经存在
                Log.d("MyReceiver", "onReceive: ID_EXISTED")
                binding.warningId.visibility=View.VISIBLE
                binding.warningPwd.visibility=View.INVISIBLE
                binding.warningNull.visibility=View.INVISIBLE
            }
        }
    }
    @SuppressLint("UseCompatLoadingForDrawables", "UnspecifiedRegisterReceiverFlag")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityEnrollBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // 注册广播
        val filter = IntentFilter().apply {
            addAction("com.example.couriersystem.ENROLL_SUCCESS")
            addAction("com.example.couriersystem.ID_EXISTED")
        }
        receiver = MyReceiver()
        registerReceiver(receiver, filter)
        binding.home.setOnClickListener{
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
        }
        binding.toLogin.setOnClickListener {
            val intent = Intent(this,LoginActivity::class.java)
            startActivity(intent)
        }
        // 密码可视功能
        binding.userPwd.inputType=
            InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        binding.eye.setOnClickListener {
            if(binding.userPwd.inputType== InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD){
                binding.userPwd.inputType= InputType.TYPE_NUMBER_VARIATION_PASSWORD
                binding.eye.setImageDrawable(resources.getDrawable(R.drawable.open_eye))
            }else{
                binding.userPwd.inputType= InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                binding.eye.setImageDrawable(resources.getDrawable(R.drawable.close_eye))
            }
        }
        // 密码可视功能
        binding.userPwdConfirm.inputType=
            InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        binding.eyeConfirm.setOnClickListener {
            if(binding.userPwdConfirm.inputType== InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD){
                binding.userPwdConfirm.inputType= InputType.TYPE_NUMBER_VARIATION_PASSWORD
                binding.eyeConfirm.setImageDrawable(resources.getDrawable(R.drawable.open_eye))
            }else{
                binding.userPwdConfirm.inputType= InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                binding.eyeConfirm.setImageDrawable(resources.getDrawable(R.drawable.close_eye))
            }
        }
        //点击确认
        binding.confirm.setOnClickListener {
            if(binding.userId.text.toString()=="" || binding.userPwd.text.toString()=="" || binding.userPwdConfirm.text.toString()=="" || binding.userName.text.toString()==""||binding.userPhone.text.toString()==""){
                //信息不能为空
                binding.warningNull.visibility=View.VISIBLE
                binding.warningPwd.visibility=View.INVISIBLE
                binding.warningId.visibility=View.INVISIBLE
            } else if(binding.userPwd.text.toString()!=binding.userPwdConfirm.text.toString()){
                //密码和确认密码不统一
                binding.warningPwd.visibility= View.VISIBLE
                binding.warningNull.visibility=View.INVISIBLE
                binding.warningId.visibility=View.INVISIBLE
            }else{
                //密码和确认密码统一
                binding.warningPwd.visibility=View.INVISIBLE
                binding.warningNull.visibility=View.INVISIBLE
                binding.warningId.visibility=View.INVISIBLE
                //查询账号是否存在
                Websocket.send(User.u+"FindId:"+binding.userId.text.toString()+":"+binding.userPwd.text.toString()+":"+binding.userName.text.toString()+":"+binding.userPhone.text.toString())
            }
        }
    }
}