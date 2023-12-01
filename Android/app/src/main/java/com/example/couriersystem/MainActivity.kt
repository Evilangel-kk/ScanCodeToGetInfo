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
        Websocket.connect("https://6e4d66d4.r8.vip.cpolar.cn")
        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        var intent= Intent(this,LoginActivity::class.java)
        binding.courier.setOnClickListener {
            User.u="courier"
            startActivity(intent)
        }
        binding.addressee.setOnClickListener {
            User.u="addressee"
            startActivity(intent)
        }
    }

    override fun onDestroy() {
        Websocket.close()
        if(User.u=="courier"){
            Websocket.send("COURIER_LEAVE:"+Courier.Id)
        }else{
            Websocket.send("ADDRESSEE_LEAVE:"+Addressee.Id)
        }
        super.onDestroy()
    }
}