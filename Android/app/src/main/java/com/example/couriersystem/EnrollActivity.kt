package com.example.couriersystem

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.view.View
import com.example.couriersystem.databinding.ActivityEnrollBinding

class EnrollActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEnrollBinding
    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityEnrollBinding.inflate(layoutInflater)
        setContentView(binding.root)
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
            if(binding.userPwd.text.toString()!=binding.userPwdConfirm.text.toString()){
                //密码和确认密码不统一
                binding.warning.visibility= View.VISIBLE
            }else{
                //密码和确认密码统一
                binding.warning.visibility=View.INVISIBLE
                //查询账号是否存在
            }
        }
    }
}