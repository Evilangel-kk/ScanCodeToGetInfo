package com.example.couriersystem

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.couriersystem.databinding.ActivityMainBinding
import com.google.zxing.integration.android.IntentIntegrator
import com.journeyapps.barcodescanner.CaptureActivity


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Websocket.connect("https://f6e9146.r15.vip.cpolar.cn")
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