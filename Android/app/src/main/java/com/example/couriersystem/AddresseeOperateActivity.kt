package com.example.couriersystem

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.couriersystem.databinding.ActivityAddresseeOperateBinding

class AddresseeOperateActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddresseeOperateBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityAddresseeOperateBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}