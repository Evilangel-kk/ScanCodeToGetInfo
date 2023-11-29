package com.example.couriersystem

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.couriersystem.databinding.ActivityCourierOrdersBinding

class CourierOrdersActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCourierOrdersBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityCourierOrdersBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}