package com.example.couriersystem

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.couriersystem.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Websocket.connect("https://7d2958.r8.vip.cpolar.cn")
        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        var intent= Intent(this,LoginActivity::class.java)
        binding.courier.setOnClickListener {
            intent.putExtra("user","courier")
            startActivity(intent)
        }
        binding.addressee.setOnClickListener {
            intent.putExtra("user", "addressee")
            startActivity(intent)
        }
    }

    override fun onDestroy() {
        Websocket.close()
        super.onDestroy()
    }
}