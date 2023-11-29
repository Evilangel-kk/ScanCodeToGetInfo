package com.example.couriersystem

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.couriersystem.databinding.ActivityCourierOperateBinding

class CourierOperateActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCourierOperateBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityCourierOperateBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}