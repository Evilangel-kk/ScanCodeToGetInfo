package com.example.couriersystem

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.couriersystem.databinding.ActivityCodeMessageBinding

class CodeMessageActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCodeMessageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityCodeMessageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.orderId.text=Order.Id
        binding.orderLocation.text=Order.Location
        binding.addresseeName.text=Order.AddresseeName
        binding.addresseePhone.text=Order.AddresseePhone
    }
}