package com.example.couriersystem

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import androidx.appcompat.app.AppCompatActivity
import com.example.couriersystem.databinding.ActivityLoginBinding


class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toEnroll.setOnClickListener {
            val intent=Intent(this,EnrollActivity::class.java)
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
    }
}