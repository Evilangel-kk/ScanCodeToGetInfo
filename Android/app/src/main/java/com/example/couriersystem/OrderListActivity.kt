package com.example.couriersystem

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.couriersystem.databinding.ActivityOrderListBinding

class OrderListActivity : AppCompatActivity() {
    private lateinit var binding:ActivityOrderListBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityOrderListBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}